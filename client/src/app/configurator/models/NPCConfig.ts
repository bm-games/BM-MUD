import {NPCType} from "./NPCType";
import {Item} from "./Item";
import {FriendlyNPCConfig} from "./FriendlyNPCConfig";
import {HostileNPCConfig} from "./HostileNPCConfig";

/*export interface NPCConfig {
  name: string;
  type: NPCType;
  items: ItemConfig[];
  loottable: string[];
}*/

type NPC = FriendlyNPCConfig | HostileNPCConfig;

export{NPC, FriendlyNPCConfig, HostileNPCConfig}
