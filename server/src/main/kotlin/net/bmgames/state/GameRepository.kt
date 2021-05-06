package net.bmgames.state


import net.bmgames.database.*
import net.bmgames.database.CommandTable.Type.Alias
import net.bmgames.database.ItemConfigTable.Type
import net.bmgames.state.model.*
import net.bmgames.state.model.Equipment.Slot
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.transactions.transaction


/**
 * Interface to the database.
 * */
object GameRepository {
    /**
     * Writes the game state into the database while updating or creating the sub entities like rooms or NPCs.
     * */
    internal fun create(game: Game): Unit {

        val gameDAO = transaction {
            GameDAO.new {
                name = game.name
                startRoom = game.startRoom
                masterUser = UserDAO[game.master.user.id]
                races = emptySized()
                classes = emptySized()
                commands = emptySized()
                npcConfigs = emptySized()
                itemConfigs = emptySized()
                startItems = emptySized()
                rooms = emptySized()
                players = emptySized()
                joinRequests = emptySized()
            }
        }

//            val maxId = GameTable.id.max()
//            val gameId = GameTable.slice(maxId).selectAll().map { it[maxId] }.first()?.value ?: -1 + 1
//            val gamedao = GameDAO(EntityID(gameId, GameTable))
//            addLogger(StdOutSqlLogger)
        val raceDAOs = transaction {
            game.races
                .map { race ->
                    RaceDAO.new {
                        this.game = gameDAO
                        name = race.name
                        health = race.health
                        damageMultiplier = race.damageModifier
                        description = race.description
                    }
                }
        }
        val classDAOs = transaction {
            game.classes
                .map { clazz ->
                    ClassDAO.new {
                        this.game = gameDAO
                        name = clazz.name
                        healthMultiplier = clazz.healthMultiplier
                        damage = clazz.damage
                        attackSpeed = clazz.attackSpeed
                        description = clazz.description
                    }
                }
        }
        val itemConfigsDAOs = transaction {
            game.itemConfigs
                .mapValues { (_, item) ->
                    when (item) {
                        is Consumable -> ItemConfigDAO.new {
                            this.game = gameDAO
                            name = item.name
                            type = Type.Consumable
                            effect = item.effect
                        }
                        is Equipment -> ItemConfigDAO.new {
                            this.game = gameDAO
                            name = item.name
                            type = when (item.slot) {
                                Slot.Head -> Type.Head
                                Slot.Chest -> Type.Chest
                                Slot.Legs -> Type.Legs
                                Slot.Boots -> Type.Boots
                            }
                            healthModifier = item.healthModifier
                            damageModifier = item.damageModifier
                        }
                        is Weapon -> ItemConfigDAO.new {
                            this.game = gameDAO
                            name = item.name
                            type = Type.Consumable
                            baseDamage = item.damage
                        }
                    }
                }
        }
        val npcConfigDAOs = transaction {
            game.npcConfigs
                .mapValues { (_, npc) ->
                    when (npc) {
                        is NPC.Hostile -> NPCConfigDAO.new {
                            this.game = gameDAO
                            name = npc.name
                            friendly = false
                            maxHealth = npc.health
                            damage = npc.damage
                        }
                        is NPC.Friendly -> NPCConfigDAO.new {
                            this.game = gameDAO
                            name = npc.name
                            friendly = true
                            command = npc.commandOnInteraction
                            message = npc.messageOnTalk
                        }
                    }
                }
        }
        val roomDAOs = transaction {
            game.rooms.map { (_, room) ->
//                val itemDAOs = SizedCollection(room.items.map { itemConfigsDAOs[it.name]!! })
                val roomConfig = RoomConfigDAO.new {
                    this.game = gameDAO
                    name = room.name
                    message = room.message
                    north = room.north
                    east = room.east
                    west = room.west
                    south = room.south
                }
                /*RoomDAO.new {
                    this.game = gameDAO
                    config = roomConfig
                    items = emptySized()
//                        npcs = emptySized()
                }*/
            }
        }

//        val roomDAOs = transaction {
//            game.rooms
//                .map { (_, room) ->
//                    val itemDAOs = SizedCollection(room.items.map { itemConfigsDAOs[it.name]!! })
//                    val npcDAOs = room.npcs
//                        .map { (name, npc) ->
//                            val npcItemDAOs = SizedCollection(npc.items.map { itemConfigsDAOs[it.name]!! })
//                            npcConfigDAOs[name]!!.let { config ->
//                                NPCDAO.new {
//                                    npcConfig = config
//                                    items = npcItemDAOs
//                                    health = config.maxHealth
//                                }
//                            }
//                        }
//                    val roomConfig = RoomConfigDAO.new {
//                        this.game = gameDAO
//                        name = room.name
//                        message = room.message
//                        north = room.north
//                        east = room.east
//                        west = room.west
//                        south = room.south
//                    }
//                    val roomDAO = RoomDAO.new {
//                        this.game = gameDAO
//                        config = roomConfig
//                        items = itemDAOs
//                        npcs = SizedCollection(npcDAOs)
//                    }
//                    npcDAOs.forEach {
//                        it.room = roomDAO
//                    }
//                    return@map roomDAO
//                }
//        }


        val commandDAOs = transaction {
            with(game.commandConfig) {
                (aliases + customCommands).map { (from, to) ->
                    CommandDAO.new {
                        this.game = gameDAO
                        type = Alias
                        original = from
                        new = to
                    }
                }
            }
        }

        val startItemDAOs = transaction {
            game.startItems.map { itemConfigsDAOs[it.name]!! }
        }
        transaction {
            with(gameDAO) {
                races = SizedCollection(raceDAOs)
                classes = SizedCollection(classDAOs)
                commands = SizedCollection(commandDAOs)
                npcConfigs = SizedCollection(npcConfigDAOs.values)
                itemConfigs = SizedCollection(itemConfigsDAOs.values)
                startItems = SizedCollection(startItemDAOs)
//                rooms = SizedCollection(roomDAOs)
            }
        }

    }

