package net.bmgames.game.commands

import arrow.core.Either
import arrow.core.traverseEither
import com.github.ajalt.clikt.core.CliktCommand
import net.bmgames.game.action.Action
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player


sealed class Command<in P : Player>(name: String, help: String) : CliktCommand(name = name, help = help) {
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


abstract class MasterCommand(name: String, help: String) : Command<Player.Master>(name, help)
abstract class PlayerCommand(name: String, help: String) : Command<Player.Normal>(name, help)

data class BatchCommand<in P : Player>(val commands: List<Command<P>>) : Command<P>("batch", "") {
    override fun toAction(player: P, game: Game): Either<String, List<Action>> =
        commands.traverseEither { it.toAction(player, game) }
            .map { it.flatten() }

}
