package net.bmgames.game.commands

import arrow.core.Either
import net.bmgames.game.state.Game


interface Command {
    /**
     * Check if this command can execute depending on the issuer and the current game state.
     * @param game The current game state
     * @return If the command can be executed, a list of actions. Else an error message.
     * */
    fun toAction(game: Game): Either<String, List<String>>
}
