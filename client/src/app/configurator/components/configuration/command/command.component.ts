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
  allActions: string[] = ['Kein Command', 'move player north', 'move player east', 'move player south', 'move player west', 'look', 'show inventory', 'pickup', 'hit'];
  selectedAction1: string = 'Kein Command';
  selectedAction2: string = 'Kein Command';
  selectedAction3: string = 'Kein Command';
  selectedAction4: string = 'Kein Command';
  selectedAction5: string = 'Kein Command';
  commandSyntax: string | undefined;

  pickupAlias: string | undefined;
  consumeAlias: string | undefined;
  showInventoryAlias: string | undefined;
  goAlias: string | undefined;
  lookAlias: string | undefined;

  isCustomCommand = false;

  aliases: Map<string,string> = new Map<string, string>();            // Map<Name, Alias>
  customCommands: Map<string, string> = new Map<string, string>();    // Map<Name, "Action1,Action2,Action3,...">

  constructor() { }

  ngOnInit(): void {
    this.aliases = ConfigurationComponent.commandConfig.aliases;
    this.customCommands = ConfigurationComponent.commandConfig.customCommands;

    this.pickupAlias = this.aliases.get('pickup');
    this.consumeAlias = this.aliases.get('consume');
    this.showInventoryAlias = this.aliases.get('show inventory');
    this.goAlias = this.aliases.get('go');
    this.lookAlias = this.aliases.get('look');
  }

  /**
   * Generates a new CommandConfig with the current UI-data inputs and adds it to the list 'customCommands'.
   * If it's not a custom command, the aliases for the standard commands are saved
   */
  addCommand(){
    if(this.isCustomCommand){
      if(this.commandSyntax != undefined){
        //this.customCommands.push(new CommandConfig(this.getNextFreeId(), this.commandSyntax, this.selectedActions))

        let actionString = "";
        if(this.selectedAction1 != "Kein Command") actionString += this.selectedAction1 + ", ";
        if(this.selectedAction2 != "Kein Command") actionString += this.selectedAction2 + ", ";
        if(this.selectedAction3 != "Kein Command") actionString += this.selectedAction3 + ", ";
        if(this.selectedAction4 != "Kein Command") actionString += this.selectedAction4 + ", ";
        if(this.selectedAction5 != "Kein Command") actionString += this.selectedAction5;

        this.customCommands.set(this.commandSyntax, actionString);
        console.log(this.customCommands);

        /*this.customCommands.push({
          id: this.getNextFreeId(),
          command: this.commandSyntax,
          actions: this.selectedActions
        });*/

        ConfigurationComponent.commandConfig.customCommands = this.customCommands;
        this.commandSyntax = undefined;
      }
      else{
        window.alert("Es wurden nicht alle Werte eingegeben");
      }
    }else{
      if(this.pickupAlias == undefined) this.pickupAlias = "pickup";
      if(this.consumeAlias == undefined) this.consumeAlias = "consume";
      if(this.showInventoryAlias == undefined) this.showInventoryAlias = "show inventory";
      if(this.goAlias == undefined) this.goAlias = "go";
      if(this.lookAlias == undefined) this.lookAlias = "look";

      this.aliases.set('pickup', this.pickupAlias);
      this.aliases.set('consume', this.consumeAlias);
      this.aliases.set('show inventory', this.showInventoryAlias);
      this.aliases.set('go', this.goAlias);
      this.aliases.set('look', this.lookAlias);

      ConfigurationComponent.commandConfig.aliases = this.aliases;
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
  //getNextFreeId(): number {
  //  let id = 0;
  //  let foundId = false;
  //  let containsId = false;
  //  while(!foundId){
  //    for (let i = 0; i < this.customCommands.length; i++) {
  //      if(this.customCommands[i].id == id){
  //        containsId = true;
  //      }
  //    }
  //    if(!containsId){
  //      foundId = true;
  //    }else{
  //      containsId = false;
  //      id++;
  //    }
  //  }
  //  return id;
  //}

  sliderValue(value: number) {
    return value;
  }
}
