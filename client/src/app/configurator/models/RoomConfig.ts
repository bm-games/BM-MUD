import {Item} from "./Item";
import {NPC} from "./NPCConfig";
import {StringMap} from "./DungeonConfig";

export interface RoomConfig {
  id: number;
  name: string;
  north: number | undefined;
  east: number | undefined;
  south: number | undefined;
  west: number | undefined;
  items: Item[];
  npcs: NPC[];
  //npcs: StringMap<NPC>;
  message: string;
}
