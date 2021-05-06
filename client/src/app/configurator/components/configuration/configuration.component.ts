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
import {ConfigService} from "../../services/config.service";
import {Router} from "@angular/router";

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
  private static _commandConfig: CommandConfig = { aliases: new Map<string, string>(), customCommands: new Map<string, string>()}
  private static _allRooms: RoomConfig[] = [];
  private static _startequipment: ItemConfig[] = [];
  private static _startRoom: number = 0;

  constructor(private configService: ConfigService, private titleService: Title, private router: Router) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  /**
   * Creates a DungeonConfig and sends it to the ConfigService.
   */
  createConfig(){
    let roomMap = new Map<string, RoomConfigExport>();
    ConfigurationComponent.allRooms.forEach(r => {
      let north = this.getRoomNameById(r.north);
      let east = this.getRoomNameById(r.east);
      let south = this.getRoomNameById(r.south);
      let west = this.getRoomNameById(r.west);
      let roomConfig: RoomConfigExport = {
        name: r.name,
        items: r.items,
        npcs: r.npcs,
        north: north,
        east: east,
        south: south,
        west: west,
        message: r.message
      }

      roomMap.set(r.name, roomConfig);
    });


    let dungeon: DungeonConfig = {
      name: this.mudName,
      startRoom: ConfigurationComponent.startRoom,
      rooms: roomMap,
      startEquipment: ConfigurationComponent.startequipment,
      npcs: ConfigurationComponent.allNPCs,
      items: ConfigurationComponent.allItems,
      races: ConfigurationComponent.allRaces,
      classes: ConfigurationComponent.allClasses,
      commands: ConfigurationComponent.commandConfig
    }
    this.configService.createDungeon(dungeon).then(() => this.router.navigateByUrl('/dashboard')).catch(({error}) => alert(error));
  }

  getRoomNameById(id: number | undefined): string {
    if(id != undefined && id > -1){
      let room;
      for (let i = 0; i < ConfigurationComponent.allRooms.length; i++) {
        if(ConfigurationComponent.allRooms[i].id == id) room = ConfigurationComponent.allRooms[i];
      }
      if(room != undefined) return room.name;
      else return '';
    }
    return '';
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
  static get commandConfig(): CommandConfig {
    return this._commandConfig;
  }

  static set commandConfig(value: CommandConfig) {
    this._commandConfig = value;
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

export interface RoomConfigExport{
  name: string;
  north: string;
  east: string;
  south: string;
  west: string;
  items: string[];
  npcs: string[];
  message: string;
}
