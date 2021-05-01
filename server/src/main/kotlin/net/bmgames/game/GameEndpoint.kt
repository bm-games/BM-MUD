@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.cio.websocket.CloseReason.Codes.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.ErrorMessage
import net.bmgames.Main
import net.bmgames.authentication.User
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.configurator.model.DungeonConfig
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.sendMessage
import net.bmgames.game.model.GameOverview


fun Route.installGameEndpoint(gameManager: GameManager = Main.gameManager) {
    route("/game") {
        installWebSocket(gameManager)

        get("/list") {
            call.respond(
                listOf(
                    GameOverview(
                        config = DungeonConfig("Test Config", CommandConfig()),
                        onlinePlayers = 12,
                        masterOnline = true,
                        avatarCount = 2,
                        userPermitted = false,
                    ),
                    GameOverview(
                        config = DungeonConfig("Test_2", CommandConfig()),
                        onlinePlayers = 100,
                        masterOnline = false,
                        avatarCount = 1,
                        userPermitted = true,
                    )
                )
            )
        }
    }
}

@Location("/play/{gameName}/{avatar}")
data class Play(val gameName: String, val avatar: String)
