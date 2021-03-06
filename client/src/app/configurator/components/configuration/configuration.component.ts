import {Component, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {RaceConfig} from "../../models/RaceConfig";
import {ClassConfig} from "../../models/ClassConfig";
import {Item} from "../../models/Item";
import {CommandConfig} from "../../models/CommandConfig";
import {NPC} from "../../models/NPCConfig";
import {RoomConfig} from "../../models/RoomConfig";
import {DungeonConfig, StringMap} from "../../models/DungeonConfig";
import {ConfigService} from "../../services/config.service";
import {Router} from "@angular/router";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {config} from "rxjs";

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {


  title = "Konfigurator";
  mudName: string = '';

  private static _id?: string;
  private static _allRaces: RaceConfig[] = [];
  private static _allClasses: ClassConfig[] = [];
  private static _allItems: Item[] = [];
  private static _allNPCs: NPC[] = [];
  private static _commandConfig: CommandConfig = {aliases: {}, customCommands: {}}
  private static _allRooms: RoomConfig[] = [];
  private static _startequipment: Item[] = [];
  private static _startRoom: string;

  constructor(private configService: ConfigService,
              private titleService: Title,
              private feedback: FeedbackService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  /**
   * Creates a DungeonConfig and sends it to the ConfigService.
   * @param draft If its saved as a draft or final version
   */
  createConfig(draft: boolean) {
    let roomMap: StringMap<RoomConfigExport> = {}
    ConfigurationComponent.allRooms.forEach(r => {
      let north = this.getRoomNameById(r.north);
      let east = this.getRoomNameById(r.east);
      let south = this.getRoomNameById(r.south);
      let west = this.getRoomNameById(r.west);
      let npcStringMap: StringMap<NPC> = {}
      r.npcs.forEach(n => {
        npcStringMap[n.name.replace(/ /g, '_')] = {
          ...n,
          name: n.name.replace(' ', '_')
        }
      })
      roomMap[r.name.replace(' ', '_')] = {
        //type: "net.bmgames.state.model.Room",
        name: r.name.replace(' ', '_'),
        items: r.items.map(item => ({
          ...item,
          name: item.name.replace(' ', '_')
        })),
        //npcs: r.npcs,
        npcs: npcStringMap,
        neighbours: {
          NORTH: north || undefined,
          EAST: east || undefined,
          SOUTH: south || undefined,
          WEST: west || undefined,
        },
        message: r.message
      };
    });

    let npcMap: StringMap<NPC> = {};
    ConfigurationComponent.allNPCs.forEach(n => {
      npcMap[n.name.replace(' ', '_')] = {
        ...n,
        name: n.name.replace(' ', '_'
        )
      }
    });
    let itemMap: StringMap<Item> = {};
    ConfigurationComponent.allItems.forEach(i => {
      itemMap[i.name.replace(' ', '_')] = {
        ...i,
        name: i.name.replace(' ', '_')
      }
    });
    let dungeon: DungeonConfig = {
      id: ConfigurationComponent._id,
      name: this.mudName,
      startRoom: ConfigurationComponent.startRoom,
      rooms: roomMap,
      startEquipment: ConfigurationComponent.startequipment,
      npcConfigs: npcMap,
      itemConfigs: itemMap,
      races: ConfigurationComponent.allRaces,
      classes: ConfigurationComponent.allClasses,
      commandConfig: ConfigurationComponent.commandConfig
    };

    this.feedback.showLoadingOverlay()
    if (draft) {
      this.configService.saveDraft(dungeon)
      this.feedback.stopLoadingOverlay()
      this.router.navigateByUrl('/dashboard')
    } else {
      this.configService.createDungeon(dungeon)
        .then(() => {
          this.resetAllLists()
          this.configService.deleteDraft(dungeon.id)
          this.router.navigateByUrl('/dashboard')
        })
        .catch(error => this.feedback.showError(error))
        .finally(() => this.feedback.stopLoadingOverlay());
    }

  }

  getRoomNameById(id: number | undefined): string {
    if (id != undefined && id > -1) {
      let room;
      for (let i = 0; i < ConfigurationComponent.allRooms.length; i++) {
        if (ConfigurationComponent.allRooms[i].id == id) room = ConfigurationComponent.allRooms[i];
      }
      if (room != undefined) return room.name;
      else return '';
    }
    return '';
  }

  quitConfigurator() {
    if (confirm("Willst du den Konfigurator wirklich verlassen? Deine eingegebenen Daten gehen verloren")) {
      this.resetAllLists()
      this.router.navigateByUrl('/dashboard')
    }
  }

  resetAllLists() {
    ConfigurationComponent._id = undefined
    ConfigurationComponent.allRaces = []
    ConfigurationComponent.allClasses = []
    ConfigurationComponent.commandConfig = {customCommands: {}, aliases: {}}
    ConfigurationComponent.allItems = []
    ConfigurationComponent.allNPCs = []
    ConfigurationComponent.allRooms = []
  }

  static get startRoom(): string {
    return this._startRoom;
  }

  static set startRoom(value: string) {
    this._startRoom = value;
  }

  static get startequipment(): Item[] {
    return this._startequipment;
  }

  static set startequipment(value: Item[]) {
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

  static get allNPCs(): NPC[] {
    return this._allNPCs;
  }

  static set allNPCs(value: NPC[]) {
    this._allNPCs = value;
  }

  static get allItems(): Item[] {
    return this._allItems;
  }

  static set allItems(value: Item[]) {
    this._allItems = value;
  }

  public setTitle(newTitle: string) {
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


  static setConfig(config: Partial<DungeonConfig>) {
    this._id = config.id
    if (config.races) this.allRaces = config.races
    if (config.classes) this.allClasses = config.classes
    if (config.itemConfigs) this.allItems = Object.values(config.itemConfigs)
    if (config.npcConfigs) this.allNPCs = Object.values(config.npcConfigs)
    if (config.commandConfig) this.commandConfig = config.commandConfig
    let north: number
    let east: number
    let south: number
    let west: number
    if (config.rooms) Object.values(config.rooms).forEach(room => {
      if (room.neighbours.NORTH == undefined) {
        north = -1
      } else {
        north = 1
      }
      if (room.neighbours.EAST == undefined) {
        east = -1
      } else {
        east = 1
      }
      if (room.neighbours.SOUTH == undefined) {
        south = -1
      } else {
        south = 1
      }
      if (room.neighbours.WEST == undefined) {
        west = -1
      } else {
        west = 1
      }
    })
    if (config.rooms) this.allRooms = Object.values(config.rooms)
      .map<RoomConfig>(({name, message, npcs, items}, index) =>
        ({
          id: index,
          name,
          message,
          npcs: Object.values(npcs),
          items,
          north: north,
          west: west,
          south: south,
          east: east
        }))
    if (config.startEquipment) this.startequipment = config.startEquipment
    if (config.startRoom) this.startRoom = config.startRoom
  }
}

export interface RoomConfigExport {
  //readonly type: 'net.bmgames.state.model.Room';
  name: string;
  message: string;
  neighbours: {
    NORTH?: string,
    WEST?: string,
    EAST?: string,
    SOUTH?: string,
  }
  npcs: StringMap<NPC>;
  items: Item[];
}

type Direction = "North" | "West" | "East" | "South"
