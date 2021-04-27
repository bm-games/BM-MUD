import {Component, OnInit} from '@angular/core';
import {ItemConfig} from "../../../models/ItemConfig";
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
  damageMultiplier: number | undefined;
  health: number | undefined;
  isConsumable: boolean = true;
  isWeapon: boolean = false;
  isEquipment: boolean = false;
  equipmentSlot: EquipmentSlot = EquipmentSlot.head;
  selectedItemType: string = 'Konsumierbares Item';

  itemTypes: string[] = ['Konsumierbares Item', 'Ausrüstung', 'Waffe'];
  itemCommands: string[] = ['Heilen', 'Gesundheit abziehen'];
  equipmentSlots: string[] = ['Kopf', 'Brust', 'Beine', 'Stiefel'];

  configuredItems: ItemConfig[] = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredItems = ConfigurationComponent.allItems;
  }

  addItem(){
    if(this.isConsumable){
      if(this.name != undefined && this.effect != undefined){
        this.configuredItems.push(new ItemConfig(this.getNextFreeId(), this.name, true, this.effect));
        this.name = undefined;
        this.effect = undefined;
      }else{
        window.alert("Es wurden nicht alle Werte eingegeben");
      }
    }
    if(this.isEquipment){
      if(this.name != undefined && this.health != undefined){
        this.configuredItems.push(new EquipmentConfig(this.getNextFreeId(), this.name, this.health, undefined, this.equipmentSlot));
        this.name = undefined;
        this.health = undefined;
      }else{
        window.alert("Es wurden nicht alle Werte eingegeben");
      }
    }
    if(this.isWeapon){
      if(this.name != undefined && this.damage != undefined && this.damageMultiplier != undefined) {
        this.configuredItems.push(new WeaponConfig(this.getNextFreeId(), this.name, this.damage, this.damageMultiplier));
        this.name = undefined;
        this.damage = undefined;
        this.damageMultiplier = undefined;
      }else{
        window.alert("Es wurden nicht alle Werte eingegeben");
      }
    }
  }

  getNextFreeId(): number {
    let id = 0;
    let foundId = false;
    let containsId = false;
    while(!foundId){
      for (let i = 0; i < this.configuredItems.length; i++) {
        if(this.configuredItems[i].id == id){
          containsId = true;
        }
      }
      if(!containsId){
        foundId = true;
      }else{
        containsId = false;
        id++;
      }
    }
    return id;
  }

  itemTypeChanged(item: string) {
    switch(item){
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

  itemEffectChanged(effect: string){
    this.effect = effect;
    console.log(effect);
  }

  equipmentSlotChanged(slot: string){
    switch(slot){
      case "Kopf":
        this.equipmentSlot = EquipmentSlot.head;
        break;
      case "Brust":
        this.equipmentSlot = EquipmentSlot.chest;
        break;
      case "Beine":
        this.equipmentSlot = EquipmentSlot.legs
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