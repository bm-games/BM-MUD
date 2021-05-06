import {NPCType} from "./NPCType";

export interface NPCConfig {
  name: string;
  type: NPCType;
  items: string[];
  loottable: string[];
}
