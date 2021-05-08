package net.bmgames.game.action

import arrow.core.Either
import net.bmgames.state.model.Game
import net.bmgames.state.model.NPC
import net.bmgames.state.model.Player

data class HealthAction(val target: Either<Player.Normal, NPC.Hostile>, val delta: Int) : Update() {
    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}
