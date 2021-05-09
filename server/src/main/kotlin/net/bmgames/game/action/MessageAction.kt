package net.bmgames.game.action

import net.bmgames.game.connection.GameRunner
import net.bmgames.game.message.Message
import net.bmgames.state.model.Player

class MessageAction (val player : Player, val message: Message) : Effect() {
    override fun run(gameRunner: GameRunner){
        TODO("Not yet implemented")
    }
}
