package net.bmgames.game.commands

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.NPC
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Normal
import net.bmgames.state.model.Room
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun Normal.getRoom(game: Game) =
    game.getRoom(room).rightIfNotNull {message("game.room-not-found")}

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


fun Game.getPlayerOrNPC(name: String, room: Room): Either<NPC.Hostile, Normal>? {
    val player = onlinePlayers[name]?.let { if (it is Player.Normal && it.room == room.name) it else null }
    if (player != null) return player.right()

    val npc = room.npcs[name]
    if (npc != null && npc is NPC.Hostile) return npc.left()

    return null
}


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
