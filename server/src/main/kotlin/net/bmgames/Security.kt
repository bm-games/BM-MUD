package net.bmgames

import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import net.bmgames.authentication.User

fun Application.configureSecurity() {
    install(CORS){
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

    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}

data class MySession(val count: Int = 0)
