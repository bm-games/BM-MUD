@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.webjars.*
import net.bmgames.configurator.installConfigEndpoint
import net.bmgames.game.installGameEndpoint
import java.time.ZoneId

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    install(Locations)

    routing {
        static("") {
            files("client")
        }

        get("/"){
            call.respondRedirect("/index.html")
        }
    }

    installConfigEndpoint()
    installGameEndpoint()

    /*routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
    }*/
}

