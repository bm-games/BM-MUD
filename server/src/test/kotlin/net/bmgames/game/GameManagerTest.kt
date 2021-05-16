package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import net.bmgames.state.GameRepository

class GameManagerTest : FunSpec({

    beforeTest {
        mockkObject(GameRepository)
    }

    test("Stopped Game should be loaded from database") {
        val gameManager = GameManager(NOOP_NOTIFIER)
        every { GameRepository.loadGame("Test") } returns GAME_WITHOUT_PLAYER
        gameManager.getRunningGames().size shouldBe 0
        gameManager.getGameRunner("Test")
        gameManager.getRunningGames().size shouldBe 1
    }


    afterSpec {
        unmockkAll()
    }
})
