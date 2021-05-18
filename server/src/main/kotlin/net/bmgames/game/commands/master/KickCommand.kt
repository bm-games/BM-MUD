package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.flatten
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.action.KickAction
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.UserRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither

class KickCommand : MasterCommand("kick") {
    val username: String by argument(help = message("game.kick.user"))

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        game.allowedUsers[username].toEither(
            { message("game.kick.user-not-joined", username) },
            {
                UserRepository.getUserByName(username).toEither(
                    { message("game.invite.user-not-found", username) },
                    { user ->
                        listOf(
                            player.sendText(message("game.kick.kicked", username)),
                            KickAction(user)
                        )
                    }
                )

            }
        ).flatten()
}
