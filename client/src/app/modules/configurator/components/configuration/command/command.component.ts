import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-command',
  templateUrl: './command.component.html',
  styleUrls: ['./command.component.scss']
})
export class CommandComponent implements OnInit {

  commandTypes: string[] = ['Standard Befehle', 'Eigener Befehl'];
  selectedCommandType: string = 'Standard Befehle';
  allActions: string[] = ['In einen beliebigen Raum teleportieren', 'Charakter LP abziehen', 'Charakter heilen', 'NPC LP abziehen', 'Bewegen nach Norden', 'Bewegen nach Osten', 'Bewegen nach Süden', 'Bewegen nach Westen'];
  selectedActions: string[] = [];

  isCustomCommand = false;


  constructor() { }

  ngOnInit(): void {
  }

  commandTypeChanged(type: string){
    switch(type){
      case "Standard Befehle":
        this.isCustomCommand = false;
        break;
      case "Eigener Befehl":
        this.isCustomCommand = true;
        break;
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
