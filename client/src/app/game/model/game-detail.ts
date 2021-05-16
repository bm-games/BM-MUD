import {ClassConfig} from "../../configurator/models/ClassConfig";
import {RaceConfig} from "../../configurator/models/RaceConfig";

export interface GameDetail {
  players: PlayerDetail[]
  races: RaceConfig[]
  classes: ClassConfig[]
  isMaster: boolean
}

export interface AvatarConfig {
  name: string
  clazz: ClassConfig
  race: RaceConfig
}

export interface PlayerDetail {
  avatar: AvatarConfig
  maxHealth: number
  health: number
  room: string
}
