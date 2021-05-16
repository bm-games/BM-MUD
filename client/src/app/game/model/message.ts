import {RoomMap} from "../../shared/model/map";

interface TextMessage {
  readonly type: 'net.bmgames.game.message.Message.Text',
  readonly text: string
}

interface CloseMessage {
  readonly type: 'net.bmgames.game.message.Message.Close',
  readonly reason: string
}

interface MapMessage {
  readonly type: 'net.bmgames.game.message.Message.Map',
  readonly map: RoomMap
}

interface ChatMessage {
  readonly type: 'net.bmgames.game.message.Message.Chat',
  readonly sender: string
  readonly message: string
}

type Message = TextMessage | MapMessage | ChatMessage | CloseMessage

export {TextMessage, MapMessage, ChatMessage, CloseMessage, Message}
