package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.message.Message
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Master

/**
 * A mastercommand which sends a message to all players. The params are given trough arguments -> "by argument"
 *
 * @param message the message which is to be sent.
 *
 * @constructor creates a complete master say command.
 */
class MasterSayCommand : MasterCommand("say", message("game.master-say-epilog")) {

    val message: String by argument(help = message("game.chat.message"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It sends the message based on the the given arguments.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Master, game: Game): Either<String, List<Action>> =
        game.onlinePlayers
            .filter { (_, p) -> p != player }
            .map { (_, p) -> MessageAction(p, Message.Chat(player.ingameName + " (Master)", message)) }
            .right()

}
