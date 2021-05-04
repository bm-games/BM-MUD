import { Component, OnInit } from '@angular/core';
import {CommandConfig} from "../../../models/CommandConfig";
import {ConfigurationComponent} from "../configuration.component";

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
  commandSyntax: string | undefined;

  pickupAlias: string | undefined;
  consumeAlias: string | undefined;
  showInventoryAlias: string | undefined;
  goAlias: string | undefined;
  lookAlias: string | undefined;

  isCustomCommand = false;

  allCommands: CommandConfig[] = [];

  customCommands: CommandConfig[] = [];


  constructor() { }

  ngOnInit(): void {
    //this.allCommands = ConfigurationComponent.allCommands;
    this.customCommands = ConfigurationComponent.allCommands;
  }

  /**
   * Generates a new CommandConfig with the current UI-data inputs and adds it to the list 'customCommands'.
   * If it's not a custom command, the aliases for the standard commands are saved
   */
  addCommand(){
    if(this.isCustomCommand){
      if(this.selectedActions.length > 0 && this.commandSyntax != undefined){
        //this.customCommands.push(new CommandConfig(this.getNextFreeId(), this.commandSyntax, this.selectedActions))

        this.customCommands.push({
          id: this.getNextFreeId(),
          command: this.commandSyntax,
          actions: this.selectedActions
        });

        ConfigurationComponent.allCommands = this.customCommands;
        this.selectedActions = [];
        this.commandSyntax = undefined;
      }
      else{
        window.alert("Es wurden nicht alle Werte eingegeben");
      }
    }else{
      // tbd --> Save standard commands to the list with new aliases
      if(this.pickupAlias == undefined) this.pickupAlias = "pickup";
      if(this.consumeAlias == undefined) this.consumeAlias = "consume";
      if(this.showInventoryAlias == undefined) this.showInventoryAlias = "show inventory";
      if(this.goAlias == undefined) this.goAlias = "go";
      if(this.lookAlias == undefined) this.lookAlias = "look";
    }
  }

  /**
   * sets the bool value 'isCustomCommand' depending on the current UI-selection
   * @param type selected value from the UI
   */
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

  /**
   * Finds next smallest possible ID for the new CommandConfig
   * @returns id: number
   */
  getNextFreeId(): number {
    let id = 0;
    let foundId = false;
    let containsId = false;
    while(!foundId){
      for (let i = 0; i < this.customCommands.length; i++) {
        if(this.customCommands[i].id == id){
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

  sliderValue(value: number) {
    return value;
  }
}
