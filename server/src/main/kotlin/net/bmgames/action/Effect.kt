package net.bmgames.action

import net.bmgames.game.connection.GameRunner

interface Effect : Action {
    fun run(gameRunner: GameRunner)
}