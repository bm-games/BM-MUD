package net.bmgames.game.commands.master

import arrow.core.*
import arrow.core.computations.either
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import net.bmgames.errorMsg
import net.bmgames.game.action.*
import net.bmgames.game.commands.MasterCommand
import net.bmgames.game.commands.getPlayerOrNPC
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Player
import net.bmgames.state.model.items
import net.bmgames.toEither
import kotlin.math.max

/**
 * A mastercommand which gives a Player or NPC an item
 * The params are given trough arguments -> "by argument"
 *
 * @param target the player or NPC which is targeted.
 * @param roomName the room where the target is located.
 * @param itemName the item that is given to the target.
 *
 * @@constructor creates a complete give command
 */
class GiveCommand : MasterCommand("give", message("game.give-epilog")) {
    val target: String by argument(help = message("game.hit.target"))
    val roomName: String by argument("room", help = message("game.hit.room"))
    val itemName: String by argument("item", help = message("game.item-name"))

    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It kicks the player from the game.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Master, game: Game): Either<String, List<Action>> = either.eager {
        val room = game.getRoom(roomName)
            .rightIfNotNull { message("game.hit.room-not-found", roomName) }.bind()
        val item = game.itemConfigs[itemName].rightIfNotNull { message("game.give.item-not-found", itemName) }.bind()
        game.getPlayerOrNPC(target, room).toEither(
            { message("game.entity-not-found", target) },
            {
                it.fold(
                    { npc ->
                        listOf(
                            player.sendText("$itemName -> ${npc.name}"),
                            NPCInventoryAction(npc, room, npc.items + item)
                        ).right()
                    },
                    { p ->
                        if (p.inventory.isFull())
                            errorMsg(message("game.give.inv-full", target))
                        else
                            listOf(
                                player.sendText("$itemName -> $target"),
                                p.sendText("+$itemName"),
                                InventoryAction(p, Inventory.items.modify(p.inventory) { items -> items + item })
                            ).right()
                    }
                )
            }
        ).flatten().bind()
    }

}
