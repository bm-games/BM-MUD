@file:OptIn(ExperimentalCoroutinesApi::class)

package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.bmgames.ErrorMessage
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player
import net.bmgames.success
import net.bmgames.error
import net.bmgames.game.GameScope
import net.bmgames.game.message.Message

/**
 * Central component which runs a MUD
 * @property onlinePlayersRef Stores all connections in a thread safe manner.
 * Every connection is indexed by the unique ingame name of the player
 *
 * */
class GameRunner internal constructor(initialGame: Game) {
    private val currentGameState: Atomic<Game> = Atomic.unsafe(initialGame)
    private val commandParser = CommandParser(initialGame.config.commandConfig)
    private val commandQueue = Channel<Pair<String, Command<*>>>()

    private val onlinePlayersRef: Atomic<Map<String, Connection>> = Atomic.unsafe(emptyMap())
//    private suspend fun getConnection(name: String) = onlinePlayersRef.get()[name]

    suspend fun getCurrentGameState() = currentGameState.get()

    /**
     * Connects a player to this game
     * @return
     * */
    internal suspend fun connect(
        player: Player
    ): Either<ErrorMessage, IConnection> = either {
        if (!getCurrentGameState().users.containsKey(player.user.username)) {
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

    suspend fun gameLoop(): Unit {
//        TODO()
    }

}

