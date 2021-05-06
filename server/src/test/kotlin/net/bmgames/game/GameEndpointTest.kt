package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import net.bmgames.state.GameRepository

class GameEndpointTest : FunSpec({

    lateinit var gameManager: GameManager
    lateinit var endpoint: GameEndpoint

    beforeSpec {
        mockkObject(GameRepository)
        every { GameRepository.loadGame("test") } returns GAME_WITH_PLAYER
        every { GameRepository.loadGame("dummy") } returns GAME_WITHOUT_PLAYER

        every { GameRepository.listGames() } returns listOf(GAME_WITHOUT_PLAYER, GAME_WITH_PLAYER)
    }

    beforeTest {
        gameManager = GameManager(GameRepository)
        endpoint = GameEndpoint(gameManager, GameRepository)
    }


    test("Should list started and stopped games correctly") {

        val gamesBeforeStart = endpoint.listGames(PLAYER.user)

        gameManager.getGameRunner("test")

        val gamesAfterStart = endpoint.listGames(PLAYER.user)

        gamesBeforeStart shouldContainAll gamesAfterStart
    }


    test("Should list games for user correctly") {

        val game = endpoint.listGames(PLAYER.user)
            .find { it.userPermitted }
        game.shouldNotBeNull()
        game.avatarCount shouldBe 1
        game.userPermitted shouldBe true
    }

})


