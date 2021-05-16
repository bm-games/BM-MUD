package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.ErrorMessage
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.action.MoveAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.game.message.Message.Map
import net.bmgames.game.message.toMap
import net.bmgames.message
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room


/**
 * eg. move north
 * */
class MoveCommand : PlayerCommand("move") {

    val direction: Direction by argument(help = message("game.move.direction")).enum()


    override fun toAction(player: Player.Normal, game: Game): Either<ErrorMessage, List<Action>> = either.eager {
        val from = player.getRoom(game).bind()
        val to: Room = from.getNeighbour(game, direction).rightIfNotNull { message("game.move.room-not-found") }.bind()

        listOf(
            MoveAction(player, from, to),
            MessageAction(player, Map(player.visitedRooms.plus(to.name).toMap(game::getRoom, to))),
            player.sendText(message("game.move.new-room", to.name, to.message)),
        )
    }

}
