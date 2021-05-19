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
class SpawnCommand : MasterCommand("spawn", message("game.spawn-epilog")) {
    val type: Type by argument(help = message("game.spawn.type")).enum()
    val roomName: String by argument(name = "room", help = message("game.spawn.room"))
    val name: String by argument(help = message("game.spawn.name"))

    enum class Type {
        NPC, ITEM
    }

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It spawns the specified entities in the given room 'amount' times.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
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
