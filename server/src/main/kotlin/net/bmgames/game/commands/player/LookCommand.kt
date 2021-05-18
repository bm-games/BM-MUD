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


/**
 * A playercommand which shows the contents of the executing player's room to said player.
 * The params are given trough arguments -> "by argument"
 *
 * @constructor creates a complete look command.
 */
class LookCommand : PlayerCommand("look") {
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It shows the contents of the executing player's room.
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
