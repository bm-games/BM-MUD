package net.bmgames.game.message

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import net.bmgames.game.message.Tile.Type.NotVisited
import net.bmgames.game.message.Tile.Type.Visited
import net.bmgames.state.model.Direction
import net.bmgames.state.model.Direction.*
import net.bmgames.state.model.Room

class RoomMapKtTest : FunSpec({

    val allRooms = (0..8).map { i ->
        Room(
            name = i.toString(), message = "",
            neighbours = mutableMapOf<Direction, String>().apply {
                if ((i % 3) == 1 && i > 3) put(NORTH, (i - 3).toString())
                if ((i % 3) == 1 && i < 5) put(SOUTH, (i + 3).toString())
                if (i in 3..4 ) put(EAST, (i + 1).toString())
                if (i in 4..5 ) put(WEST, (i - 1).toString())
            }
        )
    }.associateBy { it.name }

    test("If all rooms were visited, all rooms should be rendered") {
        val visitedRooms = (0..8).map(Int::toString).toHashSet()
        val map = MapBuilder(visitedRooms, { emptySet()}, allRooms::get, false).build(allRooms["4"]!!).tiles
        map[1].filterNotNull() shouldHaveSize 3
        map[2].filterNotNull() shouldHaveSize 1
        map[2][1]?.apply {
            type shouldBe Visited
//            north shouldBe false
//            south shouldBe false
//            east shouldBe false
//            west shouldBe true
        }
    }


    test("Only visited rooms should be rendered") {
        val visitedRooms = (4..8).map(Int::toString).toHashSet()
        val map = MapBuilder(visitedRooms, { emptySet()}, allRooms::get, false).build(allRooms["4"]!!).tiles
        map[1].filterNotNull() shouldHaveSize 3
        map[2].filterNotNull() shouldHaveSize 1
        map[0][1]?.type shouldBe NotVisited
        map[2][1]?.type shouldBe Visited
    }

})
