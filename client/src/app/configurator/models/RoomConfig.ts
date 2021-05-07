import {BaseConfig} from "./BaseConfig";
import {Item} from "./Item";
import {NPC} from "./NPCConfig";

export interface RoomConfig {
  id: number;
  name: string;
  north: number | undefined;
  east: number | undefined;
  south: number | undefined;
  west: number | undefined;
  items: Item[];
  npcs: Map<string, NPC>;
  message: string;
}
