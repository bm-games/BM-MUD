package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.bmgames.*
import net.bmgames.game.GameScope
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.message.Message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.onlinePlayers
import java.util.logging.Logger

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
    val name: String = initialGame.name

    /**
     * Connects a player to this game
     * @param player The player which should be connected
     * @return Either an [ErrorMessage], if the connection fails, else an [IConnection]
     * */
    internal suspend fun connect(player: Player): Either<ErrorMessage, IConnection> = either {
        val allowedUsers = getCurrentGameState().allowedUsers

        guard(!allowedUsers.containsKey(player.user.username)) {
            message("game.not-invited")
        }

        val connection = onlinePlayersRef.modify { connections ->
            if (connections.containsKey(player.ingameName)) {
                connections to errorMsg(message("game.player-already-connected").format(player.ingameName))             } else {
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
            connection.outgoingChannel.send(Message.Text(message("game.welcome")))
            launch {
                for (command in connection.incoming) {
                    commandQueue.send(player.ingameName to command)
                }
            }
            connection.onClose {
                launch { onlinePlayersRef.update { it - player.ingameName } }
            }
        }

        return@either connection
    }

    /**
     * Starts the game loop in the [GameScope] context
     * */
    internal suspend fun gameLoop(): Unit {
//        TODO()
    }

    /**
     * Modifies the current game state.
     * @param f A function which updates the game
     * */
    suspend fun updateGameState(f: (Game) -> Game) {
        currentGameState.update(f)
    }

    /**
     * Kicks every player and stops the game loop.
     * @return The last game state
     * */
    internal suspend fun stop(): Game {
        this.onlinePlayersRef.getAndSet(emptyMap())
            .forEach { (_, connection) -> connection.close(message("game.game-stopped")) }
        updateGameState(Game.onlinePlayers.set(emptyMap()))

        return getCurrentGameState()
    }

}

