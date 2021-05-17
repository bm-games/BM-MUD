package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import net.bmgames.*
import net.bmgames.game.action.Action
import net.bmgames.game.action.HealthAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class HitTargetCommand : MasterCommand("hit") {
    val target: String by argument(help = message("game.damage-user"))
    private val amount: Int by argument(help = message("game.damage-amount"))
        .int()

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        val targetPlayer = game.onlinePlayers[target]
        return if (targetPlayer is Player.Normal) {
            listOf(
                targetPlayer.sendText(message("game.master.hit.player-msg", amount)),
                HealthAction(targetPlayer.left(), amount)
            ).right()
        } else {
            errorMsg("Player ${this.target} not found.")
        }
    }


}
