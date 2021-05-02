package net.bmgames.game

import arrow.fx.coroutines.Atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.bmgames.authentication.User
import net.bmgames.configurator.model.DungeonConfig
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player

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

    fun createGame(config: DungeonConfig, master: User) {
        repository.save(
            Game(
                config,
                Player.Master(master),
                users = mapOf(master.username to emptyList()),
                onlinePlayers = emptyMap()
            )
        )
    }

    internal suspend fun getRunningGames() = gamesRef.get()

}
