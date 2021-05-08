package net.bmgames.game.commands

import arrow.core.rightIfNotNull
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Normal

fun Normal.getRoom(game: Game) =
    game.getRoom(room).rightIfNotNull { "Geork: I don't know how you got here, but your room doesn't exist." }

fun Player.isInRoom(roomName: String) = this is Normal && (room == roomName)

fun Normal.findDifferentPlayerInRoom(game: Game, target: String): Normal? =
    game.onlinePlayers.values
        .find { p -> p != this && p.isInRoom(room) && p.ingameName == target }
        ?.let { it as Normal }
