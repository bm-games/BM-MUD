package net.bmgames.game.action

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.PLAYER
import net.bmgames.game.npcs
import net.bmgames.state.model.NPC
import net.bmgames.state.model.Player

class HealthActionTest : FunSpec({


    test("Should modify NPC health") {
        val georkina = npcs["georkina"]!! as NPC.Hostile
        val newNPC = with(GAME_WITHOUT_PLAYER) {
            val update = HealthAction((getStartRoom() to georkina).right(), -10)
            update.update(this).getStartRoom().npcs["georkina"]!! as NPC.Hostile
        }

        newNPC.health shouldBe (georkina.health - 10)
    }

    test("Should modify player health") {
        val newPlayer = with(GAME_WITH_PLAYER) {
            val update = HealthAction(PLAYER.left(), 10)
            update.update(this).getPlayer(PLAYER.ingameName)!! as Player.Normal
        }

        newPlayer.healthPoints shouldBe (PLAYER.healthPoints + 10)
    }

})
