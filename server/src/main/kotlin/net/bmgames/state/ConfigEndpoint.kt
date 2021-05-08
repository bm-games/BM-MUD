@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.state

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
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
import net.bmgames.state.model.Item
import net.bmgames.success
import kotlin.reflect.typeOf

internal class ConfigEndpoint {
    fun saveConfig(config: DungeonConfig): Either<ErrorMessage, Unit> {
        //TODO check if a config with this name exists already (in GameRepository)
        if(config.startRoom == "") error("Es wurde kein Startraum festgelegt")
        if(config.rooms.isEmpty()) error("Es muss mindestens ein Raum erstellt werden")
        if(config.races.isEmpty()) error("Es muss mindestens eine Rasse definiert werden")
        if(config.classes.isEmpty()) error("Es muss mindestens eine Klasse erstellt werden")
        return success
    }

    fun getConfig(name: String): DungeonConfig? = TODO()
}

fun Route.installConfigEndpoint() {
    val configEndpoint = ConfigEndpoint()
    route("/configurator") {
        get<GetConfig> { (name) ->
            configEndpoint.getConfig(name)
                ?.let { config -> call.respond(config) }
                ?: call.respond(HttpStatusCode.BadRequest)
        }
        post("/createConfig") {
            either<ErrorMessage, Unit> {
                val config = call.receive<DungeonConfig>().rightIfNotNull { "Config missing" }.bind()
                println(config)
                configEndpoint.saveConfig(config).bind()
            }.fold(
                { error -> call.respond(HttpStatusCode.BadRequest, error) },
                { call.respond(HttpStatusCode.Accepted) }
            )
        }
    }
}

@Location("/get/{name}")
data class GetConfig(val name: String)

