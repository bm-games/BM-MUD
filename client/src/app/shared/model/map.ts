interface Tile {
  readonly north: boolean;
  readonly east: boolean;
  readonly south: boolean;
  readonly west: boolean;
  readonly color: string;

}

interface RoomMap {
  tiles: (Tile | null)[][]
}

export {Tile, RoomMap}
