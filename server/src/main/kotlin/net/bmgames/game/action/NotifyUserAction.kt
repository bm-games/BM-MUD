package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

data class NotifyUserAction(val user: User, val subject: String, val message: String) : Effect() {
    override suspend fun run(gameRunner: GameRunner) = gameRunner.notifier.send(user, subject, message)
}
