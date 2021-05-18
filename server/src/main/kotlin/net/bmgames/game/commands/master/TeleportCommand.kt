package net.bmgames.game.commands.master

import arrow.core.Either
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.game.action.Action
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

/**
 * A mastercommand which teleports a player into a new room.
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player who will be teleported.
 * @param newRoomID the identifier of the room the player will be teleported to.
 *
 * @constructor creates a complete teleport command.
 */
class TeleportCommand : MasterCommand("teleport") {
    val target: String by argument(help = message("game.teleport-user"))
    val newRoomID: String by argument(help = message("game.teleport.position"))
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It teleports the player into the new room
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
