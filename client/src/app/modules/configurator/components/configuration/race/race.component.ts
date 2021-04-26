import { Component, OnInit } from '@angular/core';
import {RaceConfig} from "../../../models/RaceConfig";
import {DungeonConfig} from "../../../models/DungeonConfig";
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

  addRace(){
    if(this.name != undefined && this.health != undefined && this.damage != undefined && this.description != undefined){
      this.configuredRaces.push(new RaceConfig(this.getNextFreeId(), this.name, this.health, this.damage, this.description));
      this.name = undefined;
      this.health = undefined;
      this.damage = undefined;
      this.description = undefined;
      ConfigurationComponent.allRaces = this.configuredRaces;
    }else{
      window.alert("Es wurden nicht alle Werte eingegeben");
    }
  }

  getNextFreeId(): number {
    let id = 0;
    let foundId = false;
    let containsId = false;
    while(!foundId){
      for (let i = 0; i < this.configuredRaces.length; i++) {
        if(this.configuredRaces[i].id == id){
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
export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}
