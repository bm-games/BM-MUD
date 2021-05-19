interface Tile {
  readonly name: string;
  // readonly north: boolean;
  // readonly east: boolean;
  // readonly south: boolean;
  // readonly west: boolean;
  readonly type: "Visited" | "NotVisited" | "Current";
  readonly items: string[];
  readonly npcs: string[];
  readonly players: string[];
}

interface RoomMap {
  tiles: (Tile | null)[][]
}

export {Tile, RoomMap}
