package net.bmgames.state.database


import net.bmgames.state.database.CommandTable.Type.Alias
import net.bmgames.state.database.CommandTable.Type.Custom
import net.bmgames.state.model.CommandConfig
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


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


    // Many to many => can be updated through this object
    var startItems by ItemConfigDAO via StartItemTable
    var joinRequests by UserDAO via JoinRequestTable

    // Immutable references from other table.
    // Update them if the gamestate changes
    val races by RaceDAO referrersOn RaceTable.game
    val classes by ClassDAO referrersOn ClassTable.game
    val itemConfigs by ItemConfigDAO referrersOn ItemConfigTable.game
    private val commands by CommandDAO referrersOn CommandTable.game
    private val npcConfigs by NPCConfigDAO referrersOn NPCConfigTable.game
    private val rooms by RoomDAO referrersOn RoomTable.game
    private val players by PlayerDAO referrersOn PlayerTable.game

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
        allowedUsers = players.groupBy({ it.user.username }, { it.avatar.name })
            .let { it.plus(masterUser.username to it.getOrDefault(masterUser.username, emptyList())) },
        onlinePlayers = emptyMap(),
        joinRequests = joinRequests.map { it.toUser() },
        id = id.value
    )
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
