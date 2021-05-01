package net.bmgames.game

import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import net.bmgames.ErrorMessage
import net.bmgames.Main
import net.bmgames.authentication.User
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.configurator.model.DungeonConfig
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.sendMessage
import net.bmgames.game.model.GameOverview


fun Route.installWebSocket(gameManager: GameManager = Main.gameManager) {
    webSocket("/play/{gameName}/{avatar}") {
        either<ErrorMessage, IConnection> {
            val gameName = call.parameters["gameName"].rightIfNotNull { "Missing game name" }.bind()
            val avatar = call.parameters["avatar"].rightIfNotNull { "Missing avatar" }.bind()

            call.sessions.get<User>().rightIfNotNull { "User not authenticated" }.bind()

            val player = PlayerManager.loadPlayer(gameName, avatar).rightIfNotNull { "Player not found" }.bind()
            val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()

            return@either gameRunner.connect(player).bind()
        }.fold(
            { error -> close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, error)) },
            { connection ->
                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            connection.tryQueueCommand(frame.readText())
                                .mapLeft { error -> send(error) }
                        }
                    }
                    for (message in connection.outgoing) {
                        sendMessage(message)
                    }
                } catch (_: Throwable) {
                    connection.close()
                }
            }
        )
    }
}
