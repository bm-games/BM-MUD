package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.authentication.User

/**
 * @property allowedUsers Maps every allowed user to his avatars
 * */
@Serializable
@optics
data class Game(
    val id: Int? = null,
    val name: String,

    val races: List<Race>,
    val classes: List<Clazz>,
    val commandConfig: CommandConfig,
    val startItems: List<Item> = emptyList(),
    val npcConfigs: Map<String, NPC> = emptyMap(),
    val itemConfigs: Map<String, Item> = emptyMap(),

    val startRoom: String,
    val rooms: Map<String, Room>,

    val master: Player.Master,
    val allowedUsers: Map<String, List<String>> = emptyMap(),
    val onlinePlayers: Map<String, Player> = emptyMap(),
    val joinRequests: List<User> = emptyList()
) {

    fun getPlayer(name: String): Player? = onlinePlayers[name]
    fun isMasterOnline() = onlinePlayers.containsKey(master.ingameName)

    fun getStartRoom(): Room = rooms[startRoom]!!
    fun getRoom(name: String): Room? = rooms[name]
}



