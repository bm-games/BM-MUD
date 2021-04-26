import {BaseConfig} from "./BaseConfig";

export class RaceConfig extends BaseConfig{
  get description(): string {
    return this._description;
  }

  set description(value: string) {
    this._description = value;
  }
  get damageMultiplier(): number {
    return this._damageMultiplier;
  }

  set damageMultiplier(value: number) {
    this._damageMultiplier = value;
  }
  get health(): number {
    return this._health;
  }

  set health(value: number) {
    this._health = value;
  }
  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }
  private _name: string;
  private _health: number;
  private _damageMultiplier: number;
  private _description: string;

  constructor(id: number, name: string, health: number, damageMultiplier: number, description: string) {
    super(id);
    this._name = name;
    this._health = health;
    this._damageMultiplier = damageMultiplier;
    this._description = description;
  }
}
