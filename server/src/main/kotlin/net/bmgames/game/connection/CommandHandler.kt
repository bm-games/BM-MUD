package net.bmgames.game.connection

import arrow.core.Either

interface CommandHandler {
    suspend fun tryQueueCommand(playerName: String, command: String): Either<String, Unit>
}
