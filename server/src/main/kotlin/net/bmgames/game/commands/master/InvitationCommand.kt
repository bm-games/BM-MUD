package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.*
import net.bmgames.game.action.Action
import net.bmgames.game.action.InviteAction
import net.bmgames.game.action.InviteAction.Type.*
import net.bmgames.game.action.NotifyUserAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.UserRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class InvitationCommand : MasterCommand("invite") {

    val type: InviteAction.Type by argument().enum()
    val username: String? by argument(help = message("game.invite-user")).optional()

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        when {
            type == List -> player.sendText(game.joinRequests.joinToString("\n") { it.username }).toList().right()
            username != null -> {
                when (type) {
                    Accept -> game.joinRequests.find { it.username == username }.toEither(
                        { message("game.invite.not-found") },
                        { request ->
                            listOf(
                                InviteAction(request, Accept),
                                player.sendText(message("game.invite.accepted", username)),
                                NotifyUserAction(
                                    user = request,
                                    subject = message("game.invite.accepted-subject", game.name),
                                    message = message("game.invite.accepted-message", username, game.name)
                                )
                            )
                        }
                    )
                    Reject -> game.joinRequests.find { it.username == username }.toEither(
                        { message("game.invite.not-found") },
                        { request ->
                            listOf(
                                InviteAction(request, Reject),
                                player.sendText(message("game.invite.rejected", username)),
                                NotifyUserAction(
                                    user = request,
                                    subject = message("game.invite.rejected-subject", game.name),
                                    message = message("game.invite.rejected-message", username)
                                )
                            )
                        }
                    )
                    Add -> when {
                        game.joinRequests.any { it.username == username } ->
                            errorMsg(message("game.invite.already-invited", username, username))
                        game.allowedUsers.containsKey(username) ->
                            errorMsg(message("game.invite.already-accepted", username))
                        else -> {
                            UserRepository.getUserByName(username!!).toEither(
                                { message("game.invite.user-not-found", username) },
                                { user ->
                                    listOf(
                                        player.sendText(message("game.invite-sent")),
                                        InviteAction(user, Add)
                                    )
                                }
                            )
                        }
                    }
                    else -> success(emptyList()) //Never happens
                }
            }
            else -> errorMsg(message("game.invite.user-missing"))
        }

}

