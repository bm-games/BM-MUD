package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

data class NotifyUserAction(val user: User, val message: String) : Effect() {
    override fun run(gameRunner: GameRunner) {
        TODO("Not yet implemented")
    }
}
