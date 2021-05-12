@file:OptIn(KtorExperimentalLocationsAPI::class)

package net.bmgames.state

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.bmgames.ErrorMessage
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.errorMsg
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.success

internal class ConfigEndpoint {
    fun saveConfig(config: DungeonConfig, user: User): Either<ErrorMessage, Unit> {
        if(config.name.isNullOrEmpty()) return errorMsg("Der Name des MUDs darf nicht leer sein")
        if (GameRepository.loadGame(config.name) != null)
            return errorMsg("Ein Spiel mit diesem Namen existiert bereits")
        if (config.startRoom == "") return errorMsg("Es wurde kein Startraum festgelegt")
        if (config.rooms.isEmpty()) return errorMsg("Es muss mindestens ein Raum erstellt werden")
        if (config.races.isEmpty()) return errorMsg("Es muss mindestens eine Rasse definiert werden")
        if (config.classes.isEmpty()) return errorMsg("Es muss mindestens eine Klasse erstellt werden")

        GameRepository.save(config.run {
            Game(
                name = name,
                races = races,
                classes = classes,
                commandConfig = commandConfig,
                startItems = startEquipment,
                npcConfigs = npcConfigs,
                itemConfigs = itemConfigs,
                startRoom = startRoom,
                rooms = rooms,
                master = Player.Master(user),
                allowedUsers = mapOf(user.username to emptyList()),
            )
        })
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
                val user = call.getUser().rightIfNotNull { "Not authenticated" }.bind()
                val configJSON = call.receive<String>().rightIfNotNull { "Config missing" }.bind()
                val config = catch { Json.decodeFromString<DungeonConfig>(configJSON)}
                    .mapLeft { "Please enter the missing values." }
                    .bind()
                configEndpoint.saveConfig(config, user).bind()
            }.fold(
                { error -> call.respond(HttpStatusCode.BadRequest, error) },
                { call.respond(HttpStatusCode.Accepted) }
            )
        }
    }
}

@Location("/get/{name}")
data class GetConfig(val name: String)

