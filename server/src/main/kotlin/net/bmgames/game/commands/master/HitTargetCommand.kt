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

/**
 * A mastercommand which hits the given player with a based on the given amount.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who gets hit.
 * @param amount the amount the player gets hit for.
 *
 * @constructor creates a complete hit target command.
 */
class HitTargetCommand : MasterCommand("hit") {
    val target: String by argument(help = message("game.damage-user"))
    private val amount: Int by argument(help = message("game.damage-amount"))
        .int()
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
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
