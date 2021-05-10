import { Component, OnInit } from '@angular/core';
import {Item} from "../../../models/Item";
import {WeaponConfig} from "../../../models/WeaponConfig";
import {EquipmentConfig} from "../../../models/EquipmentConfig";
import {ConfigurationComponent} from "../configuration.component";

@Component({
  selector: 'app-startequipment',
  templateUrl: './startequipment.component.html',
  styleUrls: ['./startequipment.component.scss']
})
export class StartequipmentComponent implements OnInit {

  allEquipment: Item[] | EquipmentConfig[] | WeaponConfig[] = []
  selectedEquipment: Item[] = [];
  constructor() { }

  ngOnInit(): void {
    this.allEquipment = ConfigurationComponent.allItems;
    this.selectedEquipment = ConfigurationComponent.startequipment;
  }

  saveStartequipment(){
    ConfigurationComponent.startequipment = this.selectedEquipment;
  }

}
