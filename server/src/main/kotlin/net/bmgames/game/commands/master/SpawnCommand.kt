package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

class SpawnCommand : MasterCommand("spawn") {
    val targetType: String by argument(help = "The type of the entity to be spawned")
    val targetID: String by argument(help = "The id of the entity to be spawned")
    val roomID: String by argument(help = "The id of the room where you want to spawn to entity in")
    val amount: String by argument(help = "The amount of entities to be spawned by the command")
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
