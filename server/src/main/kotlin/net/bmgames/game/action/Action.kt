package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.state.model.Game

/**
 * Actions are used to manipulate the game's state or run effects like sending an email or launching a misile
 * while the game is running.
 */
sealed class Action

/**
 * A type of action which is impure.
 *
 */
abstract class Effect : Action() {

    /**
     * Runs this effect.
     *
     * @param gameRunner the gameRunner the actions are performed with.
     */
    abstract suspend fun run(gameRunner: GameRunner)
}

/**
 * A type of action which is pure.
 * That means no side effects get executed in the update method.
 * */
abstract class Update : Action() {

    /**
     * Updates the gamestate based on the action.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    abstract fun update(game: Game): Game
}
