package net.bmgames.game.connection

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.types.shouldBeTypeOf
import net.bmgames.game.GAME_WITH_PLAYER
import net.bmgames.game.PLAYER
import net.bmgames.game.GAME_WITHOUT_PLAYER
import net.bmgames.message

class GameRunnerTest : FunSpec({

    test("Allowed player should be able to join") {
        val runner = GameRunner(GAME_WITH_PLAYER)
        val connection = runner.connect(PLAYER)
        connection.shouldBeRight()
    }

    test("Uninvited player should not be able to join") {
        val runner = GameRunner(GAME_WITHOUT_PLAYER)
        val connection = runner.connect(PLAYER)
        connection.shouldBeLeft()
        connection.value shouldBe message("game.not-invited")
    }


    test("Connected player should not join a second time") {
        val runner = GameRunner(GAME_WITH_PLAYER)
        runner.connect(PLAYER).shouldBeRight()
        val invalid = runner.connect(PLAYER)
        invalid.shouldBeLeft()
        invalid.value shouldBe message("game.player-already-connected", PLAYER.ingameName)
    }

})
