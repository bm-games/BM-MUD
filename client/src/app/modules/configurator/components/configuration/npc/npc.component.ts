import { Component, OnInit } from '@angular/core';
import {NPCConfig} from "../../../models/NPCConfig";
import {ConfigurationComponent} from "../configuration.component";
import {HostileNPCConfig} from "../../../models/HostileNPCConfig";
import {ItemConfig} from "../../../models/ItemConfig";
import {FriendlyNPCConfig} from "../../../models/FriendlyNPCConfig";

@Component({
  selector: 'app-npc',
  templateUrl: './npc.component.html',
  styleUrls: ['./npc.component.scss']
})
export class NPCComponent implements OnInit {

  name: string | undefined;
  messageOnTalk: string | undefined;
  npcTypes: string[] = ['Verbündet', 'Feindlich'];
  allEquipment: ItemConfig[] = [];
  allItemsLoottable: ItemConfig[] = [];
  interactionCommands: string[] = ['Heilen', 'In zufälligen Raum teleportieren'];
  selectedNPCType: string = 'Verbündet';
  isHostile = false;
  health: number | undefined;
  damage: number | undefined;
  selectedNPCEquipment: ItemConfig[] = [];
  selectedNPCItemsLoottable: ItemConfig[] = [];
  selectedCommandOnInteraction: string = '';

  configuredNPCs: NPCConfig[] = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredNPCs = ConfigurationComponent.allNPCs;
    this.allEquipment = ConfigurationComponent.allItems;
    this.allItemsLoottable = ConfigurationComponent.allItems;
  }

  addNPC(){
    let equipmentIds: number[] = [];
    this.selectedNPCEquipment.forEach(e => equipmentIds.push(e.id));
    let loottableItemsIds: number[] = [];
    this.selectedNPCItemsLoottable.forEach(e => loottableItemsIds.push(e.id));
    if(this.isHostile){
      if(this.name != undefined && this.health != undefined && this.damage != undefined){
        this.configuredNPCs.push(new HostileNPCConfig(this.getNextFreeId(), this.name, equipmentIds, loottableItemsIds, this.health, this.damage))
        this.name = undefined;
        this.health = undefined;
        this.damage = undefined;
        ConfigurationComponent.allNPCs = this.configuredNPCs;
      }else{
        window.alert("Es wurden nicht alle Daten eingegeben");
      }
    }else{
      if(this.name != undefined && this.messageOnTalk != undefined && this.selectedCommandOnInteraction != undefined){
        this.configuredNPCs.push(new FriendlyNPCConfig(this.getNextFreeId(), this.name, equipmentIds, loottableItemsIds, this.selectedCommandOnInteraction, this.messageOnTalk))
        this.name = undefined;
        this.messageOnTalk = undefined;
        this.selectedCommandOnInteraction = '';
        ConfigurationComponent.allNPCs = this.configuredNPCs;
      }else{
        window.alert("Es wurden nicht alle Daten eingegeben");
      }
    }
  }

  getNextFreeId(): number {
    let id = 0;
    let foundId = false;
    let containsId = false;
    while(!foundId){
      for (let i = 0; i < this.configuredNPCs.length; i++) {
        if(this.configuredNPCs[i].id == id){
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

  npcTypeChanged(type: string){
    switch(type){
      case "Verbündet":
        this.isHostile = false;
        break;
      case "Feindlich":
        this.isHostile = true;
        break;
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
