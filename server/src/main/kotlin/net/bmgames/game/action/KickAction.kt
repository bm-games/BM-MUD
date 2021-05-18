package net.bmgames.game.action

import arrow.core.none
import net.bmgames.at
import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.message
import net.bmgames.state.PlayerRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.allowedUsers

data class KickAction(val user: User) : Effect() {
    override suspend fun run(gameRunner: GameRunner) {
        val game = gameRunner.getCurrentGameState()
        val avatars = game.allowedUsers[user.username] ?: emptySet()
        avatars.forEach {
            gameRunner.getConnection(it)?.close(message("game.kick.reason"))
        }
        gameRunner.updateGameState { oldGame ->
            Game.allowedUsers.at(user.username).set(oldGame, none())
        }
        PlayerRepository.deletePlayers(game.id, avatars)
    }
}
