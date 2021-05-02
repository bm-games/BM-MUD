import {BaseConfig} from "./BaseConfig";

//export class ClassConfig extends BaseConfig{
//  private _name: string;
//  private _healthMultiplier: number;
//  private _damage: number;
//  private _attackSpeed: number;
//  private _description: string;
//
//  constructor(id: number, name: string, healthMultiplier: number, damage: number, attackSpeed: number, description: string) {
//    super(id);
//    this._name = name;
//    this._healthMultiplier = healthMultiplier;
//    this._damage = damage;
//    this._attackSpeed = attackSpeed;
//    this._description = description;
//  }
//
//  get description(): string {
//    return this._description;
//  }
//
//  set description(value: string) {
//    this._description = value;
//  }
//  get attackSpeed(): number {
//    return this._attackSpeed;
//  }
//
//  set attackSpeed(value: number) {
//    this._attackSpeed = value;
//  }
//  get damage(): number {
//    return this._damage;
//  }
//
//  set damage(value: number) {
//    this._damage = value;
//  }
//  get healthMultiplier(): number {
//    return this._healthMultiplier;
//  }
//
//  set healthMultiplier(value: number) {
//    this._healthMultiplier = value;
//  }
//  get name(): string {
//    return this._name;
//  }
//
//  set name(value: string) {
//    this._name = value;
//  }
//}

export interface ClassConfig extends BaseConfig{
  name: string;
  healthMultiplier: number;
  damage: number;
  attackSpeed: number,
  description: string;
}
