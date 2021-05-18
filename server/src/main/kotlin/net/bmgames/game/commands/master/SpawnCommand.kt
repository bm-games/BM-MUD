package net.bmgames.game.commands.master

import arrow.core.*
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.bmgames.game.action.Action
import net.bmgames.game.action.EntityAction
import net.bmgames.game.action.EntityAction.Type.Create
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.master.SpawnCommand.Type.*
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.toEither
import net.bmgames.toList

class SpawnCommand : MasterCommand("spawn") {
    val type: Type by argument(help = message("game.spawn.type")).enum()
    val roomName: String by argument(name = "room", help = message("game.spawn.room"))
    val name: String by argument(help = message("game.spawn.name"))

    enum class Type {
        NPC, ITEM
    }

    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> =
        game.getRoom(roomName).rightIfNotNull { message("game.hit.room-not-found", roomName) }
            .flatMap { room ->
                when (type) {
                    NPC -> game.npcConfigs[name].toEither(
                        { message("game.spawn.npc-not-found") },
                        { npc ->
                            EntityAction(Create, room, npc.left()).toList()
//                                .plus(game.onlinePlayers.values.filter { it.isInRoom(roomName) }.map { it.sendText(
//                                    message("game.spawn.appeared")) })

                        })

                    ITEM -> game.itemConfigs[name].toEither(
                        { message("game.spawn.npc-not-found") },
                        { item ->
                            EntityAction(Create, room, item.right()).toList()
//                                .plus(game.onlinePlayers.values.filter { it.isInRoom(roomName) }.map { it.sendText(
//                                    message("game.spawn.appeared")) })

                        })
                }
            }

}
