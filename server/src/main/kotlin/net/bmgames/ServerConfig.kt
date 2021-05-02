package net.bmgames

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.computations.either
import arrow.core.identity
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * This Config configures the server
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
        internal suspend fun readConfig(path: String): Either<Error, ServerConfig> =
            either {
                val content = catch(
                    { Error("Couldn't find config file.", it) },
                    { File(path).readText() }
                ).bind()
                catch(
                    { Error("Couldn't parse config.", it) },
                    { Json.decodeFromString<ServerConfig>(content) }
                ).bind()
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
        internal suspend fun initializeConfig(configPath: String): ServerConfig =
            readConfig(configPath)
                .fold({ error ->
                    if (DEMO_CONFIG.writeConfig(configPath)) println("A demo config was generated at $configPath.")
                    throw error
                }, ::identity)


    }
}

internal val DEMO_CONFIG = ServerConfig(
    dbHost = "localhost",
    dbPort = 5432,
    dbName = "postgres",
    dbUser = "user",
    dbPassword = "password",
    smtpHost = "smtp.mail.com",
    smtpPort = 25,
    emailAddress = "info@bm-games.net",
    emailPassword = "password",
    secretKeyHash = "YYiSB7kY5Ed5mttaJRSkgHEPF43iLjTA"
)

