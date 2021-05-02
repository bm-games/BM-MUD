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





