import {BaseConfig} from "./BaseConfig";

export interface RoomConfig {
  id: number;
  name: string;
  north: number | undefined;
  east: number | undefined;
  south: number | undefined;
  west: number | undefined;
  items: string[];
  npcs: string[];
  message: string;
}
