@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import net.bmgames.ErrorMessage
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.authentication.withUser
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.sendMessage
import net.bmgames.state.GameRepository
import net.bmgames.state.PlayerRepository

/**
 * The interface for the game logic to the outer world.
 * */
internal class GameEndpoint(
    val gameManager: GameManager,
    val gameRepo: GameRepository
) {

    /**
     * Collects all existing games and if adds information if they're running
     * */
    suspend fun listGames(user: User): List<GameOverview> {
        val runningGames = gameManager.getRunningGames()
        return runningGames
            .map { (_, gameRunner) -> gameRunner.getCurrentGameState() }
            .plus(gameRepo.listGames()
                .filterNot { runningGames.containsKey(it.name) })
            .map {
                GameOverview(
                    it.name,
//                    isStarted = runningGames.containsKey(it.name),
                    onlinePlayers = it.onlinePlayers.size,
                    masterOnline = it.isMasterOnline(),
                    avatarCount = it.users[user.username]?.size ?: 0,
                    userPermitted = it.users.containsKey(user.username),
                )
            }
    }


    /**
     * Connects a [WebSocketServerSession] with an [IConnection]
     * @param socketServerSession The WebSocket session to the client
     * @param connection The connection from the Gamerunner
     * */
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

/**
 * Connects the [GameEndpoint] to Ktor
 * */
fun Route.installGameEndpoint(
    gameRepo: GameRepository = GameRepository,
    gameManager: GameManager = GameManager(gameRepo)
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

                val player = PlayerRepository.loadPlayer(gameName, avatar).rightIfNotNull { "Player not found" }.bind()
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

