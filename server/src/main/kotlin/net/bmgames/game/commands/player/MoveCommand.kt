package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.MoveAction
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room
import net.bmgames.success

/**
 * eg. move north
 * */
class MoveCommand : PlayerCommand("move") {

    enum class Direction { NORTH, EAST, SOUTH, WEST }
    val direction: Direction by argument(help = message("game.direction")).enum()

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> = either.eager {
        val room = player.getRoom(game).bind()
        val actions = mutableListOf<Action>()
        val destinationRoom : Room?
            when(direction) {
                Direction.NORTH -> {
                    destinationRoom = room.north?.let { game.getRoom(it) }
                }
                Direction.EAST -> {
                    destinationRoom = room.east?.let { game.getRoom(it) }!!
                }
                Direction.WEST -> {
                    destinationRoom = room.west?.let { game.getRoom(it) }!!
                }
                Direction.SOUTH -> {
                    destinationRoom = room.south?.let { game.getRoom(it) }!!
                }
            }
        if(destinationRoom == null)
            errorMsg(message("game.destination"))
        else{
            actions.add(MoveAction(player.left(), room, destinationRoom))
            success(actions)
        }.bind()
    }

}

