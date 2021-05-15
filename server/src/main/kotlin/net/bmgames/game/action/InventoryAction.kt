package net.bmgames.game.action

import net.bmgames.atIndex
import net.bmgames.state.model.*

data class InventoryAction(val player: Player.Normal, val inventory: Inventory) : Update() {

    override fun update(game: Game): Game =
        Game.onlinePlayers.atIndex(player.ingameName)
            .compose(Player.normal.inventory)
            .set(game, inventory)

}
