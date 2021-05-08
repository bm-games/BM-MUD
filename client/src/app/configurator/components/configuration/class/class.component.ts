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
      if(!this.checkContainsName()){
        this.configuredClasses.push({
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
        window.alert("Es gibt bereits eine Klasse mit diesem Namen: " + this.name);
      }
    }else{
      window.alert("Es wurden nicht alle Werte eingegeben");
    }
  }

  checkContainsName(): boolean{
    for (let i = 0; i < this.configuredClasses.length; i++) {
       if(this.configuredClasses[i].name == this.name){
         return true;
       }
     }
    return false;
  }

  sliderValue(value: number) {
    return value;
  }

}
