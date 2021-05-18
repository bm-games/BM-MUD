package net.bmgames.game.commands.player

import arrow.core.Either
import arrow.core.right
import net.bmgames.game.action.Action
import net.bmgames.game.action.sendText
import net.bmgames.game.commands.PlayerCommand
import net.bmgames.message
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.toRelativePercent
import net.bmgames.toList
import net.bmgames.translate

/**
 * A playercommand which shows the contents of the executing player's inventory to said player.
 * The params are given trough arguments -> "by argument"
 *
 * @constructor creates a complete inventory command.
 */
class InventoryCommand : PlayerCommand("inventory") {
    /**
     * Creates a list of actions, which shall be executed in order, based on the Command.
     * It shows the contents of the executing player's inventory.
     *
     * @param player the player who started the command.
     * @param game the game the command is performed in.
     *
     * @return a string which shows the errormessage or the list of actions which will be executed.
     */
    override fun toAction(player: Player.Normal, game: Game): Either<String, List<Action>> = with(player.inventory) {
        listOfNotNull(
            if (equipment.isNotEmpty()) {
                equipment.map { (_, equi) ->
                    "${equi.translate()}: ${equi.name} " +
                            "(+${equi.damageModifier.toRelativePercent()}% Damage) " +
                            "(+${equi.healthModifier.toRelativePercent()}% HP)"
                }.joinToString("\n")
            } else {
                null
            },

            weapon?.let {
                "${it.translate()}: ${it.name} (Damage: ${it.damage})"
            },

            if (items.isNotEmpty()) "Items: " + items.joinToString(", ") { it.name }
            else null,

            if (allItems().isEmpty()) message("game.inv.empty")
            else null

        )
            .joinToString("\n")
            .let { player.sendText(it).toList().right() }
    }

}
