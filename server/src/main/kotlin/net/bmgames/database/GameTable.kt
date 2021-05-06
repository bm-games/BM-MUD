package net.bmgames.database


import net.bmgames.database.CommandTable.Type.Alias
import net.bmgames.database.CommandTable.Type.Custom
import net.bmgames.state.model.CommandConfig
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.setId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.emptySized


/**
 * Represents the Database Table
 * */
object GameTable : IntIdTable("Game") {
    val name = varchar("gameName", NAME_LENGTH)
    val startRoom = varchar("startRoom", NAME_LENGTH)
    val master = reference("dungeonMaster", UserTable)
}

class GameDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GameDAO>(GameTable)

    var name by GameTable.name
    var startRoom by GameTable.startRoom
    var masterUser by UserDAO referencedOn GameTable.master

    var races = emptySized<RaceDAO>() // RaceDAO via RaceTable
    var classes = emptySized<ClassDAO>() // ClassDAO via ClassTable
    var commands = emptySized<CommandDAO>() // CommandDAO via CommandTable
    var npcConfigs = emptySized<NPCConfigDAO>() // NPCConfigDAO via NPCConfigTable
    var itemConfigs = emptySized<ItemConfigDAO>() // ItemConfigDAO via ItemConfigTable
    var startItems = emptySized<ItemConfigDAO>() // ItemConfigDAO via StartItemTable
    var rooms = emptySized<RoomDAO>() // RoomDAO via RoomTable
    var players = emptySized<PlayerDAO>() // PlayerDAO via PlayerTable
    var joinRequests = emptySized<UserDAO>() // UserDAO via JoinRequestTable

    fun toGame(): Game = Game(
        name = name,
        races = races.map { it.toRace() },
        classes = classes.map { it.toClass() },
        commandConfig = commands.toCommandConfig(),
        npcConfigs = npcConfigs.map { it.toNPC() }.associateBy { it.name },
        itemConfigs = itemConfigs.map { it.toItem() }.associateBy { it.name },
        startItems = startItems.map { it.toItem() },
        startRoom = startRoom,
        rooms = rooms.map { it.toRoom() }.associateBy { it.name },
        master = Player.Master(masterUser.toUser()),
        users = players.groupBy({ it.user.username }, { it.avatar.name }),
        onlinePlayers = emptyMap(),
        joinRequests = joinRequests.map { it.username }
    ).setId(id.value)
}

private fun Iterable<CommandDAO>.toCommandConfig(): CommandConfig =
    fold(CommandConfig()) { config, dao ->
        when (dao.type) {
            Alias -> config.copy(
                aliases = config.aliases.plus(dao.original to dao.new)
            )
            Custom -> config.copy(
                customCommands = config.customCommands.plus(dao.original to dao.new)
            )
        }
    }
