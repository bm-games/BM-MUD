import {Component, OnInit} from '@angular/core';
import {ConfigurationComponent} from "../configuration.component";
import {Item} from "../../../models/Item";
import {HostileNPCConfig} from "../../../models/HostileNPCConfig";
import {NPC} from "../../../models/NPCConfig";
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
  allEquipment: Item[] = [];
  //allItemsLoottable: Item[] | EquipmentConfig[] | WeaponConfig[] = [];
  interactionCommands: string[] = ['move player north', 'move player east', 'move player south', 'move player west', 'look', 'show inventory', 'pickup', 'hit'];
  selectedNPCType: string = 'Verbündet';
  isHostile = false;
  health: number | undefined;
  damage: number | undefined;
  selectedNPCEquipment: Item[] = [];
  selectedCommandOnInteraction: string = '';

  configuredNPCs: NPC[] = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredNPCs = ConfigurationComponent.allNPCs;
    this.allEquipment = ConfigurationComponent.allItems;
  }

  /**
   * Generates a NPCConfig with the current UI-data inputs and adds it to the list 'configuredNPCs'.
   * Depending on the selected NPCType, a FriendlyNPCConfig or a HostileNPCConfig is created.
   */
  addNPC(){
    if(this.name != undefined && !this.checkContainsName() && this.name.trim() != ''){
      if(this.isHostile){
        if(this.health != undefined && this.damage != undefined){
          let hostile: HostileNPCConfig = {
            name: this.name,
            items: this.selectedNPCEquipment,
            health: this.health,
            damage: this.damage,
            type: "net.bmgames.state.model.NPC.Hostile"
          }
          this.configuredNPCs.push(hostile);

          this.name = undefined;
          this.health = undefined;
          this.damage = undefined;
          ConfigurationComponent.allNPCs = this.configuredNPCs;
        }else{
          window.alert("Es wurden nicht alle Daten eingegeben");
        }
      }else{
        if(this.messageOnTalk != undefined && this.selectedCommandOnInteraction != undefined && this.messageOnTalk.trim() != ''){
          let friendly: FriendlyNPCConfig = {
            name: this.name,
            items: this.selectedNPCEquipment,
            messageOnTalk: this.messageOnTalk,
            commandOnInteraction: '',
            type: "net.bmgames.state.model.NPC.Friendly"
          }
          this.configuredNPCs.push(friendly);

          this.name = undefined;
          this.messageOnTalk = undefined;
          this.selectedCommandOnInteraction = '';
          ConfigurationComponent.allNPCs = this.configuredNPCs;
        }else{
          window.alert("Es wurden nicht alle Daten eingegeben");
        }
      }
    }else{
      window.alert("Ungültiger Name. Entweder es ist kein Name eingetragen oder es exisitert bereits ein NPC mit diesem Namen.");
    }
  }

  checkContainsName(): boolean{
    for (let i = 0; i < this.configuredNPCs.length; i++) {
      if(this.configuredNPCs[i].name == this.name){
        return true;
      }
    }
    return false;
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

  /**
   * removes the selected NPC from the DungeonConfig
   * @param npcName name of the npc you want to remove
   */
  deleteNPC(npcName: string){
    let index = this.configuredNPCs.findIndex(npc => npc.name == npcName)
    if(index > -1){
      this.configuredNPCs.splice(index, 1)
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
