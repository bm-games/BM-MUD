import {BaseConfig} from "./BaseConfig";

export interface ItemConfig{
  name: string;
  isConsumable: boolean;
  effect: string | undefined;
}
