package net.bmgames.game.action

import arrow.core.identity
import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainAll
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.ITEMS
import net.bmgames.game.action.EntityAction.Type.*
import net.bmgames.game.npcs

class EntityActionTest : FunSpec({

    test("Should create NPC"){
        val newNPCs = with(GAME_WITHOUT_PLAYER) {
            val update = EntityAction(Create, getStartRoom(), npcs["geork"]!!.left())
            update.update(this).getStartRoom().npcs
        }

        newNPCs shouldContainAll npcs
    }

    test("Should remove NPC"){
        val newNPCs = with(GAME_WITHOUT_PLAYER) {
            val update = EntityAction(Remove, getStartRoom(), npcs["georkina"]!!.left())
            update.update(this).getStartRoom().npcs
        }

        newNPCs.shouldBeEmpty()
    }

    test("Should create Items"){
        val newItems = with(GAME_WITHOUT_PLAYER) {
            val updates = ITEMS.values.map { EntityAction(Create, getStartRoom(), it.right())}
            updates.fold(this){game, upd -> upd.update(game)}.getStartRoom().items
        }

        newItems shouldContainAll listOf(ITEMS.values, ITEMS.values).flatMap(::identity)
    }

    test("Should remove Items"){
        val newItems = with(GAME_WITHOUT_PLAYER) {
            val updates = ITEMS.values.map { EntityAction(Remove, getStartRoom(), it.right())}
            updates.fold(this){game, upd -> upd.update(game)}.getStartRoom().items
        }

        newItems.shouldBeEmpty()
    }

})
