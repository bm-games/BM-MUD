import {EquipmentConfig} from "./EquipmentConfig";
import {ItemConfig} from "./ItemConfig";

export interface WeaponConfig extends ItemConfig{
  baseDamage: number | undefined;
}