    /**
     * Loads the gamestate from the database and decodes it into a game object.
     * @return [Game] if it exists, null otherwise
     * */
    internal fun loadGame(name: String): Game? = transaction {
        GameDAO.find { GameTable.name eq name }
            .firstOrNull()?.toGame()
    }

/*
    internal fun loadPlayer(gameName: String, ingameName: String): Player.Normal? {
    return transaction {
        addLogger(StdOutSqlLogger)
        val query = PlayerTable.innerJoin(GameTable).innerJoin(AvatarTable)
            .slice(PlayerTable.columns)
            .select { GameTable.name eq gameName and (AvatarTable.name eq ingameName) }
            .withDistinct()

        PlayerDAO.wrapRows(query)
            .firstOrNull()
            ?.toPlayer()
    }
}

internal fun createPlayer(game: Game, player: Player.Normal): Unit {
    transaction {
        addLogger(StdOutSqlLogger)
        val avatarDAO = AvatarDAO.new {
            name = player.avatar.name
            race = RaceDAO[player.avatar.race.id]
            clazz = ClassDAO[player.avatar.clazz.id]
        }
        val visitedRoomDAO = VisitedRoomDAO.new {
            this.game = game.name
            room = game.startRoom
        }
        val startItems = game.startItems
            .map { item -> ItemConfigDAO[item.id] }
            .let(::SizedCollection)

        val playerDAO = PlayerDAO.new {
            this.game = game.name
            user = UserDAO[player.user.id]
            avatar = avatarDAO
            room = game.startRoom
            health = avatar.race.health
            inventory = startItems
            visitedRooms = SizedCollection(visitedRoomDAO)
        }
        visitedRoomDAO.player = playerDAO
    }
}*/
    /**
     * Loads every existing game.
     * */
    internal fun listGames(): List<Game> {
        return emptyList()//TODO("Load from DB")
    }
}
