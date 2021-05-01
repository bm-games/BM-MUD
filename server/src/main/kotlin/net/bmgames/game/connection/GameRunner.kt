@file:OptIn(ExperimentalCoroutinesApi::class)

package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import net.bmgames.ErrorMessage
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player
import net.bmgames.success

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

        for (command in connection.incoming) {
            commandQueue.send(player.ingameName to command)
        }

        connection.outgoingChannel.invokeOnClose {
            runBlocking { onlinePlayersRef.update { it - player.ingameName } }
        }

        return@either connection
    }

    suspend fun gameLoop(): Unit {
        TODO()
    }

    companion object {
        internal suspend inline fun Game.start(): GameRunner =
            GameRunner(this).apply { gameLoop() }
    }
}

