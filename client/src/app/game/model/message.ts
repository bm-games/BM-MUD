import {RoomMap} from "../../shared/model/map";

interface TextMessage {
  readonly type: 'text',
  readonly text: string
}

interface MapMessage {
  readonly type: 'map',
  readonly map: RoomMap
}

interface KickMessage {
  readonly type: 'kick',
  readonly reason: string
}

type Message = TextMessage | MapMessage | KickMessage

export {TextMessage, MapMessage, KickMessage, Message}
