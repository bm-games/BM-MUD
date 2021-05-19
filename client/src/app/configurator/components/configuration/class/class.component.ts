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
    if(this.name != undefined && this.name.trim() != '' && this.healthMultiplier != undefined && this.damage != undefined && this.description != undefined && this.description.trim() != '' && this.attackSpeed != undefined){
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

  /**
   * Removes the selected class from the DungeonConfig
   * @param className name of the class you want to remove
   */
  deleteClass(className: string){
    let index = this.configuredClasses.findIndex(c => c.name == className)
    if(index > -1){
      this.configuredClasses.splice(index, 1)
    }
  }

  sliderValue(value: number) {
    return value;
  }

}
