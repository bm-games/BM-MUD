package net.bmgames.game.action

import arrow.core.Endo
import arrow.core.foldMap
import arrow.core.none
import arrow.core.some
import arrow.typeclasses.Monoid
import net.bmgames.at
import net.bmgames.atIndex
import net.bmgames.set
import net.bmgames.state.model.Game
import net.bmgames.state.model.Room
import net.bmgames.state.model.neighbours
import net.bmgames.state.model.rooms

/**
 * An action which gives notifes the given player with a message. It's done via email.
 *
 *
 * @param type of the room action. Can be Delete and Create.
 * @param room which shall be created or deleted
 *
 * @constructor creates a complete room action.
 */

data class RoomAction(val type: Type, val room: Room) : Update() {

    /**
     * Updates the gamestate based on the action. It creates or deleted a room,
     * depending on the given type in the constructor.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    override fun update(game: Game): Game {
        val changedRoom = if (type == Type.Create) room.some() else none()
        return listOf(Game.rooms.at(room.name).set(changedRoom))
            .plus(
                room.neighbours.map { (dir, neighbour) ->
                    Game.rooms.atIndex(neighbour)
                        .compose(Room.neighbours.at(dir.getOpposite()))
                        .set(changedRoom.map { it.name })
                }
            )
            .foldMap(Monoid.endo(), ::Endo)
            .f(game)
    }


    enum class Type { Delete, Create }
}
