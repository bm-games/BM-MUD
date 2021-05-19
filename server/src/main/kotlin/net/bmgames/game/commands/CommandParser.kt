package net.bmgames.game.commands

import arrow.core.Either
import arrow.core.traverseEither
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.output.HelpFormatter
import com.github.ajalt.clikt.output.Localization
import net.bmgames.ErrorMessage
import net.bmgames.errorMsg
import net.bmgames.game.commands.master.*
import net.bmgames.game.commands.player.*
import net.bmgames.message
import net.bmgames.state.model.CommandConfig
import net.bmgames.state.model.Player
import net.bmgames.success
import java.net.URLDecoder

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
    "use" to ::UseItemCommand,
    "stats" to ::StatsCommand
)

val ALL_MASTER_COMMANDS: CommandMap<Player.Master> = mapOf(
    "say" to ::MasterSayCommand,
    "whisper" to ::MasterWhisperCommand,
    "invite" to ::InvitationCommand,
    "kick" to ::KickCommand,
    "hit" to ::MasterHitCommand,
    "heal" to ::HealCommand,
    "give" to ::GiveCommand,
    "spawn" to ::SpawnCommand,
    "teleport" to ::TeleportCommand,
    "createroom" to ::CreateRoomCommand,
    "deleteroom" to ::DeleteRoomCommand,
)

/**
 * Parses player and master commands, depending on a dungeon configuration
 * @property commandConfig The Command config of the associated dungeon
 * */
class CommandParser(val commandConfig: CommandConfig) {

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
        val commandName = args.getOrNull(0) ?: return errorMsg(message("game.empty-command"))

        val commandConstructor = commands[commandName]
        if (commandConstructor == null) {
            return commandConfig.customCommands[commandName]
                ?.split("\n")
                ?.map { it.trim() }
                ?.traverseEither { parse(it, commands) }
                ?.map(::BatchCommand)
                ?: errorMsg(
                    message(
                        "game.command-not-found",
                        commands.keys.minus("say").minus("whisper").joinToString("\n")
                    )
                )
        } else {
            val command = commandConstructor()
            return try {
                command.parse(args.subList(1, args.size).map { URLDecoder.decode(it, Charsets.UTF_8) })
                success(command)
            } catch (_: PrintHelpMessage) {
                errorMsg(command.getFormattedHelp())
            } catch (_: Exception) {
                errorMsg(command.getFormattedUsage())
            }
        }
    }

}


