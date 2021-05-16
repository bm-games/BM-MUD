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
        error(message("config.path-missing"))
    }

    val server = initializeConfig(configPath = args[0]).let(::Server)
    server.config.connectToDB()

    embeddedServer(Netty) { installServer(server) }
        .start(false)

    Runtime.getRuntime().addShutdownHook(Thread {
        println(message("config.shutdown"))
        server.stop()
    })
    Thread.currentThread().join()

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
