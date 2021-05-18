package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.MoveAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither

class TeleportCommand : MasterCommand("teleport") {
    val playerName: String by argument(name = "player", help = message("game.teleport.target"))
    val destination: String by argument(help = message("game.teleport.to"))

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> = either.eager {
        val to = game.getRoom(destination)
            .rightIfNotNull { message("game.teleport.destination-not-found", destination) }.bind()
        game.getOnlineNormal(playerName).toEither(
            { message("game.teleport.target-not-found", playerName) },
            { p ->
                listOf(
                    p.sendText(message("game.teleport.you-were-teleportet", destination)),
                    MoveAction(p, to)
                )
            }
        ).bind()
    }
}
