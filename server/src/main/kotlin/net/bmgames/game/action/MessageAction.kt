package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

data class MessageAction (val player : Player, val message: Message) : Effect() {
    override suspend fun run(gameRunner: GameRunner) {
        val connection = gameRunner.getConnection(player.ingameName)
        connection
            ?.outgoingChannel
            ?.send(message)
    }
}

fun Player.sendText(message: String) = MessageAction(this, Message.Text(message))

fun Player.sendMap(game: Game) = MessageAction(this, Message.Map(game, this))
