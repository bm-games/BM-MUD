package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

data class MessageAction (val player : Player, val message: Message) : Effect() {
    override suspend fun run(gameRunner: GameRunner) {
        gameRunner.getConnection(player.ingameName)
            ?.outgoingChannel
            ?.send(message)
    }
}

fun Player.sendText(message: String) = MessageAction(this, Message.Text(message))
