package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.ErrorMessage
import net.bmgames.action.Action
import net.bmgames.action.HealthAction
import net.bmgames.action.MessageAction
import net.bmgames.configurator.CommandConfig
import net.bmgames.error
import net.bmgames.game.message.Message
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player
import net.bmgames.success

/**
 * Registry of all player commands.
 * Key: Default command name
 * Value: Command constructor
 * */
val playerCommands: Map<String, () -> Command<Player.Normal>> = mapOf(
    "move" to ::MoveCommand,
    "say" to ::SayCommand,
    "whisper" to :: WhisperCommand,
    "hit" to ::HitCommand,
    "look" to ::LookCommand,
    "inspect" to ::InspectCommand,
    "inventory" to ::InventoryCommand,
    "pickup" to ::PickupCommand,
    "drop" to ::DropItemCommand,
    "use" to ::UseItemCommand
)

val masterCommands: Map<String, () -> Command<Player.Master>> = mapOf(
    "say" to ::SayCommand,
    "whisper" to ::WhisperCommand,
    "invite" to ::InvitationCommand,
    "listinvites" to ::ListInvitationsCommand,
    "kick" to ::KickPlayerCommand,
    "teleport" to ::TeleportCommand,
    "createroom" to ::CreateRoomCommand,
    "spawn" to ::SpawnCommand,
    "hit" to ::HitTargetCommand
)

class CommandParser(
    val commandConfig: CommandConfig
) {
    fun parse(player: Player, commandLine: String): Either<ErrorMessage, Command<*>> {
        val args = commandLine.trim().split(Regex("[ \t]+"))
        val commandName = args.getOrNull(0) //TODO replace aliases

        if (commandName == null) {
            return error("Empty command")
        }
        val availableCommands = when (player) {
            is Player.Master -> masterCommands
            is Player.Normal -> playerCommands
        }
        val commandConstructor = availableCommands[commandName]
            ?: return error("Command not found") //TODO send available commands

        val command = commandConstructor()
        return try {
            command.parse(args.subList(1, args.size))
            success(command)
        } catch (_: PrintHelpMessage) { //TODO maybe catch other exceptions -> https://ajalt.github.io/clikt/exceptions/#which-exceptions-exist
            error(command.getFormattedUsage())
        } catch (_: Exception) {
            error(command.getFormattedUsage())
        }
    }
}


abstract class PlayerCommand(name: String) : Command<Player.Normal>(name)

/**
 * @see [https://ajalt.github.io/clikt/]
 * */
class MoveCommand : PlayerCommand("move") {

    enum class Direction { NORTH, EAST, SOUTH, WEST }
    val direction: Direction by argument(help = "The direction you want to move to").enum()

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Rooms not yet implemented")
        /*val playerRoom : Room
        when(direction){
            Direction.NORTH -> game. Logik zur Berechnung der Räume
            Direction.EAST -> game. Logik zur Berechnung der Räume
            Direction.WEST -> game. Logik zur Berechnung der Räume
            Direction.SOUTH -> game. Logik zur Berechnung der Räume*/
        }
    }




class SayCommand : Command<Player>("say") {

    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player, game: Game): Either<String, List<Action>> =
        try {
            Either.Right(listOf<Action>(MessageAction(player, Message.Text(message))))
        } catch (e: Error) {
            Either.Left("Couldn't use say function")
        }
}

class WhisperCommand : Command<Player>("whisper") {
   val target: String by argument()
    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class HitCommand : Command<Player>("hit") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Required functions not yet implemented")
        val returnList = mutableListOf<Action>()
        val targetPlayer : Player.Normal? = getPlayerByName(target)
        if(targetPlayer != null){
            returnList.add(HealthAction(targetPlayer, player.damage))
            returnList.add(MessageAction(targetPlayer, Message.Text("U got hit for ${player.damage} by ${player.ingameName}")))
            returnList.add(MessageAction(player, Message.Text("U hit ${targetPlayer.ingameName} for ${player.damage}")))
            return Either.Right(returnList)
        }
        return Either.Left("Either the player with this name is offline or doesn't exist")

    }
}

class LookCommand : Command<Player>("look") {
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InspectCommand : Command<Player>("inspect") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class InventoryCommand : Command<Player>("inventory") {
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class PickupCommand : Command<Player>("pickup") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class DropItemCommand : Command<Player>("drop") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

class UseItemCommand : Command<Player>("use") {
    val target: String by argument()
    override fun toAction(player: Player, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}

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


