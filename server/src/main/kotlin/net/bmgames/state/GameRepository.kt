package net.bmgames.state


import net.bmgames.state.database.*
import net.bmgames.state.database.CommandTable.Type.Alias
import net.bmgames.state.database.CommandTable.Type.Custom
import net.bmgames.state.database.ItemConfigTable.Type
import net.bmgames.state.model.*
import net.bmgames.state.model.Equipment.Slot
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ConcurrentHashMap


/**
 * Interface to the database.
 * */
object GameRepository {

    private val cache: ConcurrentHashMap<String, Game?> by lazy {
        ConcurrentHashMap<String, Game?>().apply {
            transaction {
                GameDAO.all().forEach { put(it.name, it.toGame()) }
            }
        }
    }

    /**
     * Loads every existing game.
     * */
    internal fun listGames(): List<Game> = cache.mapNotNullTo(mutableListOf()) { it.value }

    /**
     * Loads the gamestate from the database and decodes it into a game object.
     * @return [Game] if it exists, null otherwise
     * */
    internal fun loadGame(name: String): Game? = cache.computeIfAbsent(name) {
        transaction {
            GameDAO.find { GameTable.name eq name }
                .firstOrNull()?.toGame()
        }
    }

    /**
     * Deletes the game as well as every referenced entity. This also includes all players.
     * */
    fun delete(game: Game): Unit {
        cache.remove(game.name)

        transaction {
            game.rooms.forEach { (_, room) ->
                room.npcs.mapNotNull { (_, npc) -> npc.id }
                    .forEach { id ->
                        NPCItemTable.deleteWhere { NPCItemTable.npcId eq id }
                    }
                NPCTable.deleteWhere { NPCTable.roomId eq room.id }
                RoomItemTable.deleteWhere { RoomItemTable.roomId eq room.id }
            }

            PlayerRepository.deletePlayersInGame(game.id)

            mapOf<Table, () -> Column<EntityID<Int>>>(
                ClassTable to ClassTable::game,
                CommandTable to CommandTable::game,
                StartItemTable to StartItemTable::game,
                ItemConfigTable to ItemConfigTable::game,
                JoinRequestTable to JoinRequestTable::game,
                NPCConfigTable to NPCConfigTable::game,
                RaceTable to RaceTable::game,
                RoomTable to RoomTable::game,
                GameTable to GameTable::id
            ).forEach { (table, column) ->
                table.deleteWhere { column() eq game.id }
            }
        }
    }


    /**
     * Writes the game state into the database while updating or creating the sub entities like rooms or NPCs.
     * */
    internal fun save(game: Game): Unit {
        val gameDAO = transaction {
            GameDAO.updateOrCreate(game.id) {
                name = game.name
                startRoom = game.startRoom
                masterUser = UserDAO[game.master.user.id!!]
            }
        }

        transaction {
            gameDAO.joinRequests = game.joinRequests.map { UserDAO[it.id!!] }.toSized()
        }

        val npcConfigDAOs = saveNPCConfigs(game, gameDAO)
        val itemConfigsDAOs = saveItemConfigs(game, gameDAO)
        val roomDAOs = saveRooms(game, gameDAO, itemConfigsDAOs)

        transaction {
            saveNPCs(roomDAOs, npcConfigDAOs, itemConfigsDAOs)
            if (game.id == null) { // Only if game is new
                saveRacesAndClasses(game, gameDAO)
                saveCommandConfig(game.commandConfig, gameDAO)
                gameDAO.startItems = game.startItems.mapNotNull { itemConfigsDAOs[it.name] }.toSized()
            }
        }

        cache.remove(game.name)
        loadGame(game.name)

    }

