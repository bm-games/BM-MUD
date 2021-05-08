package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner

data class MasterCommandAction (val command: String) : Effect() {

    override fun run(gameRunner: GameRunner) {
        TODO("Not yet implemented")
    }
}
