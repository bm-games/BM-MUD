package net.bmgames.game.connection

import arrow.core.Either
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import net.bmgames.game.commands.Command
import net.bmgames.game.message.Message

internal data class Connection(
    private val parseCommand: (String) -> Either<String, Command<*>>,
    internal val incoming: Channel<Command<*>>,
) : IConnection {

    internal val outgoingChannel = Channel<Message>()
    override val outgoing: ReceiveChannel<Message> = outgoingChannel

    override suspend fun tryQueueCommand(commandLine: String): Either<String, Unit> =
        parseCommand(commandLine)
            .map { incoming.send(it) }

    override suspend fun close() {
        outgoingChannel.close()
    }
}
