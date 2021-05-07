import {NPCType} from "./NPCType";
import {ItemConfig} from "./ItemConfig";

export interface NPCConfig {
  name: string;
  type: NPCType;
  items: ItemConfig[];
  loottable: string[];
}
