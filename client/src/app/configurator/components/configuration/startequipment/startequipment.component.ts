import { Component, OnInit } from '@angular/core';
import {ConfigurationComponent} from "../configuration.component";
import {ItemConfig} from "../../../models/ItemConfig";

@Component({
  selector: 'app-startequipment',
  templateUrl: './startequipment.component.html',
  styleUrls: ['./startequipment.component.scss']
})
export class StartequipmentComponent implements OnInit {

  allEquipment: ItemConfig[] = [];
  selectedEquipment: string[] = [];

  constructor() { }

  ngOnInit(): void {

    this.allEquipment = ConfigurationComponent.allItems;
  }

}
