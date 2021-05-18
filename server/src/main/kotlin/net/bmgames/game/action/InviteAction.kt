package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

/**
 * An action which invites a player to a game, or accepts/rejects a players join request.
 *
 *
 * @param user the user who gets invited or has issued a join request.
 * @param type the type of the invite action, which is performed on the player. It can either be Accept, Reject, Invite or List.
 * List just shows all current invites and join requests
 *
 * @constructor creates a complete health action
 */
data class InviteAction(val user: User, val type: Type) : Effect() {
    /**
     * Runs the Inviteaction.
     *
     * @param gameRunner the gameRunner the action is performed with.
     */
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
