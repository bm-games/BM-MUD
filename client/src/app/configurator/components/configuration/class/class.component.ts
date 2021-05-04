import { Component, OnInit } from '@angular/core';
import {ClassConfig} from "../../../models/ClassConfig";
import {ConfigurationComponent} from "../configuration.component";

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
    this.configuredClasses = ConfigurationComponent.allClasses;
  }

  /**
   * Generates a ClassConfig with the current UI-data inputs and adds it to the list 'configuredClasses'
   */
  addClass(){
    console.log(this.name);
    console.log(this.healthMultiplier);
    console.log(this.damage);
    console.log(this.attackSpeed);
    console.log(this.description);
    if(this.name != undefined && this.healthMultiplier != undefined && this.damage != undefined && this.description != undefined && this.attackSpeed != undefined){
      //this.configuredClasses.push(new ClassConfig(this.getNextFreeId(), this.name, this.healthMultiplier, this.damage, this.attackSpeed, this.description));

      this.configuredClasses.push({
        id: this.getNextFreeId(),
        name: this.name,
        healthMultiplier: this.healthMultiplier,
        damage: this.damage,
        attackSpeed: this.attackSpeed,
        description: this.description
      });

      this.name = undefined;
      this.healthMultiplier = undefined;
      this.damage = undefined;
      this.description = undefined;
      ConfigurationComponent.allClasses = this.configuredClasses;
    }else{
      window.alert("Es wurden nicht alle Werte eingegeben");
    }
  }

  /**
   * Finds next smallest possible ID for the new ClassConfig
   * @returns id: number
   */
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
