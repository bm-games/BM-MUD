package net.bmgames.action

import net.bmgames.game.state.Game
import net.bmgames.game.state.Player

class EntityAction (type : type,/* room : Room,*/ entity : Player.Normal) : Update {

    enum class type {
        Create, Remove
    }

    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}