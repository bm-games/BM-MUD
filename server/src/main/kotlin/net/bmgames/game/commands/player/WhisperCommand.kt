package net.bmgames.game.commands.player

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.message.Message.Chat
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import net.bmgames.toList

class WhisperCommand : PlayerCommand("whisper") {
    val target: String by argument(help = message("game.chat.recipient"))
    val message: String by argument(help = message("game.chat.message"))

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
