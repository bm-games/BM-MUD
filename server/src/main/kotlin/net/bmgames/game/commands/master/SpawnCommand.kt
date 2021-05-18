package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
/**
 * A mastercommand which spawns an entity in a room. The params are given trough arguments -> "by argument"
 *
 * @param targetType the type of the entity which shall be spawned.
 * @param targetID the identifier of the entity which shall be spawned.
 * @param roomID the identifier of the room the entity shall be spawned in.
 * @param amount the amount of entities to be spawned in the room.
 *
 * @constructor creates a complete spawn command.
 */
class SpawnCommand : MasterCommand("spawn") {
    val targetType: String by argument(help = message("game.entity-type"))
    val targetID: String by argument(help = message("game.entity-id"))
    val roomID: String by argument(help = message("game.room-id"))
    val amount: String by argument(help = message("game.entity-amount"))
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It spawns the specified entities in the given room 'amount' times.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> {
        TODO("Not yet implemented")
    }
}
