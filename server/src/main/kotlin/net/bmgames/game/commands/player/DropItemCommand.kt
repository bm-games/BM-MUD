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
import net.bmgames.state.model.Game
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Player
import net.bmgames.state.model.items

class DropItemCommand : PlayerCommand("drop") {
    val target: String by argument(help = "The item you want to drop")

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        either.eager {
            val room = player.getRoom(game).bind()
            val item =
                player.inventory.allItems().find { it.name == target }.rightIfNotNull { "Couldn't find $target" }.bind()

            listOf(
                player.sendText("+$target"),
                InventoryAction(player, Inventory.items.modify(player.inventory) { it + item }),
                EntityAction(EntityAction.Type.Remove, room, item.right())
            )
        }
}
