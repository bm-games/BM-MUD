package net.bmgames.game.action

import arrow.core.Either
import arrow.core.none
import arrow.core.some
import arrow.optics.dsl.at
import arrow.optics.typeclasses.At
import net.bmgames.atIndex
import net.bmgames.state.model.*

/**
 * An action which creates and removes entities from rooms.
 *
 *
 * @param type the type of action to perform on the target.
 * @param room the room in which the action shall take place.
 * @param entity the type of entity which the action in type will be performed on.
 * @constructor creates a complete entity action
 */

data class EntityAction(val type: Type, val room: Room, val entity: Either<NPC, Item>) : Update() {

    private val roomLens = Game.rooms.atIndex(room.name)


    enum class Type {
        Create, Remove
    }
    /**
     * Updates the gamestate based on the action.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
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

