package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * A mastercommand which kicks the given player from the game.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who should be kicked.
 *
 * @constructor creates a complete invitation command.
 */
class KickPlayerCommand : MasterCommand("kick") {
    val target: String by argument(help = message("game.kick-user"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It kicks the player from the game.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
