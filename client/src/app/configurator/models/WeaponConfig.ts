import {EquipmentConfig} from "./EquipmentConfig";

//export class WeaponConfig extends EquipmentConfig{
//  private _baseDamage: number;
//
//  constructor(id: number, name: string, damageModifier: number, baseDamage: number) {
//    super(id, name, undefined, damageModifier, EquipmentSlot.weapon);
//    this._baseDamage = baseDamage;
//  }
//
//  get baseDamage(): number {
//    return this._baseDamage;
//  }
//
//  set baseDamage(value: number) {
//    this._baseDamage = value;
//  }
//}

export interface WeaponConfig extends EquipmentConfig{
  baseDamage: number | undefined;
}
