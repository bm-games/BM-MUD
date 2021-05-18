package net.bmgames.game.commands.player

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.message.Message.Chat
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import net.bmgames.toList

/**
 * A playercommand which sends a whispered message to another player in the room or the master.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player the message gets sent to.
 * @param message the message which is sent.
 *
 * @constructor creates a complete use item command.
 */
class WhisperCommand : PlayerCommand("whisper") {
    val target: String by argument(help = message("game.whisper-to"))
    val message: String by argument(help = message("game.whisper-message"))
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It sends a whispered message to a player in the current room or to the master.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        game.onlinePlayers.values
            .find {  p ->
                (p is Player.Master && p.ingameName == target) ||
                        (player != p && p.ingameName == target &&
                                p is Player.Normal && p.room == player.room)
            }.toEither(
                { "Player $target not found." },
                { p ->
                    MessageAction(p, Chat(player.ingameName, message)).toList()
                }
            )

}
