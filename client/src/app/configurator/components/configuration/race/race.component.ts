import { Component, OnInit } from '@angular/core';
import {RaceConfig} from "../../../models/RaceConfig";
import {DungeonConfig} from "../../../models/DungeonConfig";
import {ConfigurationComponent} from "../configuration.component";
import {NPCType} from "../../../models/NPCType";

@Component({
  selector: 'app-race',
  templateUrl: './race.component.html',
  styleUrls: ['./race.component.scss']
})
export class RaceComponent implements OnInit {

  name: string | undefined;
  health: number | undefined
  damage: number | undefined
  description: string | undefined;

  configuredRaces: RaceConfig[] = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredRaces = ConfigurationComponent.allRaces;
  }

  /**
   * Generates a RaceConfig with the current UI-data inputs and adds it to the list 'configuredRaces'
   */
  addRace(){
    if(this.name != undefined && this.health != undefined && this.damage != undefined && this.description != undefined){
      if(this.checkContainsName() == false){
        this.configuredRaces.push({
          name: this.name,
          health: this.health,
          damageModifier: this.damage,
          description: this.description
        });

        this.name = undefined;
        this.health = undefined;
        this.damage = undefined;
        this.description = undefined;
        ConfigurationComponent.allRaces = this.configuredRaces;
      }else{
        window.alert("Es existiert bereits eine Rasse mit dem Namen: " + this.name);
      }
    }else{
      window.alert("Es wurden nicht alle Werte eingegeben");
    }
  }

  checkContainsName(): boolean{
    for (let i = 0; i < this.configuredRaces.length; i++) {
      if(this.configuredRaces[i].name == this.name){
        return true;
      }
    }
    return false;
  }

  /**
   * Finds next smallest possible ID for the new RaceConfig
   * @returns id: number
   */
  //getNextFreeId(): number {
  //  let id = 0;
  //  let foundId = false;
  //  let containsId = false;
  //  while(!foundId){
  //    for (let i = 0; i < this.configuredRaces.length; i++) {
  //      if(this.configuredRaces[i].id == id){
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
