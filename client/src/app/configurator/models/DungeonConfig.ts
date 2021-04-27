import {BaseConfig} from "./BaseConfig";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";

export class DungeonConfig extends BaseConfig {
  private name: string | undefined;
  private startRoom: number | undefined;
  private startEquipment: number[] | undefined;
  // private actionConfig: ActionConfig
}
