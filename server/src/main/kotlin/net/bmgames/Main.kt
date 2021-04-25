package net.bmgames

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.utils.io.*
import net.bmgames.ServerConfig.Companion.initializeConfig
import net.bmgames.authentication.User
import net.bmgames.authentication.UserHandler
import net.bmgames.communication.MailNotifier
import net.bmgames.communication.Notifier
import net.bmgames.plugins.*

object Main {
    lateinit var config: ServerConfig
    val mailNotifier: MailNotifier by lazy { MailNotifier(config) }
    val notifier: Notifier by lazy { mailNotifier }
    val userHandler: UserHandler by lazy { UserHandler(mailNotifier) }
}

/**
 * Starts the server.
 * @param args The command line arguments that were passed when executing the runnable.
 * */
suspend fun main(args: Array<String>) {
    if (args.size != 1) {
        error("Path of config is missing. Please specify it as the first command line argument.")
    }

    with(Main) {
        config = initializeConfig(configPath = args[0])
        config.connectToDB()
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureSockets()
    }.start(wait = true)

}
