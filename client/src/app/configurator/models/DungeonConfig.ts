import {NPCConfig} from "./NPCConfig";
import {ItemConfig} from "./ItemConfig";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";
import {CommandConfig} from "./CommandConfig";
import {RoomConfig} from "./RoomConfig";
import {RoomConfigExport} from "../components/configuration/configuration.component";

export interface DungeonConfig {
  name: string;
  startRoom: number;
  startEquipment: ItemConfig[];
  rooms: Map<string, RoomConfigExport>;
  npcConfigs: Map<string, NPCConfig>;
  itemConfigs: Map<string, ItemConfig>;
  races: RaceConfig[];
  classes: ClassConfig[];
  commandConfig: CommandConfig;
}
