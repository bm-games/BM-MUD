package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

/**
 * @property users Maps every allowed user to his avatars
 * */
@Serializable
@optics
data class Game(
    val name: String,

    val races: List<Race>,
    val classes: List<Clazz>,
    val commandConfig: CommandConfig,
    val npcConfigs: Map<String, NPC> = emptyMap(),
    val itemConfigs: Map<String, Item> = emptyMap(),
    val startItems: List<Item> = emptyList(),

    val startRoom: String,
    val rooms: Map<String, Room>,

    val master: Player.Master,
    val users: Map<String, List<String>> = emptyMap(),
    val onlinePlayers: Map<String, Player> = emptyMap(),
    val joinRequests: List<String> = emptyList()
): IdEntity() {

    fun getPlayer(name: String): Player? = onlinePlayers[name]
    fun isMasterOnline() = onlinePlayers.containsKey(master.ingameName)

    fun getStartRoom(): Room = rooms[startRoom]!!
    fun getRoom(name: String): Room? = rooms[name]
}


