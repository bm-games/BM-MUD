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

class PickupCommand : PlayerCommand("pickup") {
    val target: String by argument(help = message("game.pickup-item"))

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        if (player.inventory.items.size >= INVENTORY_SIZE) errorMsg(message("game.full-inventory"))
        else either.eager {
            val room = player.getRoom(game).bind()
            val item = room.items.find { it.name == target }.rightIfNotNull {message("game.target-not-found").format(target)}.bind()

            listOf(
                player.sendText("+$target"),
                InventoryAction(player, Inventory.items.modify(player.inventory) { it + item }),
                EntityAction(Remove, room, item.right())
            )
        }
}
