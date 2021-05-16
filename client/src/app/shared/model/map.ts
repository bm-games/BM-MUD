import {Item} from "../../configurator/models/Item";
import {NPC} from "../../configurator/models/NPCConfig";

interface Tile {
  readonly name: string;
  readonly north: boolean;
  readonly east: boolean;
  readonly south: boolean;
  readonly west: boolean;
  readonly color: string;
  readonly items: string[];
  readonly npcs: string[];
  readonly players: string[];
}

interface RoomMap {
  tiles: (Tile | null)[][]
}

export {Tile, RoomMap}
