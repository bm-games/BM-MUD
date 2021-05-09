package net.bmgames.game

import io.kotest.assertions.fail
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.bmgames.state.GameRepository
import net.bmgames.state.PlayerRepository
import net.bmgames.withAuthenticatedTestApplication

class GameEndpointKtTest : FunSpec({

    beforeSpec {
        mockkObject(GameRepository)
        every { GameRepository.loadGame(GAME_WITH_PLAYER.name) } returns GAME_WITH_PLAYER
        every { GameRepository.loadGame(GAME_WITHOUT_PLAYER.name) } returns GAME_WITHOUT_PLAYER

        every { GameRepository.listGames() } returns listOf(GAME_WITHOUT_PLAYER, GAME_WITH_PLAYER)

        mockkObject(PlayerRepository)
        coEvery { PlayerRepository.loadPlayer(GAME_WITH_PLAYER.name, PLAYER.ingameName) } returns PLAYER
    }

    test("Should connect websocket successfully") {
        withAuthenticatedTestApplication(PLAYER.user) {
            handleWebSocketConversation("/api/game/play/${GAME_WITH_PLAYER.name}/${PLAYER.ingameName}")
            { incoming, _ ->
                for (msg in incoming) {
                    if (msg is Frame.Text) {
                        msg.shouldBeTypeOf<Frame.Text>()
                            .readText() shouldContainIgnoringCase "Welcome"
                        return@handleWebSocketConversation
                    }
                }
                fail("Expected welcome message")
            }

        }
    }

    test("Should list games") {
        withAuthenticatedTestApplication(PLAYER.user) {
            handleRequest(HttpMethod.Get, "/api/game/list").apply {
                response shouldHaveStatus 200
                val games = response.content?.let {
                    Json.decodeFromString<List<GameOverview>>(it)
                        .map { it.name }
                }
                games.shouldNotBeNull()
                games shouldContainAll listOf(GAME_WITHOUT_PLAYER.name, GAME_WITH_PLAYER.name)
            }
        }
    }
})


