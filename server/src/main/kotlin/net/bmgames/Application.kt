package net.bmgames

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmgames.Communication.MailNotifier
import net.bmgames.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureSockets()
    }.start(wait = true)
}
