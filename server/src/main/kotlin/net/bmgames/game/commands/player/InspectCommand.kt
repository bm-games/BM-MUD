package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.error
import net.bmgames.game.action.Action
import net.bmgames.game.action.MasterCommandAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.getRoom
import net.bmgames.game.commands.isInRoom
import net.bmgames.secondsRemaining
import net.bmgames.state.model.*
import net.bmgames.state.model.Player.Normal
import net.bmgames.success

class InspectCommand : PlayerCommand("inspect") {
    val target: String by argument(help = "The target you want to inspect")

    override fun toAction(player: Normal, game: Game): Either<String, List<Action>> = either.eager {
        val room = player.getRoom(game).bind()
        val actions = mutableListOf<Action>()
        game.onlinePlayers.values
            .find { p -> p != player && p.isInRoom(room.name) && p.ingameName == target }
            ?.run {
                with(this as Normal) {
                    actions.add(
                        player.sendText(
                            "Player $ingameName has race ${avatar.race.name} and class ${avatar.clazz.name}."
                        )
                    )
                }
            }
        room.npcs[target]?.apply {
            when (this) {
                is NPC.Friendly -> {
                    actions.add(
                        player.sendText(
                            """${name}: "$messageOnTalk"
                            | I have some items for you: ${items.joinToString(" ") { item -> item.name }}
                        """.trimMargin()
                        )
                    )
                    if (commandOnInteraction.isNotEmpty()) {
                        actions.add(MasterCommandAction(commandOnInteraction))
                    }
                }
                is NPC.Hostile -> {
                    actions.add(
                        player.sendText(
                            """${name}: "I will hit you in ${nextAttackTimePoint().secondsRemaining()} if you don't run!
                            | If you kill me, you get these items: ${items.joinToString(" ") { item -> item.name }}
                        """.trimMargin()
                        )
                    )
                }
            }
        }
        room.items.filter { it.name == target }
            .forEach { item ->
                when (item) {
                    is Item.Consumable -> {
                        actions.add(player.sendText("$target is consumable."))
                    }
                    is Item.Equipment -> {
                        actions.add(
                            player.sendText(
                                """You can wear $target on your ${item.slot}. 
                                | It will increase your health by ${item.healthModifier.toRelativePercent()}% 
                                | and your damage by ${item.damageModifier.toRelativePercent()}%.
                                """.trimMargin()
                            )
                        )
                    }
                    is Item.Weapon -> {
                        actions.add(
                            player.sendText(
                                "If you wield $target you will deal ${item.damage} damage."
                            )
                        )
                    }
                }
            }

        if (actions.isEmpty()) {
            error("Couldn't find an entity with name $target")
        } else {
            success(actions)
        }.bind()
    }
}


