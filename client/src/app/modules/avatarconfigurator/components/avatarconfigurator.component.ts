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
  isArcher: boolean = true;

  selectedRace: string = "Ork";
  selectedClass: string = "Berserker";

  classTypes: string[] = ["Berserker", "Bogenschütze"];
  raceTypes: string[] = ["Ork", "Elf"]

  constructor() { }

  ngOnInit(): void {
  }

  raceChanged(race: string) {
    switch (race) {
      case 'Ork':
        this.isOrk = true;
        this.isElf = false;
        break;
      case 'Elf':
        this.isElf = true;
        this.isOrk = false;

    }
  }

  classChanged(classType: string) {
    switch (classType) {
      case 'Berserker':
        this.isBerserker = true;
        this.isArcher = false;
        break;
      case 'Bogenschütze':
        this.isArcher = true;
        this.isBerserker = false;

    }
  }

}
