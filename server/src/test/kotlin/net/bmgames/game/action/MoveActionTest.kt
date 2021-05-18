package net.bmgames.game.action

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.ITEMS
import net.bmgames.game.PLAYER
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Inventory
import net.bmgames.state.model.Player
import net.bmgames.state.model.Weapon

class MoveActionTest : FunSpec({

    test("Should move player if room exists") {
        val from = GAME_WITH_PLAYER.getStartRoom()
        val to = from.getNeighbour(GAME_WITH_PLAYER, Direction.SOUTH)!!
        val newPlayer = with(GAME_WITH_PLAYER) {
            val update = MoveAction(PLAYER, to)
            update.update(this).getPlayer(PLAYER.ingameName)!! as Player.Normal
        }

        newPlayer.room shouldBe to.name
    }
    test("Should not move player to non existent room") {
        val from = GAME_WITH_PLAYER.getStartRoom()
        val to = from.getNeighbour(GAME_WITH_PLAYER, Direction.SOUTH)!!
            .copy(name = "Missing room")
        val newPlayer = with(GAME_WITH_PLAYER) {
            val update = MoveAction(PLAYER, to)
            update.update(this).getPlayer(PLAYER.ingameName)!! as Player.Normal
        }

        newPlayer.room shouldBe from.name
    }
})
