import {BaseConfig} from "./BaseConfig";
import {NPCType} from "./NPCType";

//export class NPCConfig extends BaseConfig{
//  private _name: string;
//  private _type: NPCType;
//  private _items: number[] = [];
//  private _loottable: number[];
//  //private loottable: flatMap<ItemConfig, number> | undefined;
//
//  constructor(id: number, name: string, type: NPCType, items: number[], loottable: number[]) {
//    super(id);
//    this._name = name;
//    this._type = type;
//    this._items = items;
//    this._loottable = loottable;
//  }
//
//  get loottable(): number[] {
//    return this._loottable;
//  }
//
//  set loottable(value: number[]) {
//    this._loottable = value;
//  }
//  get items(): number[] {
//    return this._items;
//  }
//
//  set items(value: number[]) {
//    this._items = value;
//  }
//  get type(): NPCType {
//    return this._type;
//  }
//
//  set type(value: NPCType) {
//    this._type = value;
//  }
//  get name(): string {
//    return this._name;
//  }
//
//  set name(value: string) {
//    this._name = value;
//  }
//}

export interface NPCConfig {
  id: number;
  name: string;
  type: NPCType;
  items: number[];
  loottable: number[];
}
