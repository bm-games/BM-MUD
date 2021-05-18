package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.MasterCommandAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.game.commands.isInRoom
import net.bmgames.message
import net.bmgames.state.model.*
import net.bmgames.state.model.Player.Normal
import net.bmgames.success

class InspectCommand : PlayerCommand("inspect") {
    val target: String by argument(help = message("game.inspect.target"))

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
                    actions.add(
                        player.sendText(
                            message("game.npc-friendly",
                                name,
                                messageOnTalk,
                                items.joinToString(" ") { item -> item.name })
                        )
                    )
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
                        actions.add(player.sendText(message("game.is-consumable",target)))
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


