@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import arrow.core.computations.either
import arrow.core.invalid
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch
import net.bmgames.ErrorMessage
import net.bmgames.acceptOrReject
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.authentication.withUser
import net.bmgames.communication.Notifier
import net.bmgames.game.GameOverview.Permission
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.sendMessage
import net.bmgames.guard
import net.bmgames.modify
import net.bmgames.state.GameRepository
import net.bmgames.state.PlayerRepository
import net.bmgames.state.model.*

/**
 * The interface for the game logic to the outer world.
 * */
internal class GameEndpoint(
    private val gameManager: GameManager,
    private val notifier: Notifier
) {

    /**
     * Collects all existing games and if adds information if they're running
     * */
    suspend fun listGames(user: User): List<GameOverview> {
        val runningGames = gameManager.getRunningGames()
        return runningGames
            .map { (_, gameRunner) -> gameRunner.getCurrentGameState() }
            .plus(GameRepository.listGames()
                .filterNot { runningGames.containsKey(it.name) })
            .map {
                GameOverview(
                    it.name,
                    isMaster = it.master.user == user,
                    onlinePlayers = it.onlinePlayers.size,
                    masterOnline = it.isMasterOnline(),
                    avatarCount = it.allowedUsers[user.username]?.size ?: 0,
                    userPermitted = when {
                        it.allowedUsers.containsKey(user.username) -> Permission.Yes
                        it.joinRequests.contains(user) -> Permission.Pending
                        else -> Permission.No
                    },
                )
            }
    }


    /**
     * Connects a [WebSocketServerSession] with an [IConnection] and waits until the connection is closed.
     * @param socketServerSession The WebSocket session to the client
     * @param connection The connection from the Gamerunner
     * */
    suspend fun joinGame(socketServerSession: WebSocketServerSession, connection: IConnection) {
        val incoming = GameScope.launch {
            try {
                for (frame in socketServerSession.incoming) {
                    if (frame is Frame.Text) {
                        connection.tryQueueCommand(frame.readText())
                            .mapLeft { error -> socketServerSession.send(error) }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                connection.close("The connection was closed. Reason: ${e.message}")
            } catch (e: Throwable) {
                connection.close("The connection was closed by an error. Reason: ${e.message}")
            }
        }
        val outgoing = GameScope.launch {
            connection.onClose {
                launch { socketServerSession.close(CloseReason(CloseReason.Codes.GOING_AWAY, it)) }
            }
            for (message in connection.outgoing) {
                socketServerSession.sendMessage(message)
            }
        }
        incoming.join()
        outgoing.join()
    }

    /**
     * Adds a join request to the game and notifies the master
     * */
    suspend fun requestJoin(gameRunner: GameRunner, user: User) {
        gameRunner.updateGameState(Game.joinRequests.modify { it + user })
        with(gameRunner.getCurrentGameState()) {
            notifier.send(
                recipient = master.user,
                subject = "${user.username} wants to join $name",
                message = """Hi ${master.ingameName},
                |player ${user.username} wants to join your MUD $name!
                |Accept or reject his request in your master console.
            """.trimMargin()
            )
        }
    }

    /**
     * Creates an initial player from the supplied avatar, adds it to the database and to the game
     * */
    suspend fun createPlayer(gameRunner: GameRunner, user: User, avatar: Avatar): Unit {
        val newPlayer = gameRunner.getCurrentGameState().run {
            Player.Normal(
                user,
                avatar,
                Inventory(items = startItems),
                room = startRoom,
                healthPoints = avatar.maxHealth,
                lastHit = null,
                visitedRooms = setOf(startRoom)
            ).also {
                PlayerRepository.savePlayer(this, it)
            }
        }
        gameRunner.updateGameState(Game.allowedUsers.modify { users ->
            users.plus(
                user.username to users.getOrDefault(user.username, emptyList()).plus(newPlayer.ingameName)
            )
        })

    }

    /**
     * Stops the game and deletes it from the database
     * */
    suspend fun deleteGame(gameRunner: GameRunner): Unit {
        gameManager.stopGame(gameRunner)
        GameRepository.delete(gameRunner.name)
    }

}

/**
 * Connects the [GameEndpoint] to Ktor
 * */
fun Route.installGameEndpoint(
    gameManager: GameManager,
    notifier: Notifier
) {
    val endpoint = GameEndpoint(gameManager, notifier)

    route("/game") {

        get("/list") {
            call.withUser {
                call.respond(endpoint.listGames(this))
            }
        }

        post<RequestJoin> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { "User not authenticated" }.bind()
                val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()
                guard(gameRunner.getCurrentGameState().joinRequests.contains(user)) { "Already sent join request" }
                endpoint.requestJoin(gameRunner, user)
            }.acceptOrReject(call)
        }

        post<CreatePlayer> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { "User not authenticated" }.bind()
                val avatar = call.receive<Avatar>().rightIfNotNull { "Avatar not supplied" }.bind()
                val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()
                val avatarExists = gameRunner.getCurrentGameState().allowedUsers
                    .any { (_, avatars) -> avatars.contains(avatar.name) }
                guard(avatarExists) { "An avatar with this name already exists" }

                endpoint.createPlayer(gameRunner, user, avatar)
            }.acceptOrReject(call)
        }

        delete<DeleteGame> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { "User not authenticated" }.bind()
                val gameRunner = gameManager.getGameRunner(gameName).rightIfNotNull { "Game not found" }.bind()
                guard(gameRunner.getCurrentGameState().master.user != user) { "Not authorized" }

                endpoint.deleteGame(gameRunner)
            }.acceptOrReject(call)
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

    }
}

@Location("/join/{gameName}")
data class RequestJoin(val gameName: String)

@Location("/create/{gameName}")
data class CreatePlayer(val gameName: String)

@Location("/delete/{gameName}")
data class DeleteGame(val gameName: String)

