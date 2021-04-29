package net.bmgames.game

import net.bmgames.game.state.Game

class GamePersistenceManager {
    internal fun save(game: Game) {
        game.name
        //TODO save to DB
    }

    internal fun loadGame(name: String): Game? {
        return Game(name) //TODO load from DB
    }

    internal fun listGames(): List<Game> {
        return listOf() //TODO load from DB
    }
}
