package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class SpawnCommand : MasterCommand("spawn") {
    val targetType: String by argument(help = message("game.entity-type"))
    val targetID: String by argument(help = message("game.entity-id"))
    val roomID: String by argument(help = message("game.room-id"))
    val amount: String by argument(help = message("game.entity-amount"))
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
