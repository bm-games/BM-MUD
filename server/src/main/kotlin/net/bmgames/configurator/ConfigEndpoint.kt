@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.configurator

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import net.bmgames.configurator.model.DungeonConfig

@Location("/configurator/get/{name}")
data class GetConfig(val name: String)

@Location("/configurator/create")
class CreateConfig

fun Application.installConfigEndpoint() {
    routing {
        get<GetConfig> { (name) ->
            call.respond(DungeonConfig(name))
        }
        post<CreateConfig> {
            val config = call.receive<DungeonConfig>()
            println(config)
        }
    }
}
