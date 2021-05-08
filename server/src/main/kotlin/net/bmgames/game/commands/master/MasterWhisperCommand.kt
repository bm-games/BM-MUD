package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player


class MasterWhisperCommand : MasterCommand("whisper") {
    val target: String by argument(help = "The player you want to whisper to")
    val message: String by argument(help = "The message you want to send")
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
