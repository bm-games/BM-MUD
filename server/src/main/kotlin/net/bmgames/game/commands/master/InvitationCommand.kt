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

import net.bmgames.toList

/**
 * A mastercommand which invites the given player.
 * The commands can also accept and reject join requests or list all join requests.
 * The params are given trough arguments -> "by argument"
 *
 * @param type the possible types of the invite command are: Accept, Reject, Invite, List.
 * @param username the user who will be invited, or whose invite will be accepted or rejected.
 *
 * @constructor creates a complete invitation command.
 */
class InvitationCommand : MasterCommand("invite") {

    val type: InviteAction.Type by argument().enum()
    val username: String? by argument(help = message("game.invite-user")).optional()

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
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

