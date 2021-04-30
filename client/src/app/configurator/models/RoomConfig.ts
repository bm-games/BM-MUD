import {BaseConfig} from "./BaseConfig";

//export class RoomConfig extends BaseConfig{
//  private _name: string;
//  private _north: number | undefined;
//  private _east: number | undefined;
//  private _south: number | undefined;
//  private _west: number | undefined;
//  private _message: string;
//  private _items: number[] = [];
//  private _npcs: number[] = [];
//
//  constructor(id: number, name: string, message: string, items: number[], npcs: number[], north: number, east: number, south: number, west: number) {
//    super(id);
//    this._name = name;
//    this._message = message;
//    this._items = items;
//    this._npcs = npcs;
//    this._north = north;
//    this._east = east;
//    this._south = south;
//    this._west = west;
//  }
//
//  get npcs(): number[] {
//    return this._npcs;
//  }
//
//  set npcs(value: number[]) {
//    this._npcs = value;
//  }
//  get items(): number[] {
//    return this._items;
//  }
//
//  set items(value: number[]) {
//    this._items = value;
//  }
//  get message(): string {
//    return this._message;
//  }
//
//  set message(value: string) {
//    this._message = value;
//  }
//  get west(): number | undefined {
//    return this._west;
//  }
//
//  set west(value: number | undefined) {
//    this._west = value;
//  }
//  get south(): number | undefined {
//    return this._south;
//  }
//
//  set south(value: number | undefined) {
//    this._south = value;
//  }
//  get east(): number | undefined {
//    return this._east;
//  }
//
//  set east(value: number | undefined) {
//    this._east = value;
//  }
//  get north(): number | undefined {
//    return this._north;
//  }
//
//  set north(value: number | undefined) {
//    this._north = value;
//  }
//  get name(): string {
//    return this._name;
//  }
//
//  set name(value: string) {
//    this._name = value;
//  }
//}

export interface RoomConfig extends BaseConfig{
  name: string;
  north: number | undefined;
  east: number | undefined;
  south: number | undefined;
  west: number | undefined;
  //items: number[];
  //npcs: number[];
  items: string[];
  npcs: string[];
  message: string;
}
