package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

data class InviteAction(val user: User, val type: Type) : Effect() {
    override suspend fun run(gameRunner: GameRunner) {
        if (type == Type.Accept) {
            gameRunner.updateGameState {
                it.copy(
                    joinRequests = it.joinRequests - user,
                    allowedUsers = it.allowedUsers +(user.username to emptySet())
                )
            }
        }
    }

    enum class Type {
        Accept, Reject, Invite, List
    }
}
