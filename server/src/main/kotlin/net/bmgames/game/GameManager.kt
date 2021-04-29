package net.bmgames.game

import arrow.fx.coroutines.Atomic
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.connection.start
import net.bmgames.game.state.Game

class GameManager(private val persistence: GamePersistenceManager) {
    private val gamesRef: Atomic<Map<String, GameRunner>> = Atomic.unsafe(emptyMap())

    private suspend fun getGameRunner(name: String): GameRunner? =
        gamesRef.modify { games ->
            val runningGame = games[name]
            if (runningGame != null) {
                games to runningGame
            } else {
                val stoppedGame = persistence.loadGame(name)
                val startedGame = stoppedGame?.start()
                if (startedGame != null)
                    games.plus(name to startedGame) to startedGame
                else games to null
            }
        }

    suspend fun getGame(name: String) = getGameRunner(name)?.getCurrentGameState()

    suspend fun getGames(): List<Game> =
        gamesRef.get().entries
            .map { (_, gameRunner) -> gameRunner.getCurrentGameState() }

}
