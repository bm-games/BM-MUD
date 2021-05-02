@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames

import io.ktor.application.*
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
import io.ktor.util.*
import io.ktor.websocket.*
import net.bmgames.authentication.User
import net.bmgames.configurator.installConfigEndpoint
import net.bmgames.game.installGameEndpoint
import java.time.Duration


fun Application.installServer(config: ServerConfig) {
    installFeatures()
    configureSecurity(config)
    configureRoutes()
}

fun Application.configureRoutes() {

    routing {
        static("") {
            files("client")
        }

        get("/") {
            call.respondRedirect("/index.html")
        }

        route("/api") {
            installConfigEndpoint()
            installGameEndpoint()
        }
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
            val secretEncryptKey = hex("00112233445566778899aabbccddeeff") //replace with a value from config.json
            val secretAuthKey = hex("02030405060708090a0b0c") //replace with a value from config.json
            cookie.extensions["SameSite"] = "lax"
            cookie.httpOnly = true
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
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



