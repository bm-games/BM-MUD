@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.game

import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.cio.websocket.CloseReason.Codes.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import net.bmgames.*
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.authentication.withUser
import net.bmgames.communication.Notifier
import net.bmgames.game.GameOverview.Permission
import net.bmgames.game.connection.GameRunner
import net.bmgames.game.connection.IConnection
import net.bmgames.game.message.Message
import net.bmgames.game.message.sendMessage
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
                with(it) {
                    GameOverview(
                        name,
                        description = message(
                            "game.overview.description",
                            master.ingameName,
                            races.joinToString(", ") { race -> race.name },
                            classes.joinToString(", ") { clazz -> clazz.name },
                            rooms.size,
                            if (onlinePlayers.isNotEmpty()) onlinePlayers.keys.joinToString(", ") else "Keiner :("
                        ),
                        isMaster = master.user == user,
                        onlinePlayers = onlinePlayers.size,
                        masterOnline = isMasterOnline(),
                        avatarCount = allowedUsers[user.username]?.size ?: 0,
                        userPermitted = when {
                            allowedUsers.containsKey(user.username) -> Permission.Yes
                            joinRequests.contains(user) -> Permission.Pending
                            else -> Permission.No
                        },
                    )
                }
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
                            .mapLeft { error -> socketServerSession.sendMessage(Message.Text(error)) }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                connection.close(message("message.connection-closed").format(e.message))
            } catch (e: Throwable) {
                connection.close(message("message.connection-closed-error").format(e.message))
            }
            connection.close("Disconnected itself")
        }
        val outgoing = GameScope.launch {
            connection.onClose {
                launch {
                    socketServerSession.sendMessage(Message.Close(it))
                    socketServerSession.close(CloseReason(NORMAL, it))
                }
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
                subject = message("message.want-to-join").format(user.username, name),
                message = message("message.join-request").format(master.ingameName, user.username, name)
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
                visitedRooms = hashSetOf(startRoom)
            ).also {
                PlayerRepository.savePlayer(this, it)
            }
        }
        gameRunner.updateGameState(Game.allowedUsers.modify { users ->
            users.plus(
                user.username to users.getOrDefault(user.username, emptySet()).plus(newPlayer.ingameName)
            )
        })

    }

    /**
     * Stops the game and deletes it from the database
     * */
    suspend fun deleteGame(gameRunner: GameRunner): Unit {
        gameManager.stopGame(gameRunner)
        GameRepository.delete(gameRunner.getCurrentGameState())
    }

    fun getDetail(game: Game, user: User): GameDetail {
        val players = PlayerRepository.getPlayersForUser(user.username, game)
            .map { with(it) { PlayerOverview(avatar, maxHealthPoints, healthPoints, room) } }
        return GameDetail(players, game.races, game.classes, game.master.user == user)
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
        get<GetDetails> { (gameName) ->
            either<ErrorMessage, GameDetail> {
                val user = call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()
                val game = GameRepository.loadGame(gameName).rightIfNotNull { message("message.game-not-found") }.bind()
                endpoint.getDetail(game, user)
            }.acceptOrReject(call)
        }

        post<RequestJoin> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()
                val gameRunner = gameManager.getGameRunner(gameName)
                    .rightIfNotNull { message("message.game-not-found") }.bind()
                guard(gameRunner.getCurrentGameState().joinRequests.contains(user)) {
                    message("message.join-request-sent")
                }
                endpoint.requestJoin(gameRunner, user)
            }.acceptOrReject(call)
        }

        post<CreatePlayer> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()
                val avatar = call.receive<Avatar>().rightIfNotNull { message("message.avatar-not-supplied") }.bind()
                val gameRunner =
                    gameManager.getGameRunner(gameName).rightIfNotNull { message("message.game-not-found") }.bind()
                val avatarExists = gameRunner.getCurrentGameState().allowedUsers
                    .any { (_, avatars) -> avatars.contains(avatar.name) }
                guard(avatarExists) { message("message.avatar-exists") }

                endpoint.createPlayer(gameRunner, user, avatar)
            }.acceptOrReject(call)
        }

        delete<DeleteGame> { (gameName) ->
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()
                val gameRunner =
                    gameManager.getGameRunner(gameName).rightIfNotNull { message("message.game-not-found") }.bind()
                guard(gameRunner.getCurrentGameState().master.user != user) { message("message.not-authorized") }

                endpoint.deleteGame(gameRunner)
            }.acceptOrReject(call)
        }

        webSocket("/play/{gameName}/{avatar}") {
            either<ErrorMessage, IConnection> {
                val gameName =
                    call.parameters["gameName"].rightIfNotNull { message("message.missing-game-name") }.bind()
                val avatar = call.parameters["avatar"].rightIfNotNull { message("message.missing-avatar") }.bind()

                call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()

                val player = PlayerRepository.loadPlayer(gameName, avatar)
                    .rightIfNotNull { message("message.player-not-found") }.bind()
                val gameRunner =
                    gameManager.getGameRunner(gameName).rightIfNotNull { message("message.game-not-found") }.bind()

                gameRunner.connect(player).bind()
            }.fold(
                { error -> close(CloseReason(PROTOCOL_ERROR, error)) },
                { connection -> endpoint.joinGame(this, connection) }
            )
        }


        webSocket("/control/{gameName}") {
            either<ErrorMessage, IConnection> {
                val gameName =
                    call.parameters["gameName"].rightIfNotNull { message("message.missing-game-name") }.bind()

                val user = call.getUser().rightIfNotNull { message("message.user-not-authenticated") }.bind()

                val gameRunner = gameManager.getGameRunner(gameName)
                    .rightIfNotNull { message("message.game-not-found") }.bind()

                guard(gameRunner.getCurrentGameState().master.user != user) {
                    message("game.connection.not-master")
                }

                gameRunner.connect(Player.Master(user)).bind()
            }.fold(
                { error -> close(CloseReason(PROTOCOL_ERROR, error)) },
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

@Location("/detail/{gameName}")
data class GetDetails(val gameName: String)

