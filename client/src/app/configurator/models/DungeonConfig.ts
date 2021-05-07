import {NPC} from "./NPCConfig";
import {Item} from "./Item";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";
import {CommandConfig} from "./CommandConfig";
import {RoomConfig} from "./RoomConfig";
import {RoomConfigExport} from "../components/configuration/configuration.component";

export interface DungeonConfig {
  name: string;
  startRoom: string;
  startEquipment: Item[];
  rooms: Map<string, RoomConfigExport>;
  npcConfigs: Map<string, NPC>;
  itemConfigs: Map<string, Item>;
  races: RaceConfig[];
  classes: ClassConfig[];
  commandConfig: CommandConfig;
}
