package net.bmgames.game.commands

import arrow.core.rightIfNotNull
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Normal
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Helping methods which make the code easier to read and understand.
 */

/**
 * Gets the room of the player, which the method is executed on
 *
 * @param game the game the player is in.
 *
 * @return the room of the player in the game or null.
 */
fun Normal.getRoom(game: Game) =
    game.getRoom(room).rightIfNotNull {message("game.geork")}

/**
 * Checks if a player is in a given room
 *
 * @param roomName the room which gets checked.
 *
 * @return true if the player is in the room, false if the player is not in the room.
 */
@OptIn(ExperimentalContracts::class)
fun Player.isInRoom(roomName: String): Boolean {
    contract {
        returns() implies (this@isInRoom is Normal)
    }
    return this is Normal && (room == roomName)
}

/**
 * Gets a player in the given room based on his ingameName
 *
 * @param game the game the player is in.
 * @param target the username which gets checked for.
 *
 * @return the player if there is a player with the username in the room or null if there is no player with such a name
 */
fun Normal.findDifferentPlayerInRoom(game: Game, target: String): Normal? =
    game.onlinePlayers.values
        .find { p -> p != this && p.isInRoom(room) && p.ingameName == target }
        ?.let { it as Normal }

/**
 * Gets all players in the same room as the player the method is executed on.
 *
 * @param game the game the player is in.
 *
 * @return a map of all players in the same room as the player the method is executed on.
 */
@Suppress("UNCHECKED_CAST")
fun Normal.getOtherPlayersInRoom(game: Game): Map<String, Normal> =
    game.onlinePlayers.filter { (_, other) ->
        other != this && other.isInRoom(room)
    } as Map<String, Normal>


/**
 * Makes a string pretty, to make it displayable for the players.
 */
fun <T> Collection<T>.prettyJoin(
    empty: String = "",
    singularVerb: String = "ist",
    pluralVerb: String = "sind",
    prefix: String = "",
    suffix: String = "",
    transform: (T) -> String = Any?::toString
) = when (size) {
    0 -> empty
    1 -> prefix + " " + transform(first()) + " " + singularVerb + " " + suffix
    else -> prefix + " " + take(size - 1).joinToString(", ", transform = transform) +
            " und " + transform(last()) + " " + pluralVerb + " " + suffix
}
