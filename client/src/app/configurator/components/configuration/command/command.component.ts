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
  allActions: string[] = ['Kein Command', 'move player north', 'move player east', 'move player south', 'move player west', 'reduce player hp by 10', 'increase player hp by 10'];
  selectedAction1: string = 'Kein Command';
  selectedAction2: string = 'Kein Command';
  selectedAction3: string = 'Kein Command';
  selectedAction4: string = 'Kein Command';
  selectedAction5: string = 'Kein Command';
  commandSyntax: string | undefined;

  command2Disabled: boolean = true;
  command3Disabled: boolean = true;
  command4Disabled: boolean = true;
  command5Disabled: boolean = true;

  pickupAlias: string | undefined;
  consumeAlias: string | undefined;
  showInventoryAlias: string | undefined;
  goAlias: string | undefined;
  lookAlias: string | undefined;
  hitAlias: string | undefined;

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
    this.hitAlias = this.aliases['hit'];
  }

  /**
   * Generates a new CommandConfig with the current UI-data inputs and adds it to the list 'customCommands'.
   * If it's not a custom command, the aliases for the standard commands are saved
   */
  addCommand(){
    if(this.isCustomCommand){
      if(this.commandSyntax != undefined && this.commandSyntax.trim() != ''){
        let actionString = "";
        if(this.selectedAction1 != "Kein Command") actionString += this.selectedAction1
        if(this.selectedAction2 != "Kein Command") actionString += ", " + this.selectedAction2
        if(this.selectedAction3 != "Kein Command") actionString += ", " + this.selectedAction3
        if(this.selectedAction4 != "Kein Command") actionString += ", " + this.selectedAction4
        if(this.selectedAction5 != "Kein Command") actionString += ", " + this.selectedAction5;

        this.customCommands[this.commandSyntax] = actionString;

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
      if(this.hitAlias == undefined) this.hitAlias = "hit";

      this.aliases['pickup'] = this.pickupAlias;
      this.aliases['consume'] = this.consumeAlias;
      this.aliases['show inventory'] = this.showInventoryAlias;
      this.aliases['go'] = this.goAlias;
      this.aliases['look'] = this.lookAlias;
      this.aliases['hit'] = this.hitAlias;

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
   * Removes the selected customCommand from the DungeonConfig
   * @param name name of the custom command you want to remove
   */
  deleteCustomCommand(name: string){
    delete this.customCommands[name]
  }

  command1changed(){
    if(this.selectedAction1 != 'Kein Command'){
      this.command2Disabled = false
    }else{
      this.command2Disabled = true
    }
  }
  command2changed(){
    if(this.selectedAction2 != 'Kein Command'){
      this.command3Disabled = false
    }else{
      this.command3Disabled = true
    }
  }
  command3changed(){
    if(this.selectedAction3 != 'Kein Command'){
      this.command4Disabled = false
    }else{
      this.command4Disabled = true
    }
  }
  command4changed(){
    if(this.selectedAction4 != 'Kein Command'){
      this.command5Disabled = false
    }else{
      this.command5Disabled = true
    }
  }

  sliderValue(value: number) {
    return value;
  }
}
