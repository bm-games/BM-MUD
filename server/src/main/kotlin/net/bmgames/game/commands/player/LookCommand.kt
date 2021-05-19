package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.right
import net.bmgames.game.action.Action
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.*
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toList


/**
 * A playercommand which shows the contents of the executing player's room to said player.
 * The params are given trough arguments -> "by argument"
 *
 * @constructor creates a complete look command.
 */
class LookCommand : PlayerCommand("look") {
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It shows the contents of the executing player's room.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        player.getRoom(game).map { room ->
            val players = player.getOtherPlayersInRoom(game).keys
            listOfNotNull(
                player.sendText(message("game.look.room", room.name, room.message)),
                if (players.isNotEmpty())
                    player.sendText(players.prettyJoin(suffix = message("game.look.players")))
                else null,
                if (room.items.isNotEmpty())
                    player.sendText(
                        message("game.look.items", room.items.joinToString(", ") { it.name })
                    )
                else null,
                if (room.npcs.isNotEmpty())
                    player.sendText(
                        room.npcs.keys.prettyJoin(
                            prefix = "NPC",
                            singularVerb = "lebt",
                            pluralVerb = "leben",
                            suffix = "hier."
                        )
                    )
                else null
            )
        }
}
