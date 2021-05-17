package net.bmgames.game.commands

import arrow.core.rightIfNotNull
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Normal
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun Normal.getRoom(game: Game) =
    game.getRoom(room).rightIfNotNull {message("game.geork")}

@OptIn(ExperimentalContracts::class)
fun Player.isInRoom(roomName: String): Boolean {
    contract {
        returns() implies (this@isInRoom is Normal)
    }
    return this is Normal && (room == roomName)
}

fun Normal.findDifferentPlayerInRoom(game: Game, target: String): Normal? =
    game.onlinePlayers.values
        .find { p -> p != this && p.isInRoom(room) && p.ingameName == target }
        ?.let { it as Normal }

@Suppress("UNCHECKED_CAST")
fun Normal.getOtherPlayersInRoom(game: Game): Map<String, Normal> =
    game.onlinePlayers.filter { (_, other) ->
        other != this && other.isInRoom(room)
    } as Map<String, Normal>


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