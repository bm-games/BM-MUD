@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
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
import net.bmgames.authentication.*
import net.bmgames.communication.MailNotifier
import net.bmgames.communication.Notifier
import net.bmgames.state.installConfigEndpoint
import net.bmgames.game.GameManager
import net.bmgames.state.GameRepository
import net.bmgames.game.installGameEndpoint
import java.time.Duration

class Server(val config: ServerConfig) {
    private val mailNotifier by lazy { MailNotifier(config) }
    private val authHelper = AuthHelper(config)
    private val userHandler = UserHandler(mailNotifier, authHelper)

    val notifier: Notifier by lazy { mailNotifier }
    val authenticator = Authenticator(authHelper, userHandler)
    val gameRepository = GameRepository
}

fun Application.installServer(server: Server) {
    installFeatures()
    configureSecurity(server.config)
    configureRoutes(server.authenticator)
}

fun Application.configureRoutes(authenticator: Authenticator) {

    routing {
        route("/api") {
            installAuthEndpoint(authenticator)
            authenticate {
                installConfigEndpoint()
                installGameEndpoint()
            }
        }

        static("") {
            files( "client")
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
        json()
    }

    install(Locations)

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}

fun Application.configureSecurity(config: ServerConfig) {
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        method(HttpMethod.Get)
        method(HttpMethod.Options)
        method(HttpMethod.Post)
    }

    install(Sessions) {
        cookie<User>("UserIdentifier") {
            val secretEncryptKey = config.secretKeyHash
                .subSequence(0, 16)
                .toString()
                .toByteArray()
            val secretAuthKey = config.secretKeyHash
                .subSequence(0, 16)
                .toString()
                .toByteArray()
            cookie.extensions["SameSite"] = "lax"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
        }
    }

    install(Authentication) {
        session<User> {
            validate { session -> session }
            challenge {
                //call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}



