import {BaseConfig} from "./BaseConfig";
import {EquipmentSlot} from "./EquipmentSlot";
import {ItemConfig} from "./ItemConfig";

export interface EquipmentConfig extends ItemConfig{
  healthModifier: number | undefined;
  damageModifier: number | undefined;
  slot: EquipmentSlot | undefined;
}
