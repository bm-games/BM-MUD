package net.bmgames.game.commands.player

import arrow.core.*
import arrow.core.computations.either
import arrow.typeclasses.Monoid
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.*
import net.bmgames.game.action.EntityAction.Type.Create
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.message
import net.bmgames.modify
import net.bmgames.set
import net.bmgames.state.model.*
import net.bmgames.translate

class DropItemCommand : PlayerCommand("drop") {
    val target: String by argument(help = message("game.item-name"))

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        either.eager {
            val room = player.getRoom(game).bind()
            val item = player.inventory.allItems().find { it.name == target }
                .rightIfNotNull { message("game.target-not-found", target) }.bind()

            if (item is Equipment && player.inventory.equipment.containsValue(item) ||
                item is Weapon && player.inventory.weapon == item
            ) {
                val newInv = listOfNotNull(
                    if (item is Equipment) {
                        Inventory.equipment.modify { it - item.slot }
                    } else {
                        { it.copy(weapon = null) }
                    },
                    if (player.inventory.items.size < INVENTORY_SIZE) {
                        Inventory.items.modify { it + item }
                    } else null)
                    .foldMap(Monoid.endo(), ::Endo)
                    .f(player.inventory)

                listOfNotNull(
                    player.sendText("${item.name} (${item.translate()}) -> Inventar"),
                    InventoryAction(player, newInv),
                    if (player.inventory.items.size >= INVENTORY_SIZE)
                        EntityAction(Create, room, item.right())
                    else null
                )
            } else {
                listOf(
                    InventoryAction(player, player.inventory.copy(items = player.inventory.items - item)),
                    EntityAction(Create, room, item.right()),
                    player.sendText("-" + item.name)
                )
            }


        }
}
