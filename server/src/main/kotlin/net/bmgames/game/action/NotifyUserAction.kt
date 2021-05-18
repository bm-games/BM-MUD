package net.bmgames.game.action

import net.bmgames.authentication.User
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player
/**
 * An action which gives notifes the given player with a message. It's done via email.
 *
 *
 * @param user the player who gets notified.
 * @param subject tthe subject of the mail.
 * @param message the message, which is in the mail.
 *
 * @constructor creates a complete notify user action
 */
data class NotifyUserAction(val user: User, val subject: String, val message: String) : Effect() {
    /**
     * Runs the NotifyUserAction. Sends the player given in the field, the message which is given through the constructor, via mail.
     *
     * @param gameRunner the gameRunner the action is performed with.
     */
    override suspend fun run(gameRunner: GameRunner) = gameRunner.notifier.send(user, subject, message)
}
