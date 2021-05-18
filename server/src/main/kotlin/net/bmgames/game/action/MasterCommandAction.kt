package net.bmgames.game.action


import arrow.core.traverseEither
import kotlinx.coroutines.launch
import net.bmgames.game.GameScope
import net.bmgames.game.commands.BatchCommand
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.connection.GameRunner
import net.bmgames.message
import net.bmgames.state.model.Player
import java.time.Duration
import kotlinx.coroutines.time.delay as delayFor
/**
 * An action which executes a string of multiple commands.
 *
 *
 * @param command the string of commands which are to be executed
 * @param delay a delay which is waited for, to execute the given commands/actions.
 *
 * @constructor creates a complete MasterCommandAction.
 */
data class MasterCommandAction constructor(val command: String, val delay: Duration? = null) : Effect() {
    /**
     * Runs the MasterCommandAction. Splits the string of the command field and executes the commands extracted from it,
     * one after another.
     *
     * @param gameRunner the gameRunner the actions are performed with.
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun run(gameRunner: GameRunner) {
        GameScope.launch {
            if(delay != null) delayFor(delay)
            command.split("\n").filterNot { it.isEmpty() }
                .traverseEither { gameRunner.commandParser.parseMasterCommand(it) }
                .fold(
                    { error -> println(message("game.actions.master-failed", command.replace("\n", ", "), error)) },
                    { commands ->
                        gameRunner.commandQueue.send(
                            Pair(
                                gameRunner.getCurrentGameState().master.ingameName,
                                BatchCommand(commands as List<MasterCommand>) as Command<Player>
                            )
                        )
                    }
                )
        }
    }
}
