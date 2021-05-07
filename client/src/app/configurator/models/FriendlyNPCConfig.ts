import {Item} from "./Item";

export interface FriendlyNPCConfig {
  readonly type: 'net.bmgames.state.model.NPC.Friendly';
  name: string;
  items: Item[];
  // loottable: string[];
  commandOnInteraction: string;
  messageOnTalk: string;
}
