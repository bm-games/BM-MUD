import {NPC} from "./NPCConfig";
import {Item} from "./Item";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";
import {CommandConfig} from "./CommandConfig";
import {RoomConfigExport} from "../components/configuration/configuration.component";

export interface DungeonConfig {
  name: string;
  startRoom: string;
  startEquipment: Item[];
  rooms: StringMap<RoomConfigExport>;
  npcConfigs: StringMap<NPC>;
  itemConfigs: StringMap<Item>;
  races: RaceConfig[];
  classes: ClassConfig[];
  commandConfig: CommandConfig;
}


export type StringMap<V> = {
  [key: string]: V
}
