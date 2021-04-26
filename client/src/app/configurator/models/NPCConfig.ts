import {BaseConfig} from "./BaseConfig";
import {flatMap} from "rxjs/internal/operators";
import {ItemConfig} from "./ItemConfig";
import {extractDirectiveTypeCheckMeta} from "@angular/compiler-cli/src/ngtsc/metadata";

export class NPCConfig extends BaseConfig{
  private _name: string;
  private _type: string;
  private _items: number[] = [];
  private _loottable: number[];
  //private loottable: flatMap<ItemConfig, number> | undefined;

  constructor(id: number, name: string, type: string, items: number[], loottable: number[]) {
    super(id);
    this._name = name;
    this._type = type;
    this._items = items;
    this._loottable = loottable;
  }

  get loottable(): number[] {
    return this._loottable;
  }

  set loottable(value: number[]) {
    this._loottable = value;
  }
  get items(): number[] {
    return this._items;
  }

  set items(value: number[]) {
    this._items = value;
  }
  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }
  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }
}
