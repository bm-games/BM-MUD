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

class HealCommand : MasterCommand("heal") {
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
                    { player ->
                        val newHealth = max(player.maxHealthPoints, player.healthPoints + amount)
                        listOf(
                            player.sendText(message("game.heal.new-hp", target, newHealth)),
                            HealthAction(player.left(), newHealth - player.healthPoints)
                        )
                    }
                )
            }
        ).bind()
    }

}
