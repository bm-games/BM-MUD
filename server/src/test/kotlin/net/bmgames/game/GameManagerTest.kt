package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import net.bmgames.game.state.Game

class GameManagerTest : FunSpec({

    val db: GameRepository = mockk()

    test("Stopped Game should be loaded from database") {
        val gameManager = GameManager(db)
        every { db.loadGame("Test") } returns GAME_WITHOUT_PLAYER
        gameManager.getRunningGames().size shouldBe 0
        gameManager.getGameRunner("Test")
        gameManager.getRunningGames().size shouldBe 1
    }

    test("Config should be converted correctly into a game") {
        val gameManager = GameManager(db)
        every { db.save(any()) } just runs
        gameManager.createGame(GAME_WITHOUT_PLAYER.config, master.user)
        verify {
            db.save(
                Game(
                    GAME_WITHOUT_PLAYER.config,
                    GAME_WITHOUT_PLAYER.master,
                    users = mapOf(GAME_WITHOUT_PLAYER.master.ingameName to emptyList()),
                    onlinePlayers = emptyMap()
                )
            )
        }
    }
})
