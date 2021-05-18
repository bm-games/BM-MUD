package net.bmgames.game.commands.player

import arrow.core.Either
import net.bmgames.game.action.Action
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * A playercommand which shows the contents of the executing player's inventory to said player.
 * The params are given trough arguments -> "by argument"
 *
 * @constructor creates a complete inventory command.
 */
class InventoryCommand : PlayerCommand("inventory") {
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It shows the contents of the executing player's inventory.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
