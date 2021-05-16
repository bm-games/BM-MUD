package net.bmgames.game.commands.player

import arrow.core.Either
import net.bmgames.game.action.Action
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.*
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toList


class LookCommand : PlayerCommand("look") {
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        player.getRoom(game).map { room ->
            val players = player.getOtherPlayersInRoom(game).keys
            player.sendText(
                "You are in ${room.name}.\n" +
                        "${room.message} \n" +
                        players.prettyJoin(suffix = "in this room.\n") +
                        "Some items lay on the floor: " + room.items.prettyJoin(suffix = "in this room.\n")
            ).toList()
        }
}
