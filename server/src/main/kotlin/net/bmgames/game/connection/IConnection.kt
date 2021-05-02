package net.bmgames.game.connection

import arrow.core.Either
import kotlinx.coroutines.channels.ReceiveChannel
import net.bmgames.game.message.Message

interface IConnection {
    val outgoing: ReceiveChannel<Message>
    suspend fun tryQueueCommand(commandLine: String): Either<String, Unit>
    suspend fun close()
}
