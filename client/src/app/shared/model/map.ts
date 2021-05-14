import {Item} from "../../configurator/models/Item";
import {NPC} from "../../configurator/models/NPCConfig";

interface Tile {
  readonly name: string;
  readonly north: boolean;
  readonly east: boolean;
  readonly south: boolean;
  readonly west: boolean;
  readonly color: string;
  readonly items: Item[];
  readonly npcs: NPC[];
}

interface RoomMap {
  tiles: (Tile | null)[][]
}

export {Tile, RoomMap}
