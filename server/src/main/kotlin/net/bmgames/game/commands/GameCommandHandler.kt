package net.bmgames.game.commands

import arrow.core.Either
import kotlinx.coroutines.channels.Channel
import net.bmgames.game.connection.CommandHandler
import net.bmgames.game.state.Player

typealias IssuedCommand = Pair<String, Command<*>>

class GameCommandHandler(
    val playerCommands: Map<String, () -> Command<Player.Normal>>,
    val masterCommands: Map<String, () -> Command<Player.Master>>,
    val commandQueue: Channel<IssuedCommand>
) : CommandHandler {

    override suspend fun tryQueueCommand(playerName: String, command: String): Either<String, Unit> {
        TODO("Parse command and queue if successful")
        commandQueue
    }

}
