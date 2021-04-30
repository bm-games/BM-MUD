package net.bmgames.game.commands

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.transformAll
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.ErrorMessage
import net.bmgames.configurator.CommandConfig
import net.bmgames.error
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
    "say" to ::SayCommand
)

val masterCommands: Map<String, () -> Command<Player.Master>> = mapOf(
    "say" to ::SayCommand
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

    val direction: Direction by argument(help = "The direction you want to move to").enum()

    override fun toAction(player: Player.Normal, game: Game): Either<String, List<String>> {
        TODO("Not yet implemented")
    }

    enum class Direction { NORTH, EAST, SOUTH, WEST }

}

class SayCommand : Command<Player>("say") {

    val message: String by argument()
        .multiple(true)
        .transformAll { it.joinToString(" ") }

    override fun toAction(player: Player, game: Game): Either<String, List<String>> {
        TODO("Not yet implemented")
    }

}
