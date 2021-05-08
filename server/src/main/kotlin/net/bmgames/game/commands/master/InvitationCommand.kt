package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class InvitationCommand : MasterCommand("invite") {

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InvitationAddCommand : MasterCommand("add") {
    val username: String by argument(help = "The user you want to invite")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InvitationAcceptCommand : MasterCommand("accept") {
    val username: String by argument(help = "The user you want to invite")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InvitationRejectCommand : MasterCommand("reject") {
    val username: String by argument(help = "The user you want to invite")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InvitationListCommand : MasterCommand("list") {
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
