import {BaseConfig} from "./BaseConfig";
import {EquipmentSlot} from "./EquipmentSlot";

export class EquipmentConfig extends BaseConfig{
  private _name: string;
  private _healthModifier: number | undefined;
  private _damageModifier: number;
  private _slot: EquipmentSlot;

  constructor(id: number, name: string, healthModifier: number | undefined, damageModifier: number, slot: EquipmentSlot) {
    super(id);
    this._name = name;
    this._healthModifier = healthModifier;
    this._damageModifier = damageModifier;
    this._slot = slot;
  }

  get slot(): EquipmentSlot {
    return this._slot;
  }

  set slot(value: EquipmentSlot) {
    this._slot = value;
  }
  get damageModifier(): number {
    return this._damageModifier;
  }

  set damageModifier(value: number) {
    this._damageModifier = value;
  }
  get healthModifier(): number | undefined {
    return this._healthModifier;
  }

  set healthModifier(value: number | undefined) {
    this._healthModifier = value;
  }
  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }
}
