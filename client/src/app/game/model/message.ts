import {RoomMap} from "../../shared/model/map";

interface TextMessage {
  readonly type: 'net.bmgames.game.message.Message.Text',
  readonly text: string
}

interface MapMessage {
  readonly type: 'net.bmgames.game.message.Message.Map',
  readonly map: RoomMap
}

interface KickMessage {
  readonly type: 'net.bmgames.game.message.Message.Kick',
  readonly reason: string
}

interface ChatMessage {
  readonly type: 'net.bmgames.game.message.Message.Chat',
  readonly sender: string
  readonly message: string
}

type Message = TextMessage | MapMessage | KickMessage | ChatMessage

export {TextMessage, MapMessage, KickMessage, ChatMessage, Message}
