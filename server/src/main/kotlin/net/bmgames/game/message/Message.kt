package net.bmgames.game.message

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class Message {
    @Serializable
    data class Text(val text: String) : Message()

    @Serializable
    data class Chat(
        val sender: String,
        val message: String
    ) : Message()

    @Serializable
    data class Map(val map: RoomMap) : Message()

    @Serializable
    data class Kick(val reason: String) : Message()
}

suspend fun WebSocketServerSession.sendMessage(msg: Message) {
    send(Frame.Text(Json.encodeToString(msg)))
}

