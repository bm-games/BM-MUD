package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class TeleportCommand : MasterCommand("teleport") {
    val target: String by argument(help = "The player you want to teleport")
    val newRoomID: String by argument(help = "The destination you want to teleport the user to")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
