package net.bmgames.game

import net.bmgames.game.state.Game

object GameRepository {
    internal fun save(game: Game) {
        TODO("Save game")
    }

    internal fun loadGame(name: String): Game? {
        return null//TODO("Load from DB")
    }

    internal fun listGames(): List<Game> {
        return emptyList()//TODO("Load from DB")
    }
}
