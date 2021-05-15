package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import net.bmgames.ErrorMessage
import net.bmgames.errorMsg
import net.bmgames.game.commands.master.*
import net.bmgames.game.commands.player.*
import net.bmgames.message
import net.bmgames.state.model.CommandConfig
import net.bmgames.state.model.Player
import net.bmgames.success

typealias CommandMap<P> = Map<String, () -> Command<P>>

/**
 * Registry of all player commands.
 * Key: Default command name
 * Value: Command constructor
 * */
val ALL_PLAYER_COMMANDS: CommandMap<Player.Normal> = mapOf(
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

val ALL_MASTER_COMMANDS: CommandMap<Player.Master> = mapOf(
    "say" to ::MasterSayCommand,
    "whisper" to ::MasterWhisperCommand,
    "invite" to {
        InvitationCommand().subcommands(
            InvitationAddCommand(),
            InvitationAcceptCommand(),
            InvitationRejectCommand(),
            InvitationListCommand()
        )
    },
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
class CommandParser(commandConfig: CommandConfig) {

    private val masterCommands: CommandMap<Player.Master> = ALL_MASTER_COMMANDS
        .mapKeys { (original, _) ->
            commandConfig.aliases[original] ?: original
        }
    private val playerCommands: CommandMap<Player.Normal> = ALL_PLAYER_COMMANDS
        .mapKeys { (original, _) ->
            commandConfig.aliases[original] ?: original
        }

    @Suppress("UNCHECKED_CAST")
    fun parseMasterCommand(commandLine: String) = parse(commandLine, masterCommands).map { it as Command<Player> }

    @Suppress("UNCHECKED_CAST")
    fun parsePlayerCommand(commandLine: String) = parse(commandLine, playerCommands).map { it as Command<Player> }

    private fun <P : Player> parse(
        commandLine: String,
        commands: Map<String, () -> Command<P>>
    ): Either<ErrorMessage, Command<P>> {

        val args = commandLine.trim().split(Regex("[ \t]+"))
        val commandName = args.getOrNull(0)

        if (commandName == null) {
            return errorMsg(message("game.empty-command"))
        }

        val commandConstructor = commands[commandName]
            ?: return errorMsg(message("game.command-not-found").format(commands.keys.joinToString("\n")))

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


