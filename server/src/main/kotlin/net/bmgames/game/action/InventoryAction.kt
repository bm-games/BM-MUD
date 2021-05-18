package net.bmgames.game.action

import net.bmgames.atIndex
import net.bmgames.state.model.*
/**
 * An action which updates the player's inventory.
 *
 *
 * @param player the player whose inventory should be updated.
 * @param inventory the new inventory of the targeted player.
 *
 * @constructor creates a complete health action
 */
data class InventoryAction(val player: Player.Normal, val inventory: Inventory) : Update() {

    /**
     * Updates the gamestate based on the action.
     *
     * @param game the game the action is performed in
     * @return returns the new gamestate
     */
    override fun update(game: Game): Game =
        Game.onlinePlayers.atIndex(player.ingameName)
            .compose(Player.normal.inventory)
            .set(game, inventory)

}
