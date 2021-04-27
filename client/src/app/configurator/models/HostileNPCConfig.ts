import {NPCConfig} from "./NPCConfig";

export class HostileNPCConfig extends NPCConfig{
  private _health: number;
  private _damage: number;

  constructor(id: number, name: string, items: number[], loottable: number[], health: number, damage: number) {
    super(id, name, 'Feindlich', items, loottable);
    this._health = health;
    this._damage = damage;
  }

  get damage(): number {
    return this._damage;
  }

  set damage(value: number) {
    this._damage = value;
  }
  get health(): number {
    return this._health;
  }

  set health(value: number) {
    this._health = value;
  }
}
