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
  private static _commandConfig: CommandConfig = {aliases: {}, customCommands: {}}
  private static _allRooms: RoomConfig[] = [];
  private static _startequipment: Item[] = [];
  private static _startRoom: string;

  constructor(private configService: ConfigService,
              private titleService: Title,
              private error: FeedbackService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  /**
   * Creates a DungeonConfig and sends it to the ConfigService.
   */
  createConfig() {
    let roomMap: StringMap<RoomConfigExport> = {}
    ConfigurationComponent.allRooms.forEach(r => {
      let north = this.getRoomNameById(r.north);
      let east = this.getRoomNameById(r.east);
      let south = this.getRoomNameById(r.south);
      let west = this.getRoomNameById(r.west);
      let npcStringMap: StringMap<NPC> = {}
      r.npcs.forEach(n => {
        npcStringMap[n.name] = n
      })
      roomMap[r.name] = {
        //type: "net.bmgames.state.model.Room",
        name: r.name,
        items: r.items,
        //npcs: r.npcs,
        npcs: npcStringMap,
        neighbours: {
          North: north || undefined,
          East: east || undefined,
          South: south || undefined,
          West: west || undefined,
        },
        message: r.message
      };
    });

    let npcMap: StringMap<NPC> = {};
    ConfigurationComponent.allNPCs.forEach(n => {
      npcMap[n.name] = n;
    });
    let itemMap: StringMap<Item> = {};
    ConfigurationComponent.allItems.forEach(i => {
      itemMap[i.name] = i;
    });
    let dungeon: DungeonConfig = {
      name: this.mudName,
      startRoom: ConfigurationComponent.startRoom,
      rooms: roomMap,
      startEquipment: ConfigurationComponent.startequipment,
      npcConfigs: npcMap,
      itemConfigs: itemMap,
      races: ConfigurationComponent.allRaces,
      classes: ConfigurationComponent.allClasses,
      commandConfig: ConfigurationComponent.commandConfig
    }

    //let dungeon = JSON.parse('{"name":"Der erste echte MUD","startRoom":"Keller","rooms":{"Keller":{"name":"Keller","items":[],"npcs":{},"north":"","east":"","south":"Maschinenraum","west":"","message":"Es ist kalt. Du bist im Keller."},"Maschinenraum":{"name":"Maschinenraum","items":[{"type":"net.bmgames.state.model.Consumable","name":"Zaubertrank","effect":"Heilen"},{"type":"net.bmgames.state.model.Weapon","name":"Goldenes Schwert","damage":91},{"type":"net.bmgames.state.model.Equipment","name":"Helm","damageModifier":1,"healthModifier":7,"slot":"Head"}],"npcs":[{"name":"GeOrk","items":[{"type":"net.bmgames.state.model.Consumable","name":"Zaubertrank","effect":"Heilen"}],"messageOnTalk":"Ich bin GeOrk","commandOnInteraction":"hit","type":"net.bmgames.state.model.NPC.Friendly"},{"name":"Hecke","items":[{"type":"net.bmgames.state.model.Weapon","name":"Goldenes Schwert","damage":91},{"type":"net.bmgames.state.model.Equipment","name":"Helm","damageModifier":1,"healthModifier":7,"slot":"Head"}],"health":79,"damage":13,"type":"net.bmgames.state.model.NPC.Hostile"}],"north":"Keller","east":"","south":"","west":"","message":"Hallllloooooo."},"Himmel":{"name":"Himmel","items":[],"npcs":[{"name":"GeOrk","items":[{"type":"net.bmgames.state.model.Consumable","name":"Zaubertrank","effect":"Heilen"}],"messageOnTalk":"Ich bin GeOrk","commandOnInteraction":"hit","type":"net.bmgames.state.model.NPC.Friendly"}],"north":"","east":"","south":"","west":"","message":"the sky is the limit"}},"startEquipment":[{"type":"net.bmgames.state.model.Equipment","name":"Helm","damageModifier":1,"healthModifier":7,"slot":"Head"}],"npcConfigs":{"GeOrk":{"name":"GeOrk","items":[{"type":"net.bmgames.state.model.Consumable","name":"Zaubertrank","effect":"Heilen"}],"messageOnTalk":"Ich bin GeOrk","commandOnInteraction":"hit","type":"net.bmgames.state.model.NPC.Friendly"},"Hecke":{"name":"Hecke","items":[{"type":"net.bmgames.state.model.Weapon","name":"Goldenes Schwert","damage":91},{"type":"net.bmgames.state.model.Equipment","name":"Helm","damageModifier":1,"healthModifier":7,"slot":"Head"}],"health":79,"damage":13,"type":"net.bmgames.state.model.NPC.Hostile"}},"itemConfigs":{"Zaubertrank":{"type":"net.bmgames.state.model.Consumable","name":"Zaubertrank","effect":"Heilen"},"Goldenes Schwert":{"type":"net.bmgames.state.model.Weapon","name":"Goldenes Schwert","damage":91},"Helm":{"type":"net.bmgames.state.model.Equipment","name":"Helm","damageModifier":1,"healthModifier":7,"slot":"Head"}},"races":[{"name":"Kobold","health":54,"damageModifier":2,"description":"blablabla"},{"name":"Zwerg","health":65,"damageModifier":4,"description":"sgd"}],"classes":[{"name":"Ork","healthMultiplier":5.5,"damage":22,"attackSpeed":52,"description":"sdfg"},{"name":"Drache","healthMultiplier":6.5,"damage":30,"attackSpeed":32,"description":"sdfg"}],"commandConfig":{"aliases":{"pickup":"pickup","consume":"consume","show inventory":"show inventory","go":"go","look":"look"},"customCommands":{"fly":"move player north, look, pickup, "}}}\n')

    this.configService.createDungeon(dungeon)
      .then(() => this.router.navigateByUrl('/dashboard'))
      .catch(error => this.error.showError(error));
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

}

export interface RoomConfigExport {
  //readonly type: 'net.bmgames.state.model.Room';
  name: string;
  message: string;
  neighbours: {
    North?: string,
    West?: string,
    East?: string,
    South?: string,
  }
  npcs: StringMap<NPC>;
  items: Item[];
}

type Direction = "North" | "West" | "East" | "South"
