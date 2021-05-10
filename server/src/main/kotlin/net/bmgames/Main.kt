package net.bmgames

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmgames.ServerConfig.Companion.initializeConfig


/**
 * Starts the server.
 * @param args The command line arguments that were passed when executing the runnable.
 * */
fun main(args: Array<String>) {
    if (args.size != 1) {
        errorMsg("Path of config is missing. Please specify it as the first command line argument.")
    }

    val main = initializeConfig(configPath = args[0]).let(::Server)
    main.config.connectToDB()

    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        installServer(main)
    }.start(wait = true)

}


val DEMO_CONFIG = ServerConfig(
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

val DEMO_SERVER = Server(DEMO_CONFIG)
