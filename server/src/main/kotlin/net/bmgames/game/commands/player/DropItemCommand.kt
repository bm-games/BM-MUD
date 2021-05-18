package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.EntityAction
import net.bmgames.game.action.InventoryAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Player
import net.bmgames.state.model.items

/**
 * A playercommand which drops an item from the players inventory.
 * The item gets added to the room the player is in.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the item which will be dropped from the players inventory.
 *
 * @constructor creates a complete drop command.
 */
class DropItemCommand : PlayerCommand("drop") {
    val target: String by argument(help = message("game.drop-item"))

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
        either.eager {
            val room = player.getRoom(game).bind()
            val item =
                player.inventory.allItems().find { it.name == target }
                    .rightIfNotNull {message("game.target-not-found").format(target)}.bind()

            listOf(
                player.sendText("+$target"),
                InventoryAction(player, Inventory.items.modify(player.inventory) { it + item }),
                EntityAction(EntityAction.Type.Remove, room, item.right())
            )
        }
}
