package net.bmgames.game.action

import net.bmgames.state.model.Game
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Item
import net.bmgames.state.model.Player

data class InventoryAction(val player: Player.Normal, val inventory: Inventory) : Update() {

    override fun update(game: Game): Game {
        TODO("Not yet implemented")
    }
}
