package net.bmgames.state

import net.bmgames.state.model.Player

object PlayerRepository {

    internal suspend fun loadPlayer(gameName: String, ingameName: String): Player.Normal? {
//        TODO("Not yet implemented")
        return null
    }

    internal suspend fun savePlayer(gameName: String, player: Player.Normal): Unit {
//        TODO("Not yet implemented")
    }
}
