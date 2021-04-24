package net.bmgames.game.message

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.game.model.RoomMap

@Serializable
sealed class Message {
    @Serializable
    data class Text(val text: String) : Message()
    @Serializable
    data class Map(val map: RoomMap) : Message()
    @Serializable
    data class Kick(val reason: String) : Message()
}

suspend fun SendChannel<Frame>.sendMessage(msg: Message) {
    send(Frame.Text(Json.encodeToString(msg)))
}

