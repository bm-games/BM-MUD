package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import net.bmgames.game.state.Game

class GameManagerTest : FunSpec({

    lateinit var db: GamePersistenceManager

    beforeTest {
        db = mockk()
    }

    test("Stopped Game should be loaded from database") {
        val gameManager = GameManager(db)
        every { db.loadGame("Test") } returns Game("Test")
        gameManager.getGame("Test")?.name shouldBe "Test"
    }

    test("All loaded games should be cached") {
        val gameManager = GameManager(db)
        every { db.loadGame(any()) } answers { Game(it.invocation.args[0] as String) }
        gameManager.getGame("Test")
        gameManager.getGame("Test1")
        gameManager.getGame("Test2")

        gameManager.getGames().map(Game::name) shouldContainAll (listOf("Test", "Test1", "Test2"))
    }
})
