package net.bmgames.game.commands.player

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * A playercommand which uses an item in the room or in the player's inventory.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the item the players wants to use.
 *
 * @constructor creates a complete use item command.
 */
class UseItemCommand : PlayerCommand("use") {
    val target: String by argument(help = message("game.use-item"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It uses an item, the player can use. It executes all commands bound to the item.
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
