package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * A mastercommand which sends a message to a specific player. The params are given trough arguments -> "by argument"
 *
 * @param target the target who gets the whispered message.
 * @param message the message which is to be sent.
 *
 * @constructor creates a complete master whisper command.
 */
class MasterWhisperCommand : MasterCommand("whisper") {
    val target: String by argument(help = message("game.whisper-to"))
    val message: String by argument(help = message("game.whisper-message"))
        .multiple(true)
        .transformAll { it.joinToString(" ") }
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It sends the whispered message based on the the given arguments.
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
