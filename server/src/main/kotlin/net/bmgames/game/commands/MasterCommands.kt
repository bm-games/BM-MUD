package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import net.bmgames.game.action.Action
import net.bmgames.game.action.MessageAction
import net.bmgames.game.message.Message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player


class InvitationCommand : MasterCommand("invite") {
    val username: String by argument(help = "The user you want to invite")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class ListInvitationsCommand : MasterCommand("listinvites") {
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class KickPlayerCommand : MasterCommand("kick") {
    val target: String by argument(help = "The user you want to kick")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class TeleportCommand : MasterCommand("teleport") {
    val target: String by argument(help = "The player you want to teleport")
    val newRoomID: String by argument(help = "The destination you want to teleport the user to")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class CreateRoomCommand : MasterCommand("createroom") {
    //rooms have to be implemented first to be able to create a command
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class SpawnCommand : MasterCommand("spawn") {
    val targetType: String by argument(help = "The type of the entity to be spawned")
    val targetID: String by argument(help = "The id of the entity to be spawned")
    val roomID: String by argument(help = "The id of the room where you want to spawn to entity in")
    val amount: String by argument(help = "The amount of entities to be spawned by the command")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class HitTargetCommand : MasterCommand("hit") {
    val targetUser: String by argument(help = "The user you want to damage")
    val hitAmount: String by argument(help = "The amount the user shall be damaged with")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}


class MasterSayCommand : MasterCommand("say") {

    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        try {
            Either.Right(listOf<Action>(MessageAction(player, Message.Text(message))))
        } catch (e: Error) {
            Either.Left("Couldn't use say function")
        }
}

class MasterWhisperCommand : MasterCommand("whisper") {
    val target: String by argument(help = "The player you want to whisper to")
    val message: String by argument(help = "The message you want to send")
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
