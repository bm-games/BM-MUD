package net.bmgames.game.message

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.bmgames.game.commands.getRoom
import net.bmgames.state.model.Game
import net.bmgames.state.model.Player
import net.bmgames.state.model.Room

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
    data class Map(val map: RoomMap) : Message() {
        constructor(game: Game, player: Player) : this(
            MapBuilder(
                game,
                if (player is Player.Normal) player.visitedRooms else emptySet(),
                player
            ).build(
                if (player is Player.Normal)
                    game.getRoom(player.room) ?: game.getStartRoom()
                else game.getStartRoom()
            )
        )
    }

    @Serializable
    data class Close(val reason: String) : Message()

}

suspend fun WebSocketServerSession.sendMessage(msg: Message) {
    send(Frame.Text(Json.encodeToString(msg)))
}

