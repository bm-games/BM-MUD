package net.bmgames.game.action

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.game.action.RoomAction.Type.Create
import net.bmgames.game.action.RoomAction.Type.Delete
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Direction.SOUTH
import net.bmgames.state.model.Direction.WEST
import net.bmgames.state.model.Room

class RoomActionTest : FunSpec({

    test("Should add room") {
        val newRoom = Room(
            "New room", "",
            neighbours = mapOf(Direction.NORTH to "Next room", Direction.EAST to "Start room")
        )
        val newGame = with(GAME_WITHOUT_PLAYER) {
            RoomAction(Create, newRoom).update(this)
        }
        newGame.getRoom("Next room")!!.neighbours[SOUTH] shouldBe newRoom.name
        newGame.getRoom("Start room")!!.neighbours[WEST] shouldBe newRoom.name
        newGame.getRoom(newRoom.name) shouldBe newRoom
    }

    test("Should remove room") {
        val newGame = with(GAME_WITHOUT_PLAYER) {
            RoomAction(Delete, getRoom("Next room")!!).update(this)
        }
        newGame.rooms shouldHaveSize 1
        newGame.getStartRoom().neighbours.shouldBeEmpty()
    }
})
