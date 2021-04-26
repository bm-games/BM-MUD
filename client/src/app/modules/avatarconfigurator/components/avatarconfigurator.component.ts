import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-avatarconfigurator',
  templateUrl: './avatarconfigurator.component.html',
  styleUrls: ['./avatarconfigurator.component.scss']
})
export class AvatarconfiguratorComponent implements OnInit {

  isOrk: boolean = true;
  isElf: boolean = false;
  isBerserker: boolean = true;
  isArcher: boolean = false;

  damage: number = 0;
  health: number = 0;

  rawdamage: number | undefined;
  rawhealth: number | undefined;

  damageMultiplier: number | undefined;
  healthMultiplier: number | undefined;

  selectedRace: string = "Ork";
  selectedClass: string = "Berserker";

  classTypes: string[] = ["Berserker", "Bogenschütze"];
  raceTypes: string[] = ["Ork", "Elf"]

  constructor() { }

  ngOnInit(): void {
    this.raceChanged("Ork");
    this.classChanged("Berserker");
  }

  raceChanged(race: string) {
    switch (race) {
      case 'Ork':
        this.isOrk = true;
        this.isElf = false;
        this.rawdamage = 10;
        this.rawhealth = 100;
        // @ts-ignore
        this.damage = this.rawdamage * this.damageMultiplier;
        // @ts-ignore
        this.health = this.rawhealth * this.damageMultiplier;
        break;
      case 'Elf':
        this.isElf = true;
        this.isOrk = false;
        this.rawdamage = 20;
        this.rawhealth = 50;
        // @ts-ignore
        this.damage = this.rawdamage * this.damageMultiplier;
        // @ts-ignore
        this.health = this.rawhealth * this.damageMultiplier;
        break;
    }
  }

  classChanged(classType: string) {
    switch (classType) {
      case 'Berserker':
        this.isBerserker = true;
        this.isArcher = false;
        this.healthMultiplier = 1.2;
        this.damageMultiplier = 1.2;
        // @ts-ignore
        this.damage = this.rawdamage * this.damageMultiplier;
        // @ts-ignore
        this.health = this.rawhealth * this.healthMultiplier;
        console.log("Berserker");
        break;
      case 'Bogenschütze':
        this.isArcher = true;
        this.isBerserker = false;
        this.healthMultiplier = 1;
        this.damageMultiplier = 1.6;
        // @ts-ignore
        this.damage = this.rawdamage * this.damageMultiplier;
        // @ts-ignore
        this.health = this.rawhealth * this.healthMultiplier;
        break;
    }
  }

}
