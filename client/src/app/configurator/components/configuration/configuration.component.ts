import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {RaceConfig} from "../../models/RaceConfig";
import {ClassConfig} from "../../models/ClassConfig";
import {ItemConfig} from "../../models/ItemConfig";
import {EquipmentConfig} from "../../models/EquipmentConfig";
import {CommandConfig} from "../../models/CommandConfig";
import {WeaponConfig} from "../../models/WeaponConfig";
import {FriendlyNPCConfig} from "../../models/FriendlyNPCConfig";
import {HostileNPCConfig} from "../../models/HostileNPCConfig";
import {RoomConfig} from "../../models/RoomConfig";
import {DungeonConfig} from "../../models/DungeonConfig";
import {race} from "rxjs";

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {

  title = "BM-MUD: Configurator";
  mudName: string = '';

  private static _allRaces: RaceConfig[] = [];
  private static _allClasses: ClassConfig[] = [];
  private static _allItems: ItemConfig[] | EquipmentConfig[] | WeaponConfig[] = [];
  private static _allNPCs: (FriendlyNPCConfig | HostileNPCConfig)[] = [];
  private static _allCommands: CommandConfig[] = [];
  private static _allRooms: RoomConfig[] = [];
  private static _startequipment: ItemConfig[] = [];
  private static _startRoom: number = 0;

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  createConfig(){
    //let startequipmentIDs: number[] = [];
    //ConfigurationComponent.startequipment.forEach(s => startequipmentIDs.push(s.id));
    //let itemIDs: number[] = [];
    //ConfigurationComponent.allItems.forEach(i => itemIDs.push(i.id));
    //let npcIDs: number[] = [];
    //ConfigurationComponent.allNPCs.forEach(n => npcIDs.push(n.id));
    //let raceIDs: number[] = [];
    //ConfigurationComponent.allRaces.forEach(r => raceIDs.push(r.id));
    //let classIDs: number[] = [];
    //ConfigurationComponent.allClasses.forEach((c => classIDs.push(c.id)));
    //let commandIDs: number[] = [];
    //ConfigurationComponent.allCommands.forEach(c => commandIDs.push(c.id));
    let dungeon: DungeonConfig = {
      name: this.mudName,
      startRoom: ConfigurationComponent.startRoom,
      startEquipment: ConfigurationComponent.startequipment,
      npcs: ConfigurationComponent.allNPCs,
      items: ConfigurationComponent.allItems,
      races: ConfigurationComponent.allRaces,
      classes: ConfigurationComponent.allClasses,
      commands: ConfigurationComponent.allCommands
    }
    console.log(dungeon);
  }

  static get startRoom(): number {
    return this._startRoom;
  }

  static set startRoom(value: number) {
    this._startRoom = value;
  }
  static get startequipment(): ItemConfig[] {
    return this._startequipment;
  }

  static set startequipment(value: ItemConfig[]) {
    this._startequipment = value;
  }

  static get allRooms(): RoomConfig[] {
    return this._allRooms;
  }

  static set allRooms(value: RoomConfig[]) {
    this._allRooms = value;
  }
  static get allCommands(): CommandConfig[] {
    return this._allCommands;
  }

  static set allCommands(value: CommandConfig[]) {
    this._allCommands = value;
  }
  static get allNPCs(): (FriendlyNPCConfig | HostileNPCConfig)[] {
    return this._allNPCs;
  }

  static set allNPCs(value: (FriendlyNPCConfig | HostileNPCConfig)[]) {
    this._allNPCs = value;
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
