import {BaseConfig} from "./BaseConfig";
import {EquipmentSlot} from "./EquipmentSlot";
import {Item} from "./Item";

export interface EquipmentConfig {
  readonly type: 'net.bmgames.state.model.Equipment';
  name: string;
  healthModifier: number;
  damageModifier: number;
  //slot: EquipmentSlot | undefined;
  slot: "Head" | "Chest" | "Legs" | "Boots";
}
