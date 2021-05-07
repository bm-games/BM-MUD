import {BaseConfig} from "./BaseConfig";
import {ItemConfig} from "./ItemConfig";

//export class DungeonConfig extends BaseConfig {
//  private name: string | undefined;
//  private startRoom: number | undefined;
//  private startEquipment: number[] | undefined;
//  // private actionConfig: ActionConfig
//}

export interface DungeonConfig extends BaseConfig{
  name: string | undefined;
  startRoom: number | undefined;
  startEquipment: ItemConfig[];
  // actionConfig: ActionConfig
}
