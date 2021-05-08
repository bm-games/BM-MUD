package net.bmgames.game.action

import arrow.core.Either
import net.bmgames.state.model.*

data class EntityAction(val type: Type, val room: Room, val entity: Either<NPC, Item>) : Update() {

    enum class Type {
        Create, Remove
    }

    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}
