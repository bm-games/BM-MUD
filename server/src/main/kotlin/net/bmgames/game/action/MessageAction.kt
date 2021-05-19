package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * An action which sends a message by the given player.
 *
 *
 * @param player the Player who sends the message.
 * @param message the message which gets send
 *
 * @constructor creates a complete message action
 */
data class MessageAction (val player : Player, val message: Message) : Effect() {
    /**
     * Runs the MessageAction. Sends a message by the player given in the field.
     *
     * @param gameRunner the gameRunner the action is performed with.
     */
    override suspend fun run(gameRunner: GameRunner) {
        val connection = gameRunner.getConnection(player.ingameName)
        connection
            ?.outgoingChannel
            ?.send(message)
    }
}
/**
 * Directly a message by the player. Creates a MessageAction to simplify usage in different parts of the code.
 *
 * @param message the message of the player.
 */
fun Player.sendText(message: String) = MessageAction(this, Message.Text(message))

fun Player.sendMap(game: Game) = MessageAction(this, Message.Map(game, this))
