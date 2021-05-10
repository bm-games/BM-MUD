package net.bmgames.game.message

import kotlinx.serialization.Serializable
import net.bmgames.state.model.Player

@Serializable
data class RoomMap(
    val tiles: List<List<Tile?>>
)

@Serializable
data class Tile(
    val north: Boolean,
    val east: Boolean,
    val south: Boolean,
    val west: Boolean,
    val color: String
)

fun Player.Normal.getVisitedRooms(): RoomMap = RoomMap(emptyList()) //TODO
