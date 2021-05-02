@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import net.bmgames.ErrorMessage
import net.bmgames.Main
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.sendMessage
import net.bmgames.game.model.GameOverview
import net.bmgames.game.state.isMasterOnline
import net.bmgames.withUser

internal class GameEndpoint(
    val gameManager: GameManager,
    val gameRepo: GameRepository
) {
    suspend fun listGames(user: User): List<GameOverview> {
        val runningGames = gameManager.getRunningGames()
        return runningGames
            .map { (_, gameRunner) -> gameRunner.getCurrentGameState() }
            .plus(gameRepo.listGames()
                .filterNot { runningGames.containsKey(it.name) })
            .map {
                GameOverview(
                    it.config,
//                    isStarted = runningGames.containsKey(it.name),
                    onlinePlayers = it.onlinePlayers.size,
                    masterOnline = it.isMasterOnline(),
                    avatarCount = it.users[user.username]?.size ?: 0,
                    userPermitted = it.users.containsKey(user.username),
                )
            }
    }

    suspend fun joinGame(socketServerSession: WebSocketServerSession, connection: IConnection) {
        GameScope.launch {
            try {
                for (frame in socketServerSession.incoming) {
                    if (frame is Frame.Text) {
                        connection.tryQueueCommand(frame.readText())
                            .mapLeft { error -> socketServerSession.send(error) }
                    }
                }
            } catch (_: Throwable) {
                connection.close()
            }
        }
        for (message in connection.outgoing) {
            socketServerSession.sendMessage(message)
        }
    }
}

fun Route.installGameEndpoint(
    gameManager: GameManager = Main.gameManager,
    gameRepo: GameRepository = GameRepository
) {
    val endpoint = GameEndpoint(gameManager, gameRepo)

    route("/game") {

        get("/list") {
            call.withUser {
                call.respond(endpoint.listGames(this))
            }
        }

        post<RequestJoin> { (gameName) ->
            TODO()
        }

        post<CreatePlayer> { (gameName) ->
            TODO()
        }

        webSocket("/play/{gameName}/{avatar}") {
            either<ErrorMessage, IConnection> {
                val gameName = call.parameters["gameName"].rightIfNotNull { "Missing game name" }.bind()
                val avatar = call.parameters["avatar"].rightIfNotNull { "Missing avatar" }.bind()

                call.getUser().rightIfNotNull { "User not authenticated" }.bind()

                val player = PlayerManager.loadPlayer(gameName, avatar).rightIfNotNull { "Player not found" }.bind()
                val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()

                gameRunner.connect(player).bind()
            }.fold(
                { error -> close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, error)) },
                { connection -> endpoint.joinGame(this, connection) }
            )
        }

        /*post<JoinGame> { (gameName, avatar) ->
            either {
                val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()
                val  = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()
            }
        }*/
    }
}

@Location("/join/{gameName}")
data class RequestJoin(val gameName: String)

@Location("/create/{gameName}")
data class CreatePlayer(val gameName: String)
