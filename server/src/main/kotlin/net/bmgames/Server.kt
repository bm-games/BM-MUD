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
import io.ktor.websocket.*
import net.bmgames.configurator.installConfigEndpoint
import net.bmgames.game.installGameEndpoint
import java.time.Duration


fun Application.installServer() {
    installFeatures()
    configureSecurity()
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

fun Application.configureSecurity() {
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        method(HttpMethod.Get)
        method(HttpMethod.Options)
        method(HttpMethod.Post)
    }

    install(Sessions) {
        //TODO
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



