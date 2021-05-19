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
import net.bmgames.game.NOOP_NOTIFIER
import net.bmgames.message

class GameRunnerTest : FunSpec({

    test("Allowed player should be able to join") {
        val runner = GameRunner(GAME_WITH_PLAYER, NOOP_NOTIFIER)
        val connection = runner.connect(PLAYER)
        connection.shouldBeRight()
    }

    test("Uninvited player should not be able to join") {
        val runner = GameRunner(GAME_WITHOUT_PLAYER, NOOP_NOTIFIER)
        val connection = runner.connect(PLAYER)
        connection.shouldBeLeft()
        connection.value shouldBe message("game.not-invited")
    }


    test("Connected player should close other session") {
        val runner = GameRunner(GAME_WITH_PLAYER, NOOP_NOTIFIER)
        runner.connect(PLAYER).shouldBeRight()
        runner.connect(PLAYER).shouldBeRight()
    }

})
