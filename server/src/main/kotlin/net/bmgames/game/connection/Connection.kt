package net.bmgames.game.connection

import arrow.core.Either
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import net.bmgames.game.GameScope
import net.bmgames.game.commands.Command
import net.bmgames.game.message.Message

/**
 * The connection to a MUD player from the [GameRunner] side.
 * @property parseCommand The command parser function, either for a normal player or the master
 * @property incoming Queue for already parsed commands which are tied to this connection
 * @property outgoingChannel Queue for sending messages to the client
 * */
internal data class Connection(
    private val parseCommand: (String) -> Either<String, Command<*>>,
) : IConnection {

    private val closeListeners = mutableListOf<(String) -> Unit>()

    internal val incoming = Channel<Command<*>>()

    internal val outgoingChannel = Channel<Message>()
    override val outgoing: ReceiveChannel<Message> = outgoingChannel

    override suspend fun tryQueueCommand(commandLine: String): Either<String, Unit> =
        parseCommand(commandLine)
            .map { incoming.send(it) }

    override fun onClose(listener: (String) -> Unit) {
        closeListeners.add(listener)
    }

    override suspend fun close(reason: String) {
        closeListeners.forEach { it(reason) }
        incoming.close()
        outgoingChannel.close()
    }
}
