package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

/**
 *
 * */
@Serializable
@optics
data class Room(
    val name: String,
    val message: String,
    val neighbours: Map<Direction, String>,
    val items: List<Item> = emptyList(),
    val npcs: Map<String, NPC> = emptyMap(),
    val id: Int? = null,
) {
    fun getNeighbour(game: Game, direction: Direction): Room? = neighbours[direction]?.let(game::getRoom)
}
