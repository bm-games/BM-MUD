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

class MasterSayCommand : MasterCommand("say") {

    val message: String by argument(help = message("game.chat.message"))

    override fun toAction(player: Master, game: Game): Either<String, List<Action>> =
        game.onlinePlayers
            .filter { (_, p) -> p != player }
            .map { (_, p) -> MessageAction(p, Message.Chat(player.ingameName + " (Master)", message)) }
            .right()

}
