package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.action.Action
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player

abstract class MasterCommand(name: String) : Command<Player.Master>(name)

class InvitationCommand : Command<Player>("invite") {
    val username: String by argument(help = "The user you want to invite")
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
    val target: String by argument(help = "The user you want to kick")
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class TeleportCommand : Command<Player>("teleport") {
    val target: String by argument(help = "The player you want to teleport")
    val newRoomID: String by argument(help = "The destination you want to teleport the user to")
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class CreateRoomCommand : Command<Player>("createroom") {
    //rooms have to be implemented first to be able to create a command
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class SpawnCommand : Command<Player>("spawn") {
    val targetType: String by argument(help = "The type of the entity to be spawned")
    val targetID : String by argument(help = "The id of the entity to be spawned")
    val roomID : String by argument(help = "The id of the room where you want to spawn to entity in")
    val amount : String by argument(help = "The amount of entities to be spawned by the command")
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class HitTargetCommand : Command<Player>("hit") {
    val targetUser: String by argument(help = "The user you want to damage")
    val hitAmount : String by argument(help = "The amount the user shall be damaged with")
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
