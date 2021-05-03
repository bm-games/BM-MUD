import {BaseConfig} from "./BaseConfig";
import {NPCConfig} from "./NPCConfig";
import {ItemConfig} from "./ItemConfig";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";
import {CommandConfig} from "./CommandConfig";

//export class DungeonConfig extends BaseConfig {
//  private name: string | undefined;
//  private startRoom: number | undefined;
//  private startEquipment: number[] | undefined;
//  // private actionConfig: ActionConfig
//}

export interface DungeonConfig {
  name: string;
  startRoom: number;
  startEquipment: ItemConfig[];
  npcs: NPCConfig[];
  items: ItemConfig[];
  races: RaceConfig[];
  classes: ClassConfig[];
  commands: CommandConfig[];
}
