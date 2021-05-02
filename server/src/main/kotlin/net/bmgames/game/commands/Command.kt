package net.bmgames.game.commands

import arrow.core.Either
import com.github.ajalt.clikt.core.CliktCommand
import net.bmgames.action.Action
import net.bmgames.game.state.Game
import net.bmgames.game.state.Player


sealed class Command<P : Player>(name: String) : CliktCommand(name) {
    /**
     * Check if this command can execute depending on the issuer and the current game state.
     * @param game The current game state
     * @return If the command can be executed, a list of actions. Else an error message.
     * */
    abstract fun toAction(player: P, game: Game): Either<String, List<Action>>


    /**
     * Do nothing
     * */
    override fun run() = Unit

}

abstract class MasterCommand(name: String) : Command<Player.Master>(name)
abstract class PlayerCommand(name: String) : Command<Player.Normal>(name)
