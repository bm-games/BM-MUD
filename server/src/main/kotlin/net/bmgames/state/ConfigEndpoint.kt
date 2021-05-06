@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.state

import arrow.core.Either
import arrow.core.rightIfNull
import arrow.core.toOption
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import net.bmgames.ErrorMessage
import net.bmgames.state.model.Game

internal class ConfigEndpoint {
    fun saveConfig(config: Game): Either<ErrorMessage, Unit> = TODO()
    fun getConfig(name: String): Game? = TODO()
}

fun Route.installConfigEndpoint() {
    val configEndpoint = ConfigEndpoint()
    route("/configurator") {
        get<GetConfig> { (name) ->
            configEndpoint.getConfig(name)
                ?.let { config -> call.respond(config) }
                ?: call.respond(HttpStatusCode.BadRequest)
    }
    post<SaveConfig> {
        val config = call.receive<Game>()
        println(config)
        TODO("Validate")
        call.respond(HttpStatusCode.Accepted)
    }
}
}

@Location("/get/{name}")
data class GetConfig(val name: String)

@Location("/create")
class SaveConfig
