package net.bmgames.game

import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.bmgames.game.connection.GameRunner
import net.bmgames.state.GameRepository

internal object GameScope : CoroutineScope {
    override val coroutineContext = SupervisorJob() + Dispatchers.Default
}

class GameManager(private val repository: GameRepository) {
    private val gamesRef: Atomic<Map<String, GameRunner>> = Atomic.unsafe(emptyMap())

    internal suspend fun getGameRunner(gameName: String): GameRunner? {
        val gameRunner = gamesRef.get()[gameName]
        return if (gameRunner == null) {
            val stoppedGameRunner = repository.loadGame(gameName)?.let(::GameRunner)
            if (stoppedGameRunner != null) {
                GameScope.launch { stoppedGameRunner.gameLoop() }
                gamesRef.update { games ->
                    if (games.containsKey(gameName)) games
                    else games.plus(gameName to stoppedGameRunner)
                }
            }
            stoppedGameRunner
        } else {
            gameRunner
        }
    }


    internal suspend fun getRunningGames() = gamesRef.get()

}
