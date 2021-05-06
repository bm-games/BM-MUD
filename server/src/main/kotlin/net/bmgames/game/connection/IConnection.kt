package net.bmgames.game.connection

import arrow.core.Either
import kotlinx.coroutines.channels.ReceiveChannel
import net.bmgames.game.message.Message

/**
 * The connection to a MUD player.
 * @property outgoing Queue for sending messages to the client
 * */
interface IConnection {
    val outgoing: ReceiveChannel<Message>

    /**
     * Tries to parse and enque the command
     * @param commandLine The command line from the player
     * @return Either an error message or nothing, if it succeeds
     * */
    suspend fun tryQueueCommand(commandLine: String): Either<String, Unit>


    /**
     * Closes this channel
     * */
    suspend fun close()
}
