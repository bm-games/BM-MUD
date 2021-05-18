package net.bmgames

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.computations.either
import arrow.core.filterOrElse
import arrow.core.flatMap
import arrow.core.identity
import net.bmgames.message
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * The secret hash key must have this length.
 * Required by encryption algorithm
 * */
const val SECRET_KET_LENGTH = 32


/**
 * Keep alive ping of a websocket connection
 * */
const val WEB_SOCKETS_PING: Long = 15

/**
 * This Config configures the server
 * @property secretKeyHash Used for encrypting user passwords and sessions.
 * Must have length [SECRET_KET_LENGTH]
 * */
@Serializable
data class ServerConfig(
    val dbHost: String,
    val dbPort: Int,
    val dbName: String,
    val dbUser: String,
    val dbPassword: String,

    val smtpHost: String,
    val smtpPort: Int,
    val emailAddress: String,
    val emailPassword: String,

    val secretKeyHash: String

) {
    companion object {
        internal fun readConfig(path: String): Either<Error, ServerConfig> {
            return catch(
                { Error(message("config.config-not-found" ),it) },
                { File(path).readText() }
            ).flatMap { content ->
                catch(
                    { Error(message("config.cannot-parse-config"), it) },
                    { Json.decodeFromString<ServerConfig>(content) }
                ).filterOrElse(
                    { it.secretKeyHash.length == SECRET_KET_LENGTH },
                    { Error(message("config.wrong-key-length",SECRET_KET_LENGTH)) }
                )
            }
        }

        internal fun ServerConfig.writeConfig(path: String, override: Boolean = false): Boolean =
            File(path).let {
                if (override || !it.exists()) {
                    it.writeText(Json { prettyPrint = true }.encodeToString(this))
                    true
                } else {
                    false
                }
            }

        /**
         * Reads and parses the server config at the specified path
         * @throws Error If config wasn't found or is invalid.
         * In this case a [DEMO_CONFIG] is generated at the specified path, if the file doesn't exist
         * */
        internal fun initializeConfig(configPath: String): ServerConfig =
            readConfig(configPath)
                .fold({ error ->
                    if (DEMO_CONFIG.writeConfig(configPath)) println(message("config.config-generated",configPath))
                    throw error
                }, ::identity)


    }
}
