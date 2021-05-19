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
import java.net.URLEncoder

/**
 * A playercommand which uses an item in the room or in the player's inventory.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the item the players wants to use.
 *
 * @constructor creates a complete use item command.
 */
class UseItemCommand : PlayerCommand("use") {
    val target: String by argument(help = message("game.item-name"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It spawns the specified entities in the given room 'amount' times.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        either.eager {
            val room = player.getRoom(game).bind()
            val item = player.inventory.items.find { it.name == target }
                .rightIfNotNull { message("game.item.not-in-inv", target) }.bind()

            when (item) {
                is Consumable -> {
                    listOfNotNull(
                        player.sendText("-${item.name}"),
                        InventoryAction(player, Inventory.items.modify(player.inventory) { it - item }),
                        if (item.effect != null && item.effect.isNotEmpty())
                            MasterCommandAction(
                                item.effect
                                    .replaceItem(item).replaceRoom(room).replacePlayer(player)
                            )
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

internal fun String.replaceItem(item: Item): String =
    replace("\$item", URLEncoder.encode(item.name, Charsets.UTF_8))

internal fun String.replaceNPC(npc: NPC): String =
    replace("\$npc", URLEncoder.encode(npc.name, Charsets.UTF_8))

internal fun String.replacePlayer(player: Player): String =
    replace("\$player", URLEncoder.encode(player.ingameName, Charsets.UTF_8))

internal fun String.replaceRoom(room: Room): String =
    replace("\$room", URLEncoder.encode(room.name, Charsets.UTF_8))

