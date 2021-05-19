@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.features.CORS.Configuration.Companion.CorsSimpleRequestHeaders
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.bmgames.authentication.*
import net.bmgames.communication.MailNotifier
import net.bmgames.communication.Notifier
import net.bmgames.game.GameManager
import net.bmgames.game.installGameEndpoint
import net.bmgames.state.installConfigEndpoint
import java.time.Duration


class Server(val config: ServerConfig) {
    private val mailNotifier by lazy { MailNotifier(config) }

    private val authHelper = AuthHelper(config)
    private val userHandler = UserHandler(mailNotifier, authHelper)
    val notifier: Notifier by lazy { mailNotifier }

    val authenticator = Authenticator(authHelper, userHandler)
    val gameManager = GameManager(notifier)

    fun stop() {
        with(gameManager) {
            runBlocking {
                getRunningGames().forEach { (_, game) -> stopGame(game) }
            }
        }
    }
}

fun Application.installServer(server: Server) {
    installFeatures()
    configureSecurity(server.config)
    configureRoutes(server)
}

fun Application.configureRoutes(server: Server) {
    routing {
        route("/api") {
            installAuthEndpoint(server.authenticator)
            authenticate {
                installConfigEndpoint()
                installGameEndpoint(server.gameManager, server.notifier)
            }
        }

        static("") {
            files("client")
        }
        get("/") {
            call.respondRedirect("/index.html")
        }
    }

}

fun Application.installFeatures() {
    install(CallLogging) {
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            allowStructuredMapKeys = true
            prettyPrint = false
            useArrayPolymorphism = false
        })
    }

    install(Locations)

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(WEB_SOCKETS_PING)
        timeout = Duration.ofSeconds(WEB_SOCKETS_PING)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}

fun Application.configureSecurity(config: ServerConfig) {
    install(CORS) {
        allowCredentials = true
        allowNonSimpleContentTypes = true
        CorsSimpleRequestHeaders.forEach(::header)
        method(HttpMethod.Delete)
        header("Cookie")

        host("localhost:4200")
        host("25.30.124.39:4200")
        host("192.168.178.85:4200")
        host("bm-games.net", subDomains = listOf("play"))
    }

    install(Sessions) {
        cookie<User>("UserIdentifier") {
            val secretEncryptKey = config.secretKeyHash
                .subSequence(0, SECRET_KEY_LENGTH / 2)
                .toString()
                .toByteArray()
            val secretAuthKey = config.secretKeyHash
                .subSequence(0, SECRET_KEY_LENGTH / 2)
                .toString()
                .toByteArray()

//            cookie.extensions["SameSite"] = "Lax"
//            cookie.secure = true
            cookie.httpOnly = true
//            cookie.domain = "localhost" //TODO replace with domain for prod
            cookie.domain = "25.30.124.39" //TODO replace with domain for prod
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
        }
    }

    install(Authentication) {
        session<User> {
            validate { session -> session }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}



