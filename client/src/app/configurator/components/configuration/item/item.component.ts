import {Component, OnInit} from '@angular/core';
import {ConsumableItemConfig, Item} from "../../../models/Item";
import {EquipmentConfig} from "../../../models/EquipmentConfig";
import {ConfigurationComponent} from "../configuration.component";
import {EquipmentSlot} from "../../../models/EquipmentSlot";
import {WeaponConfig} from "../../../models/WeaponConfig";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent implements OnInit {

  name: string | undefined;
  effect: string | undefined;
  damage: number | undefined;
  damageModifier: number | undefined;
  health: number | undefined;
  isConsumable: boolean = true;
  isWeapon: boolean = false;
  isEquipment: boolean = false;
  equipmentSlot: EquipmentSlot = EquipmentSlot.head;
  selectedItemType: string = 'Konsumierbares Item';

  itemTypes: string[] = ['Konsumierbares Item', 'Ausrüstung', 'Waffe'];
  itemCommands: string[] = ['Heilen', 'Gesundheit abziehen'];
  equipmentSlots: string[] = ['Kopf', 'Brust', 'Beine', 'Stiefel'];
  commandsForItems: string[] = [];
  configuredItems: Item[] = [];

  getCommandsForItems() : string[] {
    return ['Heilen', 'Gesundheit abziehen'].concat(Object.keys(ConfigurationComponent.commandConfig.customCommands));
  }

  constructor() {
  }

  ngOnInit(): void {
    this.configuredItems = ConfigurationComponent.allItems;
    this.commandsForItems = this.getCommandsForItems();
    console.log(this.commandsForItems)
  }

  /**
   * Generates a ItemConfig with the current UI-data inputs and adds it to the list 'configuredItems'
   * Depending on the selected ItemType, a ItemConfig, EquipmentConfig or WeaponConfig is created
   */
  addItem() {
    if (this.name != undefined && !this.checkContainsName()) {
      if (this.isConsumable) {
        if (this.effect != undefined) {

          let consumable: ConsumableItemConfig = {
            type: "net.bmgames.state.model.Consumable",
            name: this.name,
            effect: this.effect
          }
          this.configuredItems.push(consumable);

          this.name = undefined;
          this.effect = undefined;
        } else {
          window.alert("Es wurden nicht alle Werte eingegeben");
        }
      }
      if (this.isEquipment) {
        if (this.health != undefined && this.name != undefined && this.damageModifier != undefined) {

          let equip: EquipmentConfig = {
            type: "net.bmgames.state.model.Equipment",
            name: this.name,
            damageModifier: this.damageModifier,
            healthModifier: this.health,
            slot: "Head"
          }
          switch (this.equipmentSlot) {
            case EquipmentSlot.head:
              equip.slot = "Head";
              break;
            case EquipmentSlot.chest:
              equip.slot = "Chest";
              break;
            case EquipmentSlot.legs:
              equip.slot = "Legs";
              break;
            case EquipmentSlot.boots:
              equip.slot = "Boots";
              break;
          }
          this.configuredItems.push(equip);

          this.name = undefined;
          this.health = undefined;
          this.damageModifier = undefined;
        } else {
          window.alert("Es wurden nicht alle Werte eingegeben");
        }
      }
      if (this.isWeapon) {
        if (this.damage != undefined && this.damageModifier != undefined && this.name != undefined) {

          let weapon: WeaponConfig = {
            type: "net.bmgames.state.model.Weapon",
            name: this.name,
            damage: this.damage
          }
          this.configuredItems.push(weapon);

          this.name = undefined;
          this.damage = undefined;
          this.damageModifier = undefined;
        } else {
          window.alert("Es wurden nicht alle Werte eingegeben");
        }
      }
    } else {
      window.alert("Ungültiger Name. Es ist kein Name eingetragen oder es existiert bereits ein Item mit diesem Namen.");
    }
  }

  checkContainsName(): boolean {
    for (let i = 0; i < this.configuredItems.length; i++) {
      if (this.configuredItems[i].name == this.name) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the bool values 'isEquipment', 'isConsumable' and 'isWeapon' depending on the current UI selection
   * @param item selected UI value
   */
  itemTypeChanged(item: string) {
    switch (item) {
      case 'Ausrüstung':
        this.isEquipment = true;
        this.isConsumable = false;
        this.isWeapon = false;
        break;
      case 'Konsumierbares Item':
        this.isConsumable = true;
        this.isEquipment = false;
        this.isWeapon = false;
        break;
      case 'Waffe':
        this.isWeapon = true;
        this.isEquipment = false;
        this.isConsumable = false;
        break;
    }
  }

  /**
   * Sets the effect of a consumable item depending on the current UI selection
   * @param item selected UI value
   */
  itemEffectChanged(effect: string) {
    this.effect = effect;
  }

  /**
   * Sets the EquipmentSlot (Enum) depending on the current UI selection
   * @param item selected UI value
   */
  equipmentSlotChanged(slot: string) {
    switch (slot) {
      case "Kopf":
        this.equipmentSlot = EquipmentSlot.head;
        break;
      case "Brust":
        this.equipmentSlot = EquipmentSlot.chest;
        break;
      case "Beine":
        this.equipmentSlot = EquipmentSlot.legs;
        break;
      case "Stiefel":
        this.equipmentSlot = EquipmentSlot.boots;
        break;
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
