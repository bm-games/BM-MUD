import {BaseConfig} from "./BaseConfig";

//export class ItemConfig extends BaseConfig {
//  private _name: string;
//  private _isConsumable: boolean;
//  private _effect: string | undefined;
//
//  constructor(id: number, name: string, isConsumable: boolean, effect: string | undefined) {
//    super(id);
//    this._name = name;
//    this._isConsumable = isConsumable;
//    this._effect = effect;
//  }
//
//  get effect(): string | undefined {
//    return this._effect;
//  }
//
//  set effect(value: string | undefined) {
//    this._effect = value;
//  }
//
//  get isConsumable(): boolean {
//    return this._isConsumable;
//  }
//
//  set isConsumable(value: boolean) {
//    this._isConsumable = value;
//  }
//
//  get name(): string {
//    return this._name;
//  }
//
//  set name(value: string) {
//    this._name = value;
//  }
//
//}

export interface ItemConfig extends BaseConfig{
  name: string;
  isConsumable: boolean;
  effect: string | undefined;
}

export interface ConsumableItemConfig{
  name: string,
  effect: string
}
