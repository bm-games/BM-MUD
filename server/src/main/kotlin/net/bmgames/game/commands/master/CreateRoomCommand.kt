package net.bmgames.game.commands.master

import arrow.core.Either
import arrow.core.Either.Companion.conditionally
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import arrow.core.traverseEither
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import net.bmgames.errorMsg
import net.bmgames.game.action.Action
import net.bmgames.game.action.RoomAction
import net.bmgames.game.action.RoomAction.Type.Create
import net.bmgames.game.commands.MasterCommand
import net.bmgames.message
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Direction.*
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room
import net.bmgames.toEither
import net.bmgames.toList

class CreateRoomCommand : MasterCommand("createroom") {

    val north by option("-n", "--north", help = message("game.room.dir", "Norden"))
    val west by option("-w", "--west", help = message("game.room.dir", "Westen"))
    val east by option("-e", "--east", help = message("game.room.dir", "Osten"))
    val south by option("-s", "--south", help = message("game.room.dir", "Süden"))
    val name: String by argument(help = message("game.spawn.room"))
    val message: String by argument(help = message("game.spawn.room-message"))

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        if (game.rooms.containsKey(name)) {
            errorMsg(message("game.room.exists-already"))
        } else {
            either.eager {
                val neighbors = mapOf(NORTH to north, WEST to west, EAST to east, SOUTH to south)
                    .filterNot { it.value == null }
                    .traverseEither { name ->
                        conditionally(
                            game.rooms.containsKey(name),
                            { message("game.hit.room-not-found", name) },
                            { name!! })
                    }.bind()

                RoomAction(Create, Room(name, message, neighbors)).toList()
            }
        }

}
