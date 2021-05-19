package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.int
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.getPlayerOrNPC
import net.bmgames.game.commands.player.hitNPC
import net.bmgames.game.commands.player.hitPlayer
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither

/**
 * A mastercommand which hits a normal player or npc. The params are given trough arguments -> "by argument"
 *
 * @property target The name of the NPC or Player
 * @property room Name of the room
 * @property amount Damage amount
 * @property hitter The name which is displayed to the player who got hit
 *
 * @constructor creates a complete master say command.
 */
class MasterHitCommand : MasterCommand("hit", message("game.master-hit-epilog")) {
    val target: String by argument(help = message("game.hit.target"))
    val room: String by argument(help = message("game.hit.room"))
    val amount: Int by argument(help = message("game.damage-amount")).int()
    val hitter: String? by argument(help = message("game.hit.hitter")).optional()


    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It hits the specified target for the specified amount
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> = either.eager {
        val room = game.getRoom(room).rightIfNotNull { message("game.hit.room-not-found", room) }.bind()
        game.getPlayerOrNPC(target, room).toEither(
            { message("game.entity-not-found", target) },
            {
                it.fold(
                    { npc -> player.hitNPC(npc, room, amount) },
                    { target ->
                        player.hitPlayer(target, game, room, amount, hitter ?: player.ingameName)
                    }
                )
            }
        ).bind()
    }


}
