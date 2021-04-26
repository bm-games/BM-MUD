package net.bmgames.game.model

import kotlinx.serialization.Serializable

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
