package net.bmgames.game.connection

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.identity
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.bmgames.*
import net.bmgames.authentication.User
import net.bmgames.communication.Notifier
import net.bmgames.game.GameScope
import net.bmgames.game.action.*
import net.bmgames.game.commands.BatchCommand
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.CommandParser
import net.bmgames.game.commands.getOtherPlayersInRoom
import net.bmgames.game.message.Message
import net.bmgames.game.message.Message.Text
import net.bmgames.state.PlayerRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Player.Master
import net.bmgames.state.model.Player.Normal
import net.bmgames.state.model.onlinePlayers

/**
 * Master for every game, so that Items can use Mastercommands, even when the real Master is offline
 * */
val SYSTEM = Master(User("system@bm-games.net", "________________________________System", "", ""))

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
     * Updates the current game state.
     * @param f A function which updates the game
     * */
    suspend fun updateGameState(f: (Game) -> Game): Unit {
        currentGameState.update(f)
    }

    /**
     * Modifies the current game state and returns something out of the old state.
     * @param f A function which updates the game
     * */
    suspend fun <T> modifyGameState(f: (Game) -> Pair<Game, T>): T {
        return currentGameState.modify(f)
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
            runBlocking {
                connections[player.ingameName]
                    ?.close(message("game.player-already-connected", player.ingameName))
            }

            val parseCommand = when (player) {
                is Master -> commandParser::parseMasterCommand
                is Normal -> commandParser::parsePlayerCommand
            }
            val connection = Connection(parseCommand)
            val newConnections = connections.plus(player.ingameName to connection)
            newConnections to success(connection)

        }.bind()

        updateGameState(Game.onlinePlayers.modify { it.plus(player.ingameName to player) })

        GameScope.launch {
            launch {
                for (command in connection.incoming) {
                    if (command is BatchCommand)
                        commandQueue.send(SYSTEM.ingameName to command)
                    else commandQueue.send(player.ingameName to command)
                }
            }
            connection.onClose {
                launch {
                    onlinePlayersRef.update { it - player.ingameName }
                    val playerState = currentGameState.modify {
                        val playerState = it.onlinePlayers[player.ingameName]
                        if (playerState != null)
                            it.copy(onlinePlayers = it.onlinePlayers - playerState.ingameName) to playerState
                        else it to null
                    }
                        ?.also { byePlayer(it) }

                    if (playerState is Normal) {
                        PlayerRepository.savePlayer(getCurrentGameState(), playerState)
                    }
                }
            }
            greetPlayer(player, connection)
        }

        return@either connection
    }

    internal suspend fun greetPlayer(player: Player, connection: Connection) = coroutineScope {
        val game = getCurrentGameState()
        println("$name: ${player.ingameName} connected.")
        connection.outgoingChannel.send(Text(message("game.welcome", game.name)))
        connection.outgoingChannel.send(Message.Map(game, player))
        launch {
            connection.tryQueueCommand("look")
        }
        if (player is Normal) {
            player.getOtherPlayersInRoom(game)
                .plus(game.master.ingameName to game.master)
                .map { (_, p) ->
                    listOf(
                        p.sendMap(game),
                        p.sendText(message("game.player-joined", player.ingameName))
                    )
                }
        } else {
            game.onlinePlayers.minus(player.ingameName)
                .map { (_, p) ->
                    listOf(
                        p.sendMap(game),
                        p.sendText(message("game.master-joined", player.ingameName))
                    )
                }
        }.forEach { ti -> ti.forEach { it.run(this@GameRunner) } }

    }


    internal suspend fun byePlayer(player: Player) = coroutineScope {
        val game = getCurrentGameState()
        println("$name: ${player.ingameName} disconnected.")
        if (player is Normal) {
            player.getOtherPlayersInRoom(game)
                .plus(game.master.ingameName to game.master)
                .map { (_, p) ->
                    listOf(
                        p.sendMap(game),
                        p.sendText(message("game.player-left", player.ingameName))
                    )
                }
        } else {
            game.onlinePlayers.minus(player.ingameName)
                .map { (_, p) ->
                    listOf(
                        p.sendMap(game),
                        p.sendText(message("game.master-left", player.ingameName))
                    )
                }
        }.forEach { ti -> ti.forEach { it.run(this@GameRunner) } }

    }


    /**
     * Starts the game loop in the [GameScope] context
     * */
    internal suspend fun gameLoop(): Unit {
        GameScope.launch {
            for ((playerName, command) in commandQueue) {
                val effects: List<Effect> = currentGameState.modify { game ->
                    val player = if (playerName == SYSTEM.ingameName) SYSTEM else game.getPlayer(playerName)
                    if (player != null) {
                        try {
                            val actions =
                                command.toAction(player, game).fold({ player.sendText(it).toList() }, ::identity)
                            val updates = actions.filterIsInstance<Update>()
                            val newGameState = updates
                                .fold(game) { intermediateState, update -> update.update(intermediateState) }

                            val effects = actions.filterIsInstanceTo(mutableListOf<Effect>())
                            if (updates.isNotEmpty()) {
                                with(newGameState) {
                                    if (player is Normal) {
                                        player.getOtherPlayersInRoom(this)
                                            .plus(master.ingameName to master)
                                            .forEach { (_, p) -> effects.add(p.sendMap(this)) }
                                    } else {
                                        onlinePlayers.forEach { (_, p) -> effects.add(p.sendMap(this)) }
                                    }
                                }
                            }
                            newGameState to effects
                        } catch (e: Exception) {
                            e.printStackTrace()
                            game to emptyList()
                        }
                    } else {
                        game to emptyList()
                    }
                }
                effects.forEach {
                    try {
                        it.run(this@GameRunner)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}

