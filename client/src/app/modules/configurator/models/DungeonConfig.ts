import {BaseConfig} from "./BaseConfig";
import {RaceConfig} from "./RaceConfig";
import {ClassConfig} from "./ClassConfig";

export class DungeonConfig extends BaseConfig {
  private name: string | undefined;
  private startRoom: number | undefined;
  private startEquipment: number[] | undefined;
  // private actionConfig: ActionConfig
  private static _allRaces: RaceConfig[] = [];
  private static _allClasses: ClassConfig[] = [];

  static set allRaces(value: RaceConfig[]) {
    this._allRaces = value;
  }
  static get allRaces(): RaceConfig[] {
    return this._allRaces;
  }

  static get allClasses(): ClassConfig[] {
    return this._allClasses;
  }

  static set allClasses(value: ClassConfig[]) {
    this._allClasses = value;
  }
}
