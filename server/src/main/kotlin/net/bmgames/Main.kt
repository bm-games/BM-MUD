package net.bmgames

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmgames.ServerConfig.Companion.initializeConfig
import java.util.concurrent.TimeUnit


/**
 * Starts the server.
 * @param args The command line arguments that were passed when executing the runnable.
 * */
fun main(args: Array<String>) {
    if (args.size != 1) {
        errorMsg(message("config.path-missing"))
    }

    val mudServer = initializeConfig(configPath = args[0]).let(::Server)
    mudServer.config.connectToDB()

    val netty = embeddedServer(Netty) { installServer(mudServer) }
        .start(false)

    Runtime.getRuntime().addShutdownHook(Thread {
        println(message("config.shutdown"))
        netty.stop(
            gracePeriod = 1,
            timeout = 15,
            timeUnit = TimeUnit.SECONDS
        )
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
