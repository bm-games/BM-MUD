package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.action.InviteAction.Type.*
import net.bmgames.game.action.InviteAction.Type.List
import net.bmgames.state.model.Game

data class InviteAction(val user: User, val type: Type) : Update() {


/**
 * An action which invites a player to a game, or accepts/rejects a players join request.
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
    override fun update(game: Game): Game =
        when (type) {
            Accept -> game.copy(
                joinRequests = game.joinRequests - user,
                allowedUsers = game.allowedUsers + (user.username to emptySet())
            )
            Reject -> game.copy(
                joinRequests = game.joinRequests - user
            )
            Add -> game.copy(
                joinRequests = game.joinRequests + user
            )
            List -> game
        }

    enum class Type {
        Accept, Reject, Add, List
    }
}
