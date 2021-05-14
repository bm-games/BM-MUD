import { Component, OnInit } from '@angular/core';
import {DungeonConfig} from '../../configurator/models/DungeonConfig';
import {RaceConfig} from '../../configurator/models/RaceConfig';
import {ClassConfig} from '../../configurator/models/ClassConfig';

@Component({
  selector: 'app-avatarconfigurator',
  templateUrl: './avatarconfigurator.component.html',
  styleUrls: ['./avatarconfigurator.component.scss']
})
export class AvatarconfiguratorComponent implements OnInit {


  currentConfig: DungeonConfig; //=getConfigOrWhatEverNichtMeinJobDigga;~Leon, 2021;  @Jakub ~Marius, 2021
  raceConfigs: RaceConfig[] = this.currentConfig.races;
  classConfigs: ClassConfig[] = this.currentConfig.classes;

  damage = 0;
  health = 0;

  rawdamage: number | undefined;
  rawhealth: number | undefined;

  damageMultiplier: number | undefined;
  healthMultiplier: number | undefined;

  selectedRace = '';
  selectedClass = '';

  classTypes: string[] = [];
  raceTypes: string[] = [];

  constructor() { }

  ngOnInit(): void {
    //getDungeonConfigOderSoIGuess
    this.getClassesAndRaces();
    this.setRaceInitial();
    this.setClassInitial();
    this.calculateInitialStats();
  }

  setRaceInitial(): void {
    this.selectedRace = this.raceConfigs[0].name;
    this.rawhealth = this.raceConfigs[0].health;
    this.damageMultiplier = this.raceConfigs[0].damageModifier;
  }

  setClassInitial(): void {
    this.selectedClass = this.classConfigs[0].name;
    this.healthMultiplier = this.classConfigs[0].healthMultiplier;
    this.rawdamage = this.classConfigs[0].damage;
  }

  calculateInitialStats(): void{
    if (this.rawhealth == undefined || this.rawdamage == undefined
      || this.healthMultiplier == undefined || this.damageMultiplier == undefined) {
      return;
    }
    this.health = this.rawhealth * this.healthMultiplier;
    this.damage = this.damageMultiplier * this.rawdamage;
  }

  getClassesAndRaces(): void {
    this.raceConfigs = this.currentConfig.races;
    this.classConfigs = this.currentConfig.classes;
  }

  raceChanged(race: string) {
    for (let i = 0; i < this.raceConfigs.length; i++){
      if (this.raceConfigs[i].name == race){
        this.damageMultiplier = this.raceConfigs[i].damageModifier;
        this.rawhealth = this.raceConfigs[i].health;
        this.health = this.rawhealth * this.healthMultiplier;
        this.selectedRace = this.raceConfigs[i].name;
      }
    }
    return;
  }



  classChanged(classType: string) {
    for (let i = 0; i < this.classConfigs.length; i++) {
      if (this.classConfigs[i].name == classType){
        this.rawdamage = this.classConfigs[i].damage;
        this.healthMultiplier = this.classConfigs[i].healthMultiplier;
        this.damage = this.rawdamage * this.damageMultiplier;
        this.health = this.rawhealth * this.healthMultiplier;
      }
    }
    return;
  }

}
