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

data class RoomAction(val type: Type, val room: Room) : Update() {

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
