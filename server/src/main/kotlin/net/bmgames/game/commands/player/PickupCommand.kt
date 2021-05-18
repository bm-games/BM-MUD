package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.EntityAction
import net.bmgames.game.action.EntityAction.Type.Remove
import net.bmgames.game.action.InventoryAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.message
import net.bmgames.state.model.*

/**
 * A playercommand which removes an item from a room and adds it to a player's inventory.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the target item which gets added to the players inventory and removed from the room.
 *
 * @constructor creates a complete pickup command.
 */
class PickupCommand : PlayerCommand("pickup") {
    val target: String by argument(help = message("game.item-name"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It picks up an item from the player's room and adds it to the player's inventory.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        if (player.inventory.items.size >= INVENTORY_SIZE) errorMsg(message("game.full-inventory"))
        else either.eager {
            val room = player.getRoom(game).bind()
            val item = room.items.find { it.name == target }
                .rightIfNotNull {message("game.target-not-found", target)}.bind()

            listOf(
                player.sendText("+$target"),
                InventoryAction(player, Inventory.items.modify(player.inventory) { it + item }),
                EntityAction(Remove, room, item.right())
            )
        }
}
