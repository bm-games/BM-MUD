import {BaseConfig} from "./BaseConfig";
import {EquipmentSlot} from "./EquipmentSlot";
import {ItemConfig} from "./ItemConfig";

//export class EquipmentConfig extends ItemConfig{
//  //private _name: string;
//  private _healthModifier: number | undefined;
//  private _damageModifier: number | undefined;
//  private _slot: EquipmentSlot;
//
//  constructor(id: number, name: string, healthModifier: number | undefined, damageModifier: number | undefined, slot: EquipmentSlot) {
//    super(id, name, false, undefined);
//    //this._name = name;
//    this._healthModifier = healthModifier;
//    this._damageModifier = damageModifier;
//    this._slot = slot;
//  }
//
//  get slot(): EquipmentSlot {
//    return this._slot;
//  }
//
//  set slot(value: EquipmentSlot) {
//    this._slot = value;
//  }
//  get damageModifier(): number | undefined {
//    return this._damageModifier;
//  }
//
//  set damageModifier(value: number | undefined) {
//    this._damageModifier = value;
//  }
//  get healthModifier(): number | undefined {
//    return this._healthModifier;
//  }
//
//  set healthModifier(value: number | undefined) {
//    this._healthModifier = value;
//  }
//}

export interface EquipmentConfig extends BaseConfig{
  healthModifier: number | undefined;
  damageModifier: number | undefined;
  slot: EquipmentSlot | undefined;
}
