import { Component, OnInit } from '@angular/core';
import {RaceConfig} from "../../../models/RaceConfig";
import {ConfigurationComponent} from "../configuration.component";

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
      if(!this.checkContainsName()){
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
   * Removes the selected race from the DungeonConfig
   * @param raceName name of the race you want to remove
   */
  deleteRace(raceName: string){
    let index = this.configuredRaces.findIndex(x => x.name == raceName)
    if(index > -1){
      this.configuredRaces.splice(index, 1)
    }
  }

  sliderValue(value: number) {
    return value;
  }

}
