package net.bmgames

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmgames.ServerConfig.Companion.initializeConfig
import net.bmgames.authentication.AuthHelper
import net.bmgames.authentication.UserHandler
import net.bmgames.communication.MailNotifier
import net.bmgames.communication.Notifier

import net.bmgames.authentication.Authenticator

object Main {
    lateinit var config: ServerConfig
    val mailNotifier: MailNotifier by lazy { MailNotifier(config) }
    val notifier: Notifier by lazy { mailNotifier }
    val userHandler: UserHandler by lazy { UserHandler(mailNotifier) }
    val Authenticator: Authenticator by lazy { Authenticator(userHandler)}
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
    Main.Authenticator.registerUser("i19002@hb.dhbw-stuttgart.de","Lucock","Randy")
    Main.Authenticator.loginUser("i19002@hb.dhbw-stuttgart.de","Randy")

    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureMonitoring()
    }.start(wait = true)

}
