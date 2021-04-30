package net.bmgames.game.connection

import arrow.core.Either
import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import net.bmgames.game.commands.Command
import net.bmgames.game.commands.IssuedCommand
import net.bmgames.game.message.Message
import net.bmgames.game.state.Game

/**
 * Central component which runs a MUD
 * */
class GameRunner(initialGame: Game) {
    /**
     * Stores all connections in a thread safe manner.
     * Every connection is indexed by the unique ingame name of the player
     */
    private val connections: Atomic<Map<String, SendChannel<Message>>> = Atomic.unsafe(emptyMap())
    private val currentGameState: Atomic<Game> = Atomic.unsafe(initialGame)
    private val commandHandler = GameCommandHandler()
    private val commandQueue = Channel<Command<*>>()

    suspend fun getCurrentGameState() = currentGameState.get()

//    internal suspend fun queueCommand(command: IssuedCommand<*>) = commandQueue.send(command)

    internal suspend fun connect(playerName: String, channel: SendChannel<Message>): CommandHandler {
        connections.update { it.plus(playerName to channel) }
        return commandHandler
    }


    private class GameCommandHandler : CommandHandler {
        override suspend fun tryQueueCommand(playerName: String, command: String): Either<String, Unit> {
            TODO("Parse command and queue if successful")
        }
    }
}

fun Game.start() = GameRunner(this)
