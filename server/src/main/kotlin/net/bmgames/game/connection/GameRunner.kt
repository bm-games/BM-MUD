package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.andThen
import arrow.core.computations.either
import arrow.core.identity
import arrow.fx.coroutines.Atomic
import arrow.optics.dsl.at
import arrow.optics.typeclasses.At
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.bmgames.*
import net.bmgames.communication.Notifier
import net.bmgames.game.GameScope
import net.bmgames.game.action.Action
import net.bmgames.game.action.Effect
import net.bmgames.game.action.Update
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.message.Message
import net.bmgames.state.GameRepository
import net.bmgames.state.PlayerRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Master
import net.bmgames.state.model.Player.Normal
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
class GameRunner internal constructor(initialGame: Game, val notifier: Notifier) {
    private val currentGameState: Atomic<Game> = Atomic.unsafe(initialGame)
    internal val commandParser = CommandParser(initialGame.commandConfig)
    internal val commandQueue = Channel<Pair<String, Command<Player>>>()

    private val onlinePlayersRef: Atomic<Map<String, Connection>> = Atomic.unsafe(emptyMap())

    /**
     * @return The current game state
     * */
    suspend fun getCurrentGameState() = currentGameState.get()
    val name: String = initialGame.name

    /**
     * Connects a player to this game runner. It also updates the game state accordingly.
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
                connections to errorMsg(message("game.player-already-connected", player.ingameName))
            } else {
                val parseCommand = when (player) {
                    is Master -> commandParser::parseMasterCommand
                    is Normal -> commandParser::parsePlayerCommand
                }
                val connection = Connection(parseCommand)
                val newConnections = connections.plus(player.ingameName to connection)
                newConnections to success(connection)
            }
        }.bind()

        updateGameState(Game.onlinePlayers.modify { it.plus(player.ingameName to player) })
        //TODO broadcast that player joined


        GameScope.launch {
            println("$name: ${player.ingameName} connected.")
            connection.outgoingChannel.send(Message.Text(message("game.welcome")))
            launch {
                for (command in connection.incoming) {
                    commandQueue.send(player.ingameName to command)
                }
            }
            connection.onClose {
                launch {
                    println("$name: ${player.ingameName} disconnected.")
                    //TODO broadcast that player left
                    onlinePlayersRef.update { it - player.ingameName }
                    val playerState = currentGameState.modify {
                        val playerState = it.onlinePlayers[player.ingameName]
                        if (playerState != null)
                            it.copy(onlinePlayers = it.onlinePlayers - playerState.ingameName) to playerState
                        else it to null
                    }
                    if (playerState is Normal) {
                        PlayerRepository.savePlayer(getCurrentGameState(), playerState)
                    }
                }
            }
        }

        return@either connection
    }

    /**
     * Starts the game loop in the [GameScope] context
     * */
    internal suspend fun gameLoop(): Unit {
        GameScope.launch {
            for ((playerName, command) in commandQueue) {
                val effects: List<Effect> = currentGameState.modify { game ->
                    val player = game.getPlayer(playerName)
                    if (player != null) {
                        val actions = command.toAction(player, game).fold({ player.sendText(it).toList() }, ::identity)
                        val newGameState = actions.filterIsInstance<Update>()
                            .fold(game) { intermediateState, update -> update.update(intermediateState) }
                        newGameState to actions.filterIsInstance<Effect>()
                    } else {
                        game to emptyList()
                    }
                }
                effects.forEach { it.run(this@GameRunner) }
            }
        }
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

    internal suspend fun getConnection(playerName: String): Connection? {
        return onlinePlayersRef.get()[playerName]
    }

}

