package net.bmgames.game.action

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.ITEMS
import net.bmgames.game.PLAYER
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Player
import net.bmgames.state.model.Weapon

class InventoryActionTest : FunSpec({

    test("Should modify player inventory") {
        val newInv = Inventory(weapon = ITEMS["Wooden Sword"] as Weapon)
        val newPlayer = with(GAME_WITH_PLAYER) {
            val update = InventoryAction(PLAYER, newInv)
            update.update(this).getPlayer(PLAYER.ingameName)!! as Player.Normal
        }

        newPlayer.inventory shouldBe newInv
    }
})
