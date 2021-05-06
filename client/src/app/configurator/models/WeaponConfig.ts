import {EquipmentConfig} from "./EquipmentConfig";

export interface WeaponConfig extends EquipmentConfig{
  baseDamage: number | undefined;
}
