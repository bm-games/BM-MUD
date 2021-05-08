package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class HitTargetCommand : MasterCommand("hit") {
    val targetUser: String by argument(help = "The user you want to damage")
    val hitAmount: String by argument(help = "The amount the user shall be damaged with")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
