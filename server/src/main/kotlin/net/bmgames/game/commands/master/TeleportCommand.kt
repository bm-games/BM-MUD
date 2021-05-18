package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.MoveAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither

/**
 * A mastercommand which teleports a player into a new room.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who will be teleported.
 * @param newRoomID the identifier of the room the player will be teleported to.
 *
 * @constructor creates a complete teleport command.
 */
class TeleportCommand : MasterCommand("teleport") {
    val playerName: String by argument(name = "player", help = message("game.teleport.target"))
    val destination: String by argument(help = message("game.teleport.to"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It spawns the specified entities in the given room 'amount' times.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> = either.eager {
        val to = game.getRoom(destination)
            .rightIfNotNull { message("game.teleport.destination-not-found", destination) }.bind()
        game.getOnlineNormal(playerName).toEither(
            { message("game.teleport.target-not-found", playerName) },
            { p ->
                listOf(
                    p.sendText(message("game.teleport.you-were-teleportet", destination)),
                    MoveAction(p, to)
                )
            }
        ).bind()
    }
}
