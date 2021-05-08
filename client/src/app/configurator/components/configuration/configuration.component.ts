import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {RaceConfig} from "../../models/RaceConfig";
import {ClassConfig} from "../../models/ClassConfig";
import {ConsumableItemConfig, Item} from "../../models/Item";
import {EquipmentConfig} from "../../models/EquipmentConfig";
import {CommandConfig} from "../../models/CommandConfig";
import {WeaponConfig} from "../../models/WeaponConfig";
import {FriendlyNPCConfig} from "../../models/FriendlyNPCConfig";
import {HostileNPCConfig} from "../../models/HostileNPCConfig";
import {NPC} from "../../models/NPCConfig";
import {RoomConfig} from "../../models/RoomConfig";
import {DungeonConfig} from "../../models/DungeonConfig";
import {ConfigService} from "../../services/config.service";
import {Router} from "@angular/router";
import {JsonObject, JsonValue} from "@angular/compiler-cli/ngcc/src/packages/entry_point";

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
  private static _allItems: Item[] = [];
  private static _allNPCs: NPC[] = [];
  private static _commandConfig: CommandConfig = { aliases: new Map<string, string>(), customCommands: new Map<string, string>()}
  private static _allRooms: RoomConfig[] = [];
  private static _startequipment: Item[] = [];
  private static _startRoom: string;

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
        type: "net.bmgames.state.model.Room",
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

    let npcMap = new Map<string, NPC>();
    ConfigurationComponent.allNPCs.forEach(n => {
      npcMap.set(n.name, n);
    });
    let itemMap = new Map<string, Item>();
    ConfigurationComponent.allItems.forEach(i => {
      itemMap.set(i.name, i);
    });
    let dungeon: DungeonConfig = {
      name: this.mudName,
      startRoom: ConfigurationComponent.startRoom,
      rooms: roomMap,
      startEquipment: ConfigurationComponent.startequipment,
      //npcConfigs: ConfigurationComponent.allNPCs,
      npcConfigs: npcMap,
      //itemConfigs: ConfigurationComponent.allItems,
      itemConfigs: itemMap,
      races: ConfigurationComponent.allRaces,
      classes: ConfigurationComponent.allClasses,
      commandConfig: ConfigurationComponent.commandConfig
    }

    //console.log(JSON.stringify(dungeon, this.mapToJson))
    console.log(JSON.stringify(dungeon))

    // language=JSON
    //let dungeonClient = JSON.parse('{\n  "name": "sdfg",\n  "startRoom": "",\n  "rooms": {},\n  "startEquipment": [\n    {\n      "type": "net.bmgames.state.model.Consumable",\n      "name": "Apfel",\n      "effect": "heal $player 10"\n    },\n    {\n      "type": "net.bmgames.state.model.Equipment",\n      "name": "Diamond Helmet",\n      "healthModifier": 10.0,\n      "damageModifier": 1.0,\n      "slot": "Head"\n    },\n    {\n      "type": "net.bmgames.state.model.Weapon",\n      "name": "Wooden Sword",\n      "damage": 1\n    }\n  ],\n  "npcConfigs": {},\n  "itemConfigs": {},\n  "races": [\n    {\n      "name": "sdfgsdfg",\n      "health": 26,\n      "damageModifier": 6.5,\n      "description": "sdfg"\n    }\n  ],\n  "classes": [\n    {\n      "name": "sdfg",\n      "healthMultiplier": 4.5,\n      "damage": 53,\n      "attackSpeed": 32,\n      "description": "sdfg"\n    }\n  ],\n  "commandConfig": {\n    "aliases": {},\n    "customCommands": {}\n  }\n}')
    /*let dungeon = JSON.parse(`{
    "name": "Test game",
    "races": [
        {
            "name": "race",
            "description": "Its a race",
            "health": 10,
            "damageModifier": 1.5
        }
    ],
    "classes": [
        {
            "name": "class",
            "description": "Its a class",
            "healthMultiplier": 1.5,
            "damage": 10,
            "attackSpeed": 1
        }
    ],
    "commandConfig": {
    },
    "npcConfigs": {
        "geork": {
            "type": "net.bmgames.state.model.NPC.Friendly",
            "name": "geork",
            "items": [
                {
                    "type": "net.bmgames.state.model.Consumable",
                    "name": "Apfel",
                    "effect": "heal $player 10"
                }
            ],
            "commandOnInteraction": "heal $player 1",
            "messageOnTalk": "Halloooooooooo"
        },
        "georkina": {
            "type": "net.bmgames.state.model.NPC.Hostile",
            "name": "georkina",
            "items": [
                {
                    "type": "net.bmgames.state.model.Equipment",
                    "name": "Diamond Helmet",
                    "healthModifier": 10.0,
                    "damageModifier": 1.0,
                    "slot": "Head"
                }
            ],
            "health": 100,
            "damage": 100000
        }
    },
    "itemConfigs": {
        "Apfel": {
            "type": "net.bmgames.state.model.Consumable",
            "name": "Apfel",
            "effect": "heal $player 10"
        },
        "Diamond Helmet": {
            "type": "net.bmgames.state.model.Equipment",
            "name": "Diamond Helmet",
            "healthModifier": 10.0,
            "damageModifier": 1.0,
            "slot": "Head"
        },
        "Wooden Sword": {
            "type": "net.bmgames.state.model.Weapon",
            "name": "Wooden Sword",
            "damage": 1
        }
    },
    "startEquipment": [
        {
            "type": "net.bmgames.state.model.Consumable",
            "name": "Apfel",
            "effect": "heal $player 10"
        },
        {
            "type": "net.bmgames.state.model.Equipment",
            "name": "Diamond Helmet",
            "healthModifier": 10.0,
            "damageModifier": 1.0,
            "slot": "Head"
        },
        {
            "type": "net.bmgames.state.model.Weapon",
            "name": "Wooden Sword",
            "damage": 1
        }
    ],
    "startRoom": "start",
    "rooms": {
        "start": {
            "name": "Start room",
            "message": "Welcome!",
            "south": "room",
            "items": [
                {
                    "type": "net.bmgames.state.model.Consumable",
                    "name": "Apfel",
                    "effect": "heal $player 10"
                },
                {
                    "type": "net.bmgames.state.model.Equipment",
                    "name": "Diamond Helmet",
                    "healthModifier": 10.0,
                    "damageModifier": 1.0,
                    "slot": "Head"
                },
                {
                    "type": "net.bmgames.state.model.Weapon",
                    "name": "Wooden Sword",
                    "damage": 1
                }
            ],
            "npcs": {
                "georkina": {
                    "type": "net.bmgames.state.model.NPC.Hostile",
                    "name": "georkina",
                    "items": [
                        {
                            "type": "net.bmgames.state.model.Equipment",
                            "name": "Diamond Helmet",
                            "healthModifier": 10.0,
                            "damageModifier": 1.0,
                            "slot": "Head"
                        }
                    ],
                    "health": 100,
                    "damage": 100000
                }
            }
        },
        "room": {
            "name": "Next room",
            "message": "HIIIIIIIIIIIIIIIIIII!",
            "north": "start"
        }
    }
}`)*/
    this.configService.createDungeon(dungeon).then(() => this.router.navigateByUrl('/dashboard')).catch(({error}) => alert(error));
  }

  /*mapToJson(key: any, value: any){
    if(value instanceof Map){
      return Array.from(value.entries())
    }
    return value
  }*/

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
  readonly type: 'net.bmgames.state.model.Room';
  name: string;
  message: string;
  north?: string;
  east?: string;
  south?: string;
  west?: string;
  items: Item[];
  npcs: Map<String, NPC>;
}
