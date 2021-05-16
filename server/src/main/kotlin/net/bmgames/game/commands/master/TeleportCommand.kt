package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class TeleportCommand : MasterCommand("teleport") {
    val target: String by argument(help = message("game.teleport-user"))
    val newRoomID: String by argument(help = message("game.teleport.position"))
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
