package net.bmgames.game

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import net.bmgames.game.message.Message
import net.bmgames.game.message.sendMessage
import java.time.Duration


const val PERIOD: Long = 15

fun Application.installWebSocketConnection() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(PERIOD)
        timeout = Duration.ofSeconds(PERIOD)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/game/{name}") { // websocketSession
            val name = call.parameters["name"]
            outgoing.sendMessage(Message.Text(name ?: "Name nicht angegeben"))

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    outgoing.sendMessage(Message.Text(text))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        }
    }
}
