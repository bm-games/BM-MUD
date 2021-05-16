package net.bmgames.game.commands.master

import arrow.core.Either
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

class MasterSayCommand : MasterCommand("say") {

    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        try {
            Either.Right(listOf<Action>(MessageAction(player, Message.Text(message))))
        } catch (e: Error) {
            Either.Left(message("game.say-function"))
        }
}
