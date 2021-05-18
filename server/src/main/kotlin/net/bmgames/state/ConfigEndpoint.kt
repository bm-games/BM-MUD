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
import net.bmgames.*
import net.bmgames.authentication.User
import net.bmgames.authentication.getUser
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player

internal class ConfigEndpoint {
    fun saveConfig(config: DungeonConfig, user: User): Either<ErrorMessage, Unit> {
        if(config.name.isNullOrEmpty()) return errorMsg("Der Name des MUDs darf nicht leer sein")
        if (GameRepository.getGame(config.name) != null)
            return errorMsg(message("config.game-name-used"))
        if (config.startRoom == "") return errorMsg(message("config.no-starting-room"))
        if (config.rooms.isEmpty()) return errorMsg(message("config.at-least-one-room"))
        if (config.races.isEmpty()) return errorMsg(message("config.at-least-one-race"))
        if (config.classes.isEmpty()) return errorMsg(message("config.at-least-one-class"))

        GameRepository.saveGame(config.run {
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
                allowedUsers = mapOf(user.username to emptySet()),
            )
        })
        return success
    }

    fun getConfig(name: String): DungeonConfig? =
        GameRepository.getGame(name)?.run {
            DungeonConfig(name, races, classes, commandConfig, npcConfigs, itemConfigs, startItems, startRoom, rooms)
        }
}

fun Route.installConfigEndpoint() {
    val configEndpoint = ConfigEndpoint()
    route("/configurator") {
        get<GetConfig> { (name) ->
            configEndpoint.getConfig(name)
                ?.let { config -> call.respond(config) }
                ?: call.respond(HttpStatusCode.BadRequest, message("config.game-not-found"))
        }
        post("/createConfig") {
            either<ErrorMessage, Unit> {
                val user = call.getUser().rightIfNotNull {message("config.not-authenticated")}.bind()
                val configJSON = call.receive<String>().rightIfNotNull {message("config.config-missing")}.bind()
                val config = catch { Json.decodeFromString<DungeonConfig>(configJSON)}
                    .mapLeft {it.printStackTrace(); message("config.missing-values")}
                    .bind()
                configEndpoint.saveConfig(config, user).bind()
            }.acceptOrReject(call)
        }
    }
}

@Location("/get/{name}")
data class GetConfig(val name: String)

