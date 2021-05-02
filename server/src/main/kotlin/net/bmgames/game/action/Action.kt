package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.game.state.Game

sealed class Action

abstract class Effect : Action() {
    abstract fun run(gameRunner: GameRunner)
}

abstract class Update : Action() {
    abstract fun update(game: Game): Game
}
