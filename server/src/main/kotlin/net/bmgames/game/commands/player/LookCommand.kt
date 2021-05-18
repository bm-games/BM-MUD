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


class LookCommand : PlayerCommand("stats") {
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> =
        player.sendText(message("game.stats", player.healthPoints, player.maxHealthPoints, player.damage.toInt()))
            .toList()
            .right()
}
