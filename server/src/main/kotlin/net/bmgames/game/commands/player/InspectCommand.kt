package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.*
import net.bmgames.game.action.EntityAction.Type.Create
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.game.commands.isInRoom
import net.bmgames.message
import net.bmgames.state.model.*
import net.bmgames.state.model.Player.Normal
import net.bmgames.success

/**
 * A playercommand which inspects an entity in the executings player's room..
 * The params are given trough arguments -> "by argument"
 *
 * @param target the entity the player inspects.
 *
 * @constructor creates a complete inspect command.
 */
class InspectCommand : PlayerCommand("inspect") {
    val target: String by argument(help = message("game.inspect.target"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It shows information on the inspected target. The target can be a NPC, an item or another player.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Normal, game: Game): Either<String, List<Action>> = either.eager {
        val room = player.getRoom(game).bind()
        val actions = mutableListOf<Action>()
        game.onlinePlayers.values
            .find { p -> p != player && p.isInRoom(room.name) && p.ingameName == target }
            ?.run {
                with(this as Normal) {
                    actions.add(
                        player.sendText(
                            message("game.player", ingameName, avatar.race.name, avatar.clazz.name)
                        )
                    )
                }
            }
        room.npcs[target]?.apply {
            when (this) {
                is NPC.Friendly -> {
                    actions.add(player.sendText(message("game.npc-friendly", name, messageOnTalk)))
                    if (items.isNotEmpty()) {
                        val item = items.random()
                        actions.addAll(
                            listOf(
                                player.sendText("$name: $messageOnTalk"),
                                player.sendText(message("game.npc-friendly", item.name)),
                                NPCInventoryAction(this, room, items - item),
                                EntityAction(Create, room, item.right())
                            )
                        )
                    }

                    if (commandOnInteraction.isNotEmpty()) {
                        actions.add(MasterCommandAction(commandOnInteraction))
                    }
                }
                is NPC.Hostile -> {
                    actions.add(
                        player.sendText(
                            message("game.npc-hostile",
                                name,
                                nextAttackTimePoint().secondsRemaining(),
                                items.joinToString(" ") { item -> item.name })
                        )
                    )
                }
            }
        }
        room.items.filter { it.name == target }
            .forEach { item ->
                when (item) {
                    is Consumable -> {
                        actions.add(player.sendText(message("game.is-consumable", target)))
                    }
                    is Equipment -> {
                        actions.add(
                            player.sendText(
                                message(
                                    "game.equipment",
                                    target,
                                    item.slot,
                                    item.healthModifier.toRelativePercent(),
                                    item.damageModifier.toRelativePercent()
                                )
                            )
                        )
                    }
                    is Weapon -> {
                        actions.add(
                            player.sendText(
                                message("game.wield-weapon", target, item.damage)
                            )
                        )
                    }
                }
            }

        if (actions.isEmpty()) {
            errorMsg(message("game.entity-not-found", target))
        } else {
            success(actions)
        }.bind()
    }
}


