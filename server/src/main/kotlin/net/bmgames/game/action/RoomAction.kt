package net.bmgames.game.action

import net.bmgames.state.model.Game
import net.bmgames.state.model.Room

data class RoomAction(val type: Type, val room: Room) : Update() {
    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }

    enum class Type { Delete, Create }
}
