package net.bmgames.game.action

import arrow.core.none
import net.bmgames.at
import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.message
import net.bmgames.state.PlayerRepository
import net.bmgames.state.model.Game
import net.bmgames.state.model.allowedUsers


/**
 * An action which kicks a player out of the game.
 * All his avatars get removed and he is disconnected from the game.
 * Also he has to send a join request again to play in the game.
 *
 * @param user the user who gets kicked.
 * @constructor creates a complete kick
 */
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
