import {EquipmentConfig} from "./EquipmentConfig";
import {Item} from "./Item";

export interface WeaponConfig {
  readonly type: 'net.bmgames.state.model.Weapon';
  name: string;
  damage: number;
}
