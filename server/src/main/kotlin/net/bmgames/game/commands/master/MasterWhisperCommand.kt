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


class MasterWhisperCommand : MasterCommand("whisper") {
    val target: String by argument(help = message("game.whisper-to"))
    val message: String by argument(help = message("game.whisper-message"))
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
