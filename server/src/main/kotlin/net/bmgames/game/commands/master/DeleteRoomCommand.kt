package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.Either.Companion.conditionally
import arrow.core.flatten
import com.github.ajalt.clikt.parameters.arguments.argument
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.RoomAction
import net.bmgames.game.action.RoomAction.Type.Delete
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.isInRoom
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import net.bmgames.toList

class DeleteRoomCommand : MasterCommand("deleteroom") {

    val name: String by argument(help = message("game.spawn.room"))

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        if (name == game.startRoom) {
            errorMsg(message("game.room.cant-delete-start"))
        } else {
            game.getRoom(name).toEither(
                { message("game.hit.room-not-found", name) },
                { room ->
                    conditionally(
                        game.onlinePlayers.any { (_, p) -> p.isInRoom(name) },
                        { message("game.room.not-empty") },
                        { RoomAction(Delete, room).toList() }
                    )
                }
            ).flatten()
        }
}
