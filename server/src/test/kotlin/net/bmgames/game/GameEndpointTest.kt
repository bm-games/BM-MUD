package net.bmgames.game

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import net.bmgames.game.GameOverview.Permission.Yes
import net.bmgames.state.GameRepository

class GameEndpointTest : FunSpec({

    lateinit var gameManager: GameManager
    lateinit var endpoint: GameEndpoint


    beforeSpec {
        mockkObject(GameRepository)
        every { GameRepository.getGame("test") } returns GAME_WITH_PLAYER
        every { GameRepository.getGame("dummy") } returns GAME_WITHOUT_PLAYER
        every { GameRepository.listGames() } returns listOf(GAME_WITHOUT_PLAYER, GAME_WITH_PLAYER)

    }

    beforeTest {
        gameManager = GameManager(NOOP_NOTIFIER)
        endpoint = GameEndpoint(gameManager, NOOP_NOTIFIER)
    }


    test("Should list started and stopped games correctly") {

        val gamesBeforeStart = endpoint.listGames(PLAYER.user)

        gameManager.getGameRunner("test")

        val gamesAfterStart = endpoint.listGames(PLAYER.user)

        gamesBeforeStart shouldContainAll gamesAfterStart
    }


    test("Should list games for user correctly") {

        val game = endpoint.listGames(PLAYER.user)
            .find { it.userPermitted == Yes }
        game.shouldNotBeNull()
        game.avatarCount shouldBe 1
        game.userPermitted shouldBe Yes
    }

    afterSpec {
        unmockkAll()
    }

})


