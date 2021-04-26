import { Component, OnInit } from '@angular/core';
import {RaceConfig} from "../../../models/RaceConfig";
import {ClassConfig} from "../../../models/ClassConfig";
import {DungeonConfig} from "../../../models/DungeonConfig";

@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss']
})
export class ClassComponent implements OnInit {

  name: string | undefined;
  healthMultiplier: number | undefined;
  damage: number | undefined;
  attackSpeed: number | undefined;
  description: string | undefined;

  configuredClasses: ClassConfig[] = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredClasses = DungeonConfig.allClasses;
  }

  addClass(){
    console.log(this.name);
    console.log(this.healthMultiplier);
    console.log(this.damage);
    console.log(this.attackSpeed);
    console.log(this.description);
    if(this.name != undefined && this.healthMultiplier != undefined && this.damage != undefined && this.description != undefined && this.attackSpeed != undefined){
      this.configuredClasses.push(new ClassConfig(this.getNextFreeId(), this.name, this.healthMultiplier, this.damage, this.attackSpeed, this.description));
      this.name = undefined;
      this.healthMultiplier = undefined;
      this.damage = undefined;
      this.description = undefined;
      DungeonConfig.allClasses = this.configuredClasses;
    }else{
      window.alert("Es wurden nicht alle Werte eingegeben");
    }
  }

  getNextFreeId(): number{
    let id = 0;
    let foundId = false;
    let containsId = false;
    while(!foundId){
      for (let i = 0; i < this.configuredClasses.length; i++) {
        if(this.configuredClasses[i].id == id){
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
