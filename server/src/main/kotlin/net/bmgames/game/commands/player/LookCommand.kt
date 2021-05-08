package net.bmgames.game.commands.player

import arrow.core.Either
import net.bmgames.game.action.Action
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class LookCommand : PlayerCommand("look") {
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
