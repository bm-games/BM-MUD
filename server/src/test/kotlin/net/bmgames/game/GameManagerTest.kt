package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import net.bmgames.state.GameRepository
import net.bmgames.state.model.Game

class GameManagerTest : FunSpec({

    val db: GameRepository = mockk()

    test("Stopped Game should be loaded from database") {
        val gameManager = GameManager(db)
        every { db.loadGame("Test") } returns GAME_WITHOUT_PLAYER
        gameManager.getRunningGames().size shouldBe 0
        gameManager.getGameRunner("Test")
        gameManager.getRunningGames().size shouldBe 1
    }

})
