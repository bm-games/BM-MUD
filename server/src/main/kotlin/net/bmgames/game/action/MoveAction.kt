package net.bmgames.game.action

import arrow.core.Either
import net.bmgames.state.model.Game
import net.bmgames.state.model.NPC
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room

data class MoveAction(val entity: Either<Player.Normal, NPC>, val from: Room, val to: Room) : Update() {
    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}
