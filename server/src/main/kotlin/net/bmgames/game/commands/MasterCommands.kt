package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.action.Action
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player

abstract class MasterCommand(name: String) : Command<Player.Master>(name)

class InvitationCommand : Command<Player>("invite") {
    val username: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class ListInvitationsCommand : Command<Player>("listinvites") {
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class KickPlayerCommand : Command<Player>("kick") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class TeleportCommand : Command<Player>("teleport") {
    val target: String by argument()
    val newRoomID: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class CreateRoomCommand : Command<Player>("createroom") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class SpawnCommand : Command<Player>("spawn") {
    val targetType: String by argument()
    val roomID : String by argument()
    val amound : String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class HitTargetCommand : Command<Player>("hit") {
    val targetUser: String by argument()
    val hitAmount : String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
