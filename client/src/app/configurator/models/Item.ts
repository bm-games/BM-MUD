import {BaseConfig} from "./BaseConfig";
import {EquipmentConfig} from "./EquipmentConfig";
import {WeaponConfig} from "./WeaponConfig";

/*export interface Item {
  name: string;
  //isConsumable: boolean;
  //effect: string | undefined;
}*/

interface ConsumableItemConfig {
  readonly type: 'net.bmgames.state.model.Consumable'
  name: string;
  effect: string;
}

type Item = ConsumableItemConfig | EquipmentConfig | WeaponConfig;

export{Item, ConsumableItemConfig, EquipmentConfig, WeaponConfig}

