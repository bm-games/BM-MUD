import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-npc',
  templateUrl: './npc.component.html',
  styleUrls: ['./npc.component.scss']
})
export class NPCComponent implements OnInit {

  npcTypes: string[] = ['Friendly', 'Hostile'];
  allEquipment: string[] = ['Schwert', 'Helm'];
  allItemsLoottable: string[] = ['Stock', 'Heilsaft', 'Schwert', 'Helm'];
  interactionCommands: string[] = ['Heilen', 'In zufälligen Raum teleportieren'];
  selectedNPCType: string = 'Friendly';
  isHostile = false;
  selectedNPCEquipment: string[] = [];
  selectedNPCItemsLoottable: string[] = [];
  selectedCommandOnInteraction: string = '';

  constructor() { }

  ngOnInit(): void {
  }

  npcTypeChanged(type: string){
    switch(type){
      case "Friendly":
        this.isHostile = false;
        break;
      case "Hostile":
        this.isHostile = true;
        break;
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
