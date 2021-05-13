import {Component, OnInit} from '@angular/core';
import {ConfigurationComponent} from "../configuration.component";
import {StringMap} from "../../../models/DungeonConfig";

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

  aliases: StringMap<string> = {}           // StringMap[Name] = 'alias'
  customCommands: StringMap<string> = {}    // StringMap[Name] = 'ActionString'
  customCommandList: string[] = []

  constructor() { }

  ngOnInit(): void {
    this.aliases = ConfigurationComponent.commandConfig.aliases;
    this.customCommands = ConfigurationComponent.commandConfig.customCommands;
    this.pickupAlias = this.aliases['pickup'];
    this.consumeAlias = this.aliases['consume'];
    this.showInventoryAlias = this.aliases['show inventory'];
    this.goAlias = this.aliases['go'];
    this.lookAlias = this.aliases['look'];
  }

  /**
   * Generates a new CommandConfig with the current UI-data inputs and adds it to the list 'customCommands'.
   * If it's not a custom command, the aliases for the standard commands are saved
   */
  addCommand(){
    if(this.isCustomCommand){
      if(this.commandSyntax != undefined){
        let actionString = "";
        if(this.selectedAction1 != "Kein Command") actionString += this.selectedAction1 + ", ";
        if(this.selectedAction2 != "Kein Command") actionString += this.selectedAction2 + ", ";
        if(this.selectedAction3 != "Kein Command") actionString += this.selectedAction3 + ", ";
        if(this.selectedAction4 != "Kein Command") actionString += this.selectedAction4 + ", ";
        if(this.selectedAction5 != "Kein Command") actionString += this.selectedAction5;

        this.customCommands[this.commandSyntax] = actionString;

        ConfigurationComponent.commandConfig.customCommands = this.customCommands;
        ConfigurationComponent.customCommandList.push(this.commandSyntax);
        this.commandSyntax = undefined;
        console.log(ConfigurationComponent.customCommandList)
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

      this.aliases['pickup'] = this.pickupAlias;
      this.aliases['consume'] = this.consumeAlias;
      this.aliases['show inventory'] = this.showInventoryAlias;
      this.aliases['go'] = this.goAlias;
      this.aliases['look'] = this.lookAlias;

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

  sliderValue(value: number) {
    return value;
  }
}
