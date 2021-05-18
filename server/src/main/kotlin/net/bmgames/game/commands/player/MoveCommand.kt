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
import net.bmgames.game.message.MapBuilder
import net.bmgames.game.message.Message.Map
import net.bmgames.message
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room


/**
 * A playercommand which moves the player based on a given direction.
 * The params are given trough arguments -> "by argument"
 *
 * @param direction the direction the player is moved in. The player gets moved to adjacent rooms based on the direction.
 *
 * @constructor creates a complete move command.
 */
class MoveCommand : PlayerCommand("move") {

    val direction: Direction by argument(help = message("game.move.direction")).enum()

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It moves the player to an adjacent room based on the direction.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<ErrorMessage, List<Action>> = either.eager {
        val from = player.getRoom(game).bind()
        val to: Room = from.getNeighbour(game, direction).rightIfNotNull { message("game.move.room-not-found") }.bind()
        val mapBuilder = MapBuilder(game, player.visitedRooms.plus(to.name), player)
        listOf(
            MoveAction(player, to),
            MessageAction(player, Map(mapBuilder.build(to))),
            player.sendText(message("game.move.new-room", to.name, to.message)),
        )
    }

}
