package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.message.Message
import net.bmgames.game.message.Message.Chat
import net.bmgames.game.message.Message.Text
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Master
import net.bmgames.state.model.Player.Normal
import net.bmgames.success
import java.net.URLDecoder

/**
 * A playercommand which sends a message to all players in the same room as the executing player and to the master.
 * The params are given trough arguments -> "by argument"
 *
 * @param message the message the player wants to send.
 *
 * @constructor creates a complete say command.
 */

class SayCommand : PlayerCommand("say") {
    val message: String by argument(help = message("game.chat.message"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It sends a message to all players in the current room.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Normal, game: Game): Either<String, List<Action>> =
        game.onlinePlayers
            .filter { (_, p) ->
                player != p && (p is Normal && p.room == player.room || p is Master)
            }.map { (_, p) ->
                MessageAction(p, Chat(player.ingameName, message))
            }.right()

}
