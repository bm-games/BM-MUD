import {NPCConfig} from "./NPCConfig";

export interface HostileNPCConfig extends NPCConfig{
  health: number | undefined;
  damage: number | undefined;
}
