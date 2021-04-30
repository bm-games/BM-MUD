import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {RaceConfig} from "../../models/RaceConfig";
import {ClassConfig} from "../../models/ClassConfig";
import {ItemConfig} from "../../models/ItemConfig";
import {EquipmentConfig} from "../../models/EquipmentConfig";
import {NPCConfig} from "../../models/NPCConfig";
import {CommandConfig} from "../../models/CommandConfig";
import {WeaponConfig} from "../../models/WeaponConfig";
import {FriendlyNPCConfig} from "../../models/FriendlyNPCConfig";
import {HostileNPCConfig} from "../../models/HostileNPCConfig";

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {
  static get allCommands(): CommandConfig[] {
    return this._allCommands;
  }

  static set allCommands(value: CommandConfig[]) {
    this._allCommands = value;
  }
  static get allNPCs(): FriendlyNPCConfig[] | HostileNPCConfig[] {
    return this._allNPCs;
  }

  static set allNPCs(value: FriendlyNPCConfig[] | HostileNPCConfig[]) {
    this._allNPCs = value;
  }

  title = "BM-MUD: Configurator";

  private static _allRaces: RaceConfig[] = [];
  private static _allClasses: ClassConfig[] = [];
  private static _allItems: ItemConfig[] | EquipmentConfig[] | WeaponConfig[] = [];
  private static _allNPCs: FriendlyNPCConfig[] | HostileNPCConfig[] = [];
  private static _allCommands: CommandConfig[] = [];

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  static get allItems(): ItemConfig[] | EquipmentConfig[] | WeaponConfig[] {
    return this._allItems;
  }

  static set allItems(value: ItemConfig[] | EquipmentConfig[] | WeaponConfig[]) {
    this._allItems = value;
  }

  public setTitle(newTitle: string){
    this.titleService.setTitle(newTitle);
  }

  static set allRaces(value: RaceConfig[]) {
    this._allRaces = value;
  }
  static get allRaces(): RaceConfig[] {
    return this._allRaces;
  }

  static get allClasses(): ClassConfig[] {
    return this._allClasses;
  }

  static set allClasses(value: ClassConfig[]) {
    this._allClasses = value;
  }

}
