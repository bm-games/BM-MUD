package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.right
import net.bmgames.game.action.Action
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.*
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toList


class StatsCommand : PlayerCommand("stats", message("game.stats-epilog")) {
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It drops the item from the players inventory.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
            player.sendText(message("game.stats", player.healthPoints, player.maxHealthPoints, player.damage.toInt()))
                .toList()
                .right()
}
