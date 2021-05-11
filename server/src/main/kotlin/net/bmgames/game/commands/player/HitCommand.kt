package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.*
import net.bmgames.game.action.EntityAction.Type.Create
import net.bmgames.game.action.EntityAction.Type.Remove
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.game.commands.findDifferentPlayerInRoom
import net.bmgames.game.commands.getRoom
import net.bmgames.state.model.Game
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.NPC
import net.bmgames.state.model.Player.Normal
import net.bmgames.state.model.Room
import net.bmgames.success

class HitCommand : PlayerCommand("hit") {

    val target: String by argument(help = "The target you want to hit")

    override fun toAction(player: Normal, game: Game): Either<String, List<Action>> =
        if (!player.canHit()) errorMsg("Cool down a bit!")
        else either.eager {
            val room = player.getRoom(game).bind()

            val actions = mutableListOf<Action>()

            room.npcs[target]?.let { player.hitNPC(it, room) }?.let(actions::addAll)
            player.findDifferentPlayerInRoom(game, target)
                ?.let { player.hitPlayer(it, game, room) }
                ?.let(actions::addAll)

            if (actions.isEmpty()) {
                errorMsg("Couldn't find an entity with name $target")
            } else {
                success(actions)
            }.bind()
        }


    private fun Normal.hitPlayer(player: Normal, game: Game, room: Room): List<Action> {
        val newHealth = player.healthPoints - damage
        return if (newHealth <= 0) {
            listOf(
                sendText(
                    """You have slain ${player.ingameName}. 
                    |${player.ingameName}'s items have dropped on the floor."""
                        .trimMargin()
                ),
                player.sendText(
                    """You have been slain by $ingameName. 
                    |Your items have dropped and you're back at the start."""
                        .trimMargin()
                ),
                MoveAction(player.left(), room, game.getStartRoom()),
                InventoryAction(player, Inventory(null, emptyMap(), game.startItems))
            ) + player.inventory.allItems().map { EntityAction(Create, room, it.right()) }
        } else {
            listOf(
                sendText("${player.ingameName} has ${newHealth.toInt()} HP left."),
                player.sendText("You have been hit by $ingameName. You have ${newHealth.toInt()} left."),
                HealthAction(player.left(), -damage.toInt())
            )
        }
    }

    private fun Normal.hitNPC(npc: NPC, room: Room): List<Action> =
        if (npc is NPC.Hostile) {
            val newHealth = npc.health - damage
            if (newHealth <= 0) {
                listOf(
                    sendText("You have slain ${npc.name}. ${npc.name}'s items have dropped on the floor."),
                    EntityAction(Remove, room, npc.left())
                ) + npc.items.map { EntityAction(Create, room, it.right()) }
            } else {
                listOf(
                    sendText("${npc.name} has ${newHealth.toInt()} HP left."),
                    HealthAction(npc.right(), -damage.toInt())
                )
            }
        } else {
            emptyList()
        }


}

