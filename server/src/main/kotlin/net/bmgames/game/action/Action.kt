package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.state.model.Game
/**
 * Actions are used to manipulate the game's state, while it's running
 */
sealed class Action

abstract class Effect : Action() {
    abstract suspend fun run(gameRunner: GameRunner)
}

abstract class Update : Action() {
    abstract fun update(game: Game): Game
}
