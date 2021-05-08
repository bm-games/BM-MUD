package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.error
import net.bmgames.game.action.Action
import net.bmgames.game.action.EntityAction
import net.bmgames.game.action.EntityAction.Type.Remove
import net.bmgames.game.action.InventoryAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.state.model.*

class PickupCommand : PlayerCommand("pickup") {
    val target: String by argument(help = "The item you want to pickup")

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        if (player.inventory.items.size >= INVENTORY_SIZE) error("Your inventory is already full.")
        else either.eager {
            val room = player.getRoom(game).bind()
            val item = room.items.find { it.name == target }.rightIfNotNull { "Couldn't find $target" }.bind()

            listOf(
                player.sendText("+$target"),
                InventoryAction(player, Inventory.items.modify(player.inventory) { it + item }),
                EntityAction(Remove, room, item.right())
            )
        }
}
