package net.bmgames.game.action

import arrow.core.Either
import arrow.core.none
import arrow.core.some
import arrow.optics.dsl.at
import arrow.optics.typeclasses.At
import net.bmgames.atIndex
import net.bmgames.state.model.*


data class EntityAction(val type: Type, val room: Room, val entity: Either<NPC, Item>) : Update() {

    private val roomLens = Game.rooms.atIndex(room.name)


    enum class Type {
        Create, Remove
    }

    override fun update(game: Game): Game =
        entity.fold(
            {
                roomLens.compose(Room.npcs.at(At.map(), it.name))
                    .set(game, if (type == Type.Create) it.some() else none())
            },
            { item ->
                roomLens.compose(Room.items).modify(game) {
                    if (type == Type.Create) it + item else it - item
                }
            }
        )
}

