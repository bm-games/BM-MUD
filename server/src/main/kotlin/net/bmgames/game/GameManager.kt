package net.bmgames.game

import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.bmgames.communication.Notifier
import net.bmgames.game.connection.GameRunner
import net.bmgames.state.GameRepository

/**
 * The Coroutine Scope where all game related tasks run
 * */
internal object GameScope : CoroutineScope {
    override val coroutineContext = SupervisorJob() + Dispatchers.Default
}

/**
 * Manages all running games
 * @property gamesRef Stores all running games threadsafe
 * */
class GameManager(val notifier: Notifier) {
    private val gamesRef: Atomic<Map<String, GameRunner>> = Atomic.unsafe(emptyMap())

    /**
     * @return A started game runner, if it exists. Otherwise it gets loaded from the database
     * */
    internal suspend fun getGameRunner(gameName: String): GameRunner? {
        val gameRunner = gamesRef.get()[gameName]
        return if (gameRunner == null) {
            val stoppedGameRunner = GameRepository.getGame(gameName)?.let {GameRunner(it, notifier)}
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

    internal suspend fun stopGame(gameRunner: GameRunner): Unit{
        GameRepository.saveGame(gameRunner.stop())
        gamesRef.update { it - gameRunner.name }

    }

}
