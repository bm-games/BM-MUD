package net.bmgames

import net.bmgames.authentication.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.bmgames.Communication.MailNotifier
import net.bmgames.plugins.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
            configureRouting()
            configureSecurity()
            configureMonitoring()
            configureSerialization()
            configureSockets()
        }.start(wait = true)

    }