    private fun saveNPCConfigs(
        game: Game,
        gameDAO: GameDAO
    ) = transaction {
        game.npcConfigs.mapValues { (_, npcConfig) ->
            NPCConfigDAO.getOrCreate(npcConfig.id) {
                gameRef = gameDAO
                name = npcConfig.name
                when (npcConfig) {
                    is NPC.Hostile -> {
                        friendly = false
                        maxHealth = npcConfig.health
                        damage = npcConfig.damage
                    }
                    is NPC.Friendly -> {
                        friendly = true
                        command = npcConfig.commandOnInteraction
                        message = npcConfig.messageOnTalk
                    }
                }
            }
        }
    }

    private fun saveItemConfigs(
        game: Game,
        gameDAO: GameDAO
    ) = transaction {
        game.itemConfigs.mapValues { (_, item) ->
            ItemConfigDAO.getOrCreate(item.id) {
                gameRef = gameDAO
                name = item.name
                when (item) {
                    is Consumable -> {
                        type = Type.Consumable
                        effect = item.effect
                    }
                    is Equipment -> {
                        type = when (item.slot) {
                            Slot.Head -> Type.Head
                            Slot.Chest -> Type.Chest
                            Slot.Legs -> Type.Legs
                            Slot.Boots -> Type.Boots
                        }
                        healthModifier = item.healthModifier
                        damageModifier = item.damageModifier
                    }
                    is Weapon -> {
                        type = Type.Weapon
                        baseDamage = item.damage
                    }
                }
            }
        }
    }

    private fun saveRooms(
        game: Game,
        gameDAO: GameDAO,
        itemConfigsDAOs: Map<String, ItemConfigDAO>
    ) = transaction {
        val roomDAOs = transaction {
            game.rooms.map { (_, room) ->
                room to RoomDAO.updateOrCreate(room.id) {
                    gameRef = gameDAO
                    name = room.name
                    message = room.message
                    north = room.north
                    east = room.east
                    west = room.west
                    south = room.south
                }
            }
        }
        roomDAOs.forEach { (room, roomDAO) ->
            roomDAO.items = room.items.mapNotNull { itemConfigsDAOs[it.name] }.toSized()
        }
        roomDAOs
    }

    /**
     * TODO Maybe modify this => preconfigured vs. custom NPCs
     * */
    private fun Transaction.saveNPCs(
        rooms: List<Pair<Room, RoomDAO>>,
        npcConfigDAOs: Map<String, NPCConfigDAO>,
        itemConfigsDAOs: Map<String, ItemConfigDAO>
    ): Unit {
        val npcDAOs = transaction {
            rooms.flatMap { (room, roomDAO) ->
                room.npcs.mapNotNull { (_, npc) ->
                    npcConfigDAOs[npc.name]?.let { config ->
                        npc to NPCDAO.updateOrCreate(npc.id) {
                            npcConfig = config
                            health = NPC.hostile.health.getOrNull(npc)
                            roomRef = roomDAO
                        }
                    }
                }
            }
        }
        npcDAOs.forEach { (npc, npcDAO) ->
            npcDAO.items = npc.items.mapNotNull { itemConfigsDAOs[it.name] }.toSized()
        }
    }

    private fun Transaction.saveRacesAndClasses(game: Game, gameDAO: GameDAO) {
        game.races.forEach { race ->
            RaceDAO.new {
                gameRef = gameDAO
                name = race.name
                health = race.health
                damageMultiplier = race.damageModifier
                description = race.description
            }
        }
        game.classes.forEach { clazz ->
            ClassDAO.new {
                gameRef = gameDAO
                name = clazz.name
                healthMultiplier = clazz.healthMultiplier
                damage = clazz.damage
                attackSpeed = clazz.attackSpeed
                description = clazz.description
            }
        }
    }

    private fun Transaction.saveCommandConfig(commandConfig: CommandConfig, gameDAO: GameDAO) = with(commandConfig) {
        (aliases.map { it to Alias } + customCommands.map { it to Custom })
            .map { (mapping, typ) ->
                CommandDAO.new {
                    gameRef = gameDAO
                    type = typ
                    original = mapping.key
                    new = mapping.value
                }
            }
    }

}
