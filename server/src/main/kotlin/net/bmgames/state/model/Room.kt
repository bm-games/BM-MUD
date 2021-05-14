package net.bmgames.state.model

import arrow.optics.optics
import kotlinx.serialization.Serializable
import net.bmgames.game.commands.player.MoveCommand
import net.bmgames.game.commands.player.MoveCommand.Direction

/**
 *
 * */
@Serializable
@optics
data class Room(
    val name: String,
    val message: String,
    val north: String? = null,
    val east: String? = null,
    val south: String? = null,
    val west: String? = null,
    val items: List<Item> = emptyList(),
    val npcs: Map<String, NPC> = emptyMap(),
    val id: Int? = null,
) {
    fun getNorth(game: Game): Room? = north?.let(game::getRoom)
    fun getSouth(game: Game): Room? = south?.let(game::getRoom)
    fun getWest(game: Game): Room? = west?.let(game::getRoom)
    fun getEast(game: Game): Room? = east?.let(game::getRoom)
    fun getNext(game: Game, direction: Direction): Room? = when (direction) {
        Direction.NORTH -> getNorth(game)
        Direction.EAST -> getEast(game)
        Direction.SOUTH -> getSouth(game)
        Direction.WEST -> getWest(game)
    }
}
