package net.bmgames.game.message

import kotlinx.serialization.Serializable
import net.bmgames.game.message.Tile.Type.NotVisited
import net.bmgames.game.message.Tile.Type.Visited
import net.bmgames.state.model.Direction.*
import net.bmgames.state.model.Room

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
    val players: Set<String>,
    val npcs: Set<String> = emptySet(),
    val items: List<String> = emptyList(),
    val type: Type
) {
    enum class Type { Current, Visited, NotVisited }
}

