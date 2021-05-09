import {Item} from "./Item";

export interface HostileNPCConfig {
  readonly type: 'net.bmgames.state.model.NPC.Hostile';
  name: string;
  items: Item[];
  // loottable: string[];
  health: number;
  damage: number;
}
