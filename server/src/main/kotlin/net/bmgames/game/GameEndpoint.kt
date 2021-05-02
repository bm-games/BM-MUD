@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.configurator.model.DungeonConfig
import net.bmgames.game.model.GameOverview

@Location("/game/availableGames")
class AvailableGames

fun Application.installGameEndpoint() {
    installWebSocketConnection()

    routing {
        get<AvailableGames> {
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
