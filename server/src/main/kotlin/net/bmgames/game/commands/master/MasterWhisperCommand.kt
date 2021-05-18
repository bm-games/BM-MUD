package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.message.Message
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import net.bmgames.toList


class MasterWhisperCommand : MasterCommand("whisper") {
    val target: String by argument(help = message("game.chat.recipient"))
    val message: String by argument(help = message("game.chat.message"))

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        game.onlinePlayers[target].toEither(
            { message("game.target-not-found") },
            { MessageAction(it, Message.Chat(player.ingameName + " (Master)", message)).toList() }
        )
}
