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

/**
 * A mastercommand which kicks the given player from the game.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who should be kicked.
 *
 * @constructor creates a complete kick command.
 */
class KickCommand : MasterCommand("kick", message("game.kick-epilog")) {
    val playerName: String by argument(name = "player", help = message("game.kick.player"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It kicks the player from the game.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        game.getOnlineNormal(playerName).toEither(
            { message("game.kick.user-not-joined", playerName) },
            { p ->
                listOf(
                    player.sendText(message("game.kick.kicked", playerName)),
                    KickAction(p.user)
                )


            }
        )
}
