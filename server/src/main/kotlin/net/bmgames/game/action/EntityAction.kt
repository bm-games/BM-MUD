package net.bmgames.game.action

import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class EntityAction (type : type,/* room : Room,*/ entity : Player.Normal) : Update() {

    enum class type {
        Create, Remove
    }

    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}
