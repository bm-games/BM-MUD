import { Component, OnInit } from '@angular/core';
import {ItemConfig} from "../../../models/ItemConfig";
import {WeaponConfig} from "../../../models/WeaponConfig";
import {EquipmentConfig} from "../../../models/EquipmentConfig";
import {ConfigurationComponent} from "../configuration.component";

@Component({
  selector: 'app-startequipment',
  templateUrl: './startequipment.component.html',
  styleUrls: ['./startequipment.component.scss']
})
export class StartequipmentComponent implements OnInit {

  allEquipment: ItemConfig[] | EquipmentConfig[] | WeaponConfig[] = []
  selectedEquipment: ItemConfig[] = [];
  constructor() { }

  ngOnInit(): void {
    this.allEquipment = ConfigurationComponent.allItems;
  }

}
