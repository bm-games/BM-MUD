package net.bmgames.game.commands.player

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class UseItemCommand : PlayerCommand("use") {
    val target: String by argument(help = message("game.drop-item"))
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
