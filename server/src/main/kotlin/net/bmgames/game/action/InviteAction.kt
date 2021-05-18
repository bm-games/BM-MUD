package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.action.InviteAction.Type.*
import net.bmgames.game.action.InviteAction.Type.List
import net.bmgames.state.model.Game

data class InviteAction(val user: User, val type: Type) : Update() {

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
