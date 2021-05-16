package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.InviteAction
import net.bmgames.game.action.InviteAction.Type.Accept
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toList

class InvitationCommand : MasterCommand("invite") {

    val type: InviteAction.Type by argument().enum()
    val username: String? by argument(help = message("game.invite-user"))
        .optional()

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        return if (type == InviteAction.Type.List) {
            player.sendText(game.joinRequests.joinToString("\n") { it.username }).toList().right()
        } else {
            val request = game.joinRequests.find { it.username.equals(username, ignoreCase = true) }
            return if (request != null) {
                InviteAction(request, Accept).toList().right()
            } else {
                errorMsg(message("game.invite.not-found"))
            }
        }
    }
}

