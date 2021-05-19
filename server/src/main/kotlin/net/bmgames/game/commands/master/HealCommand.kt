package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import net.bmgames.game.action.Action
import net.bmgames.game.action.HealthAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.getPlayerOrNPC
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import kotlin.math.max

/**
 * A mastercommand which heals a player or NPC.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the name of the target.
 * @param room the room where the target is located.
 * @param amount how many Healthpoints are replenished.
 *
 * @@constructor creates a complete heal command
 */
class HealCommand : MasterCommand("heal", message("game.heal-epilog")) {
    val target: String by argument(help = message("game.hit.target"))
    val room: String by argument(help = message("game.hit.room"))
    val amount: Int by argument(help = message("game.heal.amount")).int()

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> = either.eager {
        val room = game.getRoom(room).rightIfNotNull { message("game.hit.room-not-found", room) }.bind()
        game.getPlayerOrNPC(target, room).toEither(
            { message("game.entity-not-found", target) },
            {
                it.fold(
                    { npc ->
                        listOf(
                            player.sendText(message("game.heal.new-hp", target, npc.health + amount)),
                            HealthAction((room to npc).right(), amount)
                        )
                    },
                    { p ->
                        val newHealth = max(p.maxHealthPoints, p.healthPoints + amount)
                        listOf(
                            player.sendText(message("game.heal.new-hp", target, newHealth)),
                            HealthAction(p.left(), newHealth - p.healthPoints)
                        )
                    }
                )
            }
        ).bind()
    }

}
