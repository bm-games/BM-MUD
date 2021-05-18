package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.*
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.guard
import net.bmgames.message
import net.bmgames.state.model.*
import net.bmgames.state.model.Equipment.Slot.*
import net.bmgames.state.model.Player.Normal.Companion
import net.bmgames.translate

class UseItemCommand : PlayerCommand("use") {
    val target: String by argument(help = message("game.item-name"))
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        either.eager {
            val item = player.inventory.items.find { it.name == target }
                .rightIfNotNull { message("game.item.not-in-inv", target) }.bind()

            when (item) {
                is Consumable -> {
                    listOfNotNull(
                        player.sendText("-${item.name}"),
                        InventoryAction(player, Inventory.items.modify(player.inventory) { it - item }),
                        if (item.effect != null && item.effect.isNotEmpty()) MasterCommandAction(item.effect)
                        else null
                    ).right()
                }
                is Equipment -> {
                    if (player.inventory.equipment.containsKey(item.slot)) {
                        message("game.use.equipment-slot-full", item.translate()).left()
                    } else {
                        listOf(
                            player.sendText("${item.name} -> (${item.translate()})"),
                            InventoryAction(player, with(player.inventory) {
                                copy(
                                    items = items - item,
                                    equipment = equipment + (item.slot to item)
                                )
                            })
                        ).right()
                    }
                }
                is Weapon -> {
                    if (player.inventory.weapon != null) {
                        message("game.use.weapon-slot-full").left()
                    } else {
                        listOf(
                            player.sendText(message("game.use.weapon", item.name, item.damage)),
                            InventoryAction(player, with(player.inventory) {
                                copy(items = items - item, weapon = item)
                            })
                        ).right()
                    }
                }
            }.bind()
        }
}
