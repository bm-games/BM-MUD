@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.configurator

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import net.bmgames.configurator.model.CommandConfig
import net.bmgames.configurator.model.DungeonConfig

@Location("/get/{name}")
data class GetConfig(val name: String)

@Location("/create")
class CreateConfig

fun Route.installConfigEndpoint() {
route("/configurator") {
    get<GetConfig> { (name) ->
        call.respond(DungeonConfig(name, CommandConfig()))
    }
    post<CreateConfig> {
        val config = call.receive<DungeonConfig>()
        println(config)
        call.respond(HttpStatusCode.Accepted)
    }
}
}
