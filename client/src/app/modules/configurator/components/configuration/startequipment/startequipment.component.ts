import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-startequipment',
  templateUrl: './startequipment.component.html',
  styleUrls: ['./startequipment.component.scss']
})
export class StartequipmentComponent implements OnInit {

  allEquipment: string[] = ['Helm', 'Schwert', 'Heiltrank', 'Apfel', 'Lampe'];
  selectedEquipment: string[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
