package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.bmgames.ErrorMessage
import net.bmgames.error
import net.bmgames.game.GameScope
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.message.Message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.success

/**
 * Central component which runs a concrete MUD
 * @property currentGameState The current gamestate as an atomic value
 * @property commandParser The command parser depending on the game configuration
 * @property commandQueue The central queue where every parsed command with its player is enqueued
 * @property onlinePlayersRef Stores all connections in a thread safe manner.
 *  Every connection is indexed by the unique ingame name of the player
 *
 * */
class GameRunner internal constructor(initialGame: Game) {
    private val currentGameState: Atomic<Game> = Atomic.unsafe(initialGame)
    private val commandParser = CommandParser(initialGame.commandConfig)
    private val commandQueue = Channel<Pair<String, Command<*>>>()

    private val onlinePlayersRef: Atomic<Map<String, Connection>> = Atomic.unsafe(emptyMap())

    /**
     * @return The current game state
     * */
    suspend fun getCurrentGameState() = currentGameState.get()

    /**
     * Connects a player to this game
     * @param player The player which should be connected
     * @return Either an [ErrorMessage], if the connection fails, else an [IConnection]
     * */
    internal suspend fun connect(player: Player): Either<ErrorMessage, IConnection> = either {
        if (!getCurrentGameState().allowedUsers.containsKey(player.user.username)) {
            error("You are not invited to this game").bind()
        }

        val connection = onlinePlayersRef.modify { connections ->
            if (connections.containsKey(player.ingameName)) {
                connections to error("Player ${player.ingameName} already connected")
            } else {
                val parseCommand = when (player) {
                    is Player.Master -> commandParser::parseMasterCommand
                    is Player.Normal -> commandParser::parsePlayerCommand
                }
                val connection = Connection(parseCommand)
                val newConnections = connections.plus(player.ingameName to connection)
                newConnections to success(connection)
            }
        }.bind()

        GameScope.launch {
            connection.outgoingChannel.send(Message.Text("Welcome!"))
            launch {
                for (command in connection.incoming) {
                    commandQueue.send(player.ingameName to command)
                }
            }
            launch {
                connection.outgoingChannel.invokeOnClose {
                    launch { onlinePlayersRef.update { it - player.ingameName } }
                }
            }
        }

        return@either connection
    }

    /**
     * Starts the game loop in the [GameScope] context
     * */
    suspend fun gameLoop(): Unit {
//        TODO()
    }

}

