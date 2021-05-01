package net.bmgames.game

import arrow.fx.coroutines.Atomic
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.connection.GameRunner.Companion.start
import net.bmgames.game.state.Game

class GameManager(private val repository: GameRepository) {
    private val gamesRef: Atomic<Map<String, GameRunner>> = Atomic.unsafe(emptyMap())

    internal suspend fun getGameRunner(name: String): GameRunner? =
        gamesRef.get()[name]

    suspend fun getGame(name: String) = getGameRunner(name)?.getCurrentGameState()

    suspend fun startGame(gameName: String) {
        val gameRunner = getGameRunner(gameName)
            ?: repository.loadGame(gameName)?.start()
        if(gameRunner != null) {
            gamesRef.update { games ->
                if (games.containsKey(gameName)) games
                else games.plus(gameName to gameRunner)
            }
        }
    }

    suspend fun getGames(): List<Game> =
        gamesRef.get().entries
            .map { (_, gameRunner) -> gameRunner.getCurrentGameState() }

}
