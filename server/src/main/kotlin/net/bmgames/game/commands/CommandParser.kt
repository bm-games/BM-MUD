package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.core.PrintHelpMessage
import net.bmgames.ErrorMessage
import net.bmgames.state.model.CommandConfig
import net.bmgames.errorMsg
import net.bmgames.state.model.Player
import net.bmgames.success

/**
 * Registry of all player commands.
 * Key: Default command name
 * Value: Command constructor
 * */
val playerCommands: Map<String, () -> Command<Player.Normal>> = mapOf(
    "move" to ::MoveCommand,
    "say" to ::SayCommand,
    "whisper" to ::WhisperCommand,
    "hit" to ::HitCommand,
    "look" to ::LookCommand,
    "inspect" to ::InspectCommand,
    "inventory" to ::InventoryCommand,
    "pickup" to ::PickupCommand,
    "drop" to ::DropItemCommand,
    "use" to ::UseItemCommand
)

val masterCommands: Map<String, () -> Command<Player.Master>> = mapOf(
    "say" to ::MasterSayCommand,
    "whisper" to ::MasterWhisperCommand,
    "invite" to ::InvitationCommand,
    "listinvites" to ::ListInvitationsCommand,
    "kick" to ::KickPlayerCommand,
    "teleport" to ::TeleportCommand,
    "createroom" to ::CreateRoomCommand,
    "spawn" to ::SpawnCommand,
    "hit" to ::HitTargetCommand
)

/**
 * Parses player and master commands, depending on a dungeon configuration
 * @property commandConfig The Command config of the associated dungeon
 * */
class CommandParser(val commandConfig: CommandConfig) {

    fun parseMasterCommand(commandLine: String) = parse(commandLine, masterCommands)
    fun parsePlayerCommand(commandLine: String) = parse(commandLine, playerCommands)

    private fun parse(commandLine: String, commands: Map<String, () -> Command<*>>): Either<ErrorMessage, Command<*>> {
        val args = commandLine.trim().split(Regex("[ \t]+"))
        val commandName = args.getOrNull(0) //TODO replace aliases

        if (commandName == null) {
            return errorMsg("Empty command")
        }

        val commandConstructor = commands[commandName]
            ?: return errorMsg("Command not found") //TODO send available commands

        val command = commandConstructor()
        return try {
            command.parse(args.subList(1, args.size))
            success(command)
        } catch (_: PrintHelpMessage) {
            errorMsg(command.getFormattedHelp())
        } catch (_: Exception) {
            errorMsg(command.getFormattedUsage())
        }
    }

}


