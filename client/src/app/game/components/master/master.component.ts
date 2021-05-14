import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {RoomConfig} from "../../../configurator/models/RoomConfig";
import {NPC} from "../../../configurator/models/NPCConfig";
import {Item} from "../../../configurator/models/Item";
import {CommandService, SocketConnection} from "../../services/command.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatMessage} from "../chat/chat.component";
import {RoomMap, Tile} from "../../../shared/model/map";


@Component({
  selector: 'app-game',
  templateUrl: './master.component.html',
  styleUrls: ['./master.component.scss']
})
export class MasterComponent implements OnInit {

  echoCmd = new Subject<string>()
  echoChat = new Subject<string>()

  private connection!: SocketConnection;
  game: string = "MUD"
  commands = new Subject<string>()
  chat = new Subject<ChatMessage>()

  selectedGridValueIndex: number = 0;
  selectedRoomName: string = '';
  selectedRoomMessage: string = '';
  selectedRoomNPCs: NPC[] = [{ name: 'npc1', commandOnInteraction: '', messageOnTalk: 'hallo', items: [], type: "net.bmgames.state.model.NPC.Friendly"}, { name: 'npc2', health: 3, damage: 3, items: [], type: "net.bmgames.state.model.NPC.Hostile"}]
  npcsToAdd: NPC[] = [];
  selectedRoomItems: Item[] = []
  itemsToAdd: Item[] = []
  selectedRoomPlayers: string[] = ['Hans', 'Martin', 'Peter'];
  selectedPlayerInRoom: string | undefined;
  onlinePlayers: string[] = ['Hans', 'Martin', 'Peter'];
  commandsOnPlayer: string[] = ['Spieler rauswerfen', 'Spieler teleportieren', 'Charakter LP abziehen']
  playerToInviteName: string | undefined;
  selectedCommandOnPlayer!: string;
  selectedRoomToTeleportTo!: string;
  allDungeonNPCs: NPC[] = [];
  allDungeonItems: Item[] = [];
  allDungeonRooms: string[] = [];

  disableNewRoomTab: boolean = true;
  selectedTabIndexRoomInformation: number = 1;

  map: RoomMap = {tiles: [[null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: [], npcs: [], name: '' }],
      [null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: [], npcs: [], name: '' }]] }       // map.tiles[i][j] -> gridValue[mapColumns * i + j]

  //Grid
  // -> neighbours of a gridValue are: index -> [-1],[+1],[-mapColumns},[+mapColumns]
  mapColumns = 8;
  grid: Array<gridValue> = [];

  constructor(private commandService: CommandService,
              route: ActivatedRoute,
              router: Router) {
    const game = route.snapshot.paramMap.get("game")
    if (!game) {
      //router.navigateByUrl("/")
    } else {
      this.game = game
      this.connection = this.commandService.connectMaster(this.game)
      this.connection.incoming.subscribe(msg => {
        switch (msg.type) {
          case "net.bmgames.game.message.Message.Text":
            this.commands.next(msg.text)
            break;
          case "net.bmgames.game.message.Message.Map":
            console.log(msg)
            //TODO update map
            break;
          case "net.bmgames.game.message.Message.Kick":
            console.log(msg)
            //TODO disconnect from game
            break;
          case "net.bmgames.game.message.Message.Chat":
            this.chat.next({msg: msg.message, senderOrRecipient: msg.sender})
            break
        }
      })
    }
  }

  ngOnInit(): void {
    // initialize grid with rooms
    this.mapColumns = this.map.tiles[0].length
    /*for(let i = 0; i<this.mapColumns*this.mapColumns; i++){
      this.grid[i] = {
        index: i,
        value: null,
        color: "#C0C0C0"
      }
    }*/

    for (let i = 0; i < this.map.tiles[0].length; i++) {
      for (let j = 0; j < this.map.tiles[1].length; j++) {
        let tile = this.map.tiles[i][j]
        let gridIndex = this.mapColumns * i + j;
        console.log(gridIndex)
        console.log(tile)
        if(tile != null){
          this.grid[gridIndex] = {index: gridIndex, value: tile, color: tile.color}
        }else{
          this.grid[gridIndex] = {index: gridIndex, value: null, color: '#C0C0C0'}
        }
      }
    }
    console.log("test")
  }

  /**
   * Removes the selected NPC from the room. The NPC is still available in the list allDungeonNPCs
   */
  removeNPCFromRoom(){
    console.log(this.selectedGridValueIndex)
    //save this.selectedRoomNPCs to the room npcs
    //TODO
  }

  /**
   * Adds the selected NPCs to the selected room
   */
  addNPCToRoom(){
    console.log(this.npcsToAdd)
    this.npcsToAdd.forEach(n => {
      if(!this.checkSelectedRoomContainsNPC(n.name)){
        this.selectedRoomNPCs.push(n)
      }
    })
    this.npcsToAdd = []

    //save this.selectedRoomNPCs to the rooms npcs
    //TODO
  }

  checkSelectedRoomContainsNPC(npcName: string) : boolean{
    for (let i = 0; i < this.selectedRoomNPCs.length; i++) {
      if(this.selectedRoomNPCs[i].name == npcName) return true
    }
    return false
  }

  checkSelectedRoomContainsItem(itemName: string) : boolean{
    for (let i = 0; i < this.selectedRoomItems.length; i++) {
      if(this.selectedRoomItems[i].name == itemName) return true
    }
    return false
  }

  /**
   * Removes the selected NPC from the room. The NPC is still available in the list allDungeonNPCs
   */
  removeItemFromRoom(){
    console.log(this.selectedGridValueIndex)
    //save this.selectedRoomItems to the room items (send command)
    //TODO
  }

  /**
   * Adds the selected NPC to the selected room
   */
  addItemToRoom(){
    console.log(this.itemsToAdd)
    this.itemsToAdd.forEach(n => {
      if(!this.checkSelectedRoomContainsItem(n.name)){
        this.selectedRoomItems.push(n)
      }
    })
    this.itemsToAdd = []

    //save this.selectedRoomNPCs to the rooms items
    //TODO
  }

  /**
   * Sends a kick command to the server which kicks the player out of the dungeon
   * @param playerName username of the player to kick out of the dungeon
   */
  kickPlayer(playerName?: string){
    console.log(this.selectedPlayerInRoom)
    //send kick command
    //TODO
  }

  /**
   * Sends a teleport command to the server which teleports the player to the selected room
   */
  teleportPlayer(){
    console.log(this.selectedPlayerInRoom)
    console.log(this.selectedRoomToTeleportTo)
    //TODO
  }

  /**
   * Hits a player and decrements his health
   * @param damage the number healthpoints that are going to be subtracted from players health
   */
  hitPlayer(damage: number | null){
    console.log(this.selectedPlayerInRoom)
    console.log(damage)
    //TODO
  }

  /**
   * Deletes the selected room
   * This is only possible, if the selected room doesn't contains any characters
   */
  deleteRoom(){
    console.log(this.selectedGridValueIndex)
    //TODO
  }

  /**
   * Creates a new room and adds it at the selected position to the dungeon
   * It sets the input values name, description, NPCs and Items
   */
  addNewRoom(){
    console.log(this.selectedRoomName)
    console.log(this.selectedRoomMessage)
    console.log(this.selectedRoomNPCs)
    console.log(this.selectedRoomItems)

    this.selectedRoomName = ''
    this.selectedRoomMessage = ''
    this.selectedRoomNPCs = []
    this.selectedRoomItems = []
    //TODO
  }

  /**
   * Sends a invitePlayer command to the server to send an invitation to the player
   */
  invitePlayer(){
    console.log("Invite player: " + this.playerToInviteName)
    this.playerToInviteName = undefined
    //TODO
  }

  gridRoomSelected(gridV: gridValue){
    this.selectedGridValueIndex = gridV.index;
    this.setInputValuesToSelected(this.selectedGridValueIndex);
    this.highlightSelectedValue(this.selectedGridValueIndex);
    console.log(this.grid[this.selectedGridValueIndex].value);
  }

  setInputValuesToSelected(index: number){
    let name = this.grid[index].value?.name;
    let npcs = this.grid[index].value?.npcs;
    let items = this.grid[index].value?.items;

    // check if values are undefined, if not set the values of the selected grid value
    if(name == undefined){
      this.selectedRoomName = '';
    }else{
      this.selectedRoomName = name;
    }
    if(npcs == undefined){
      this.selectedRoomNPCs = [];
    }else{
      this.selectedRoomNPCs = npcs;
    }
    if(items == undefined){
      this.selectedRoomItems = [];
    }else{
      this.selectedRoomItems = items;
    }
    // set 'selectedRoomPlayers' to the players, that are currently in the selected room
  }

  highlightSelectedValue(index: number){
    for (let i = 0; i < this.grid.length; i++) {
      if(this.grid[i].value == null){
        this.grid[i].color = "#C0C0C0";             // no room in this grid -> grey
      }else{
        this.grid[i].color = "lightgreen";          // room in this grid -> lightgreen
      }
    }
    if(this.grid[index].value == null){
      this.grid[index].color = "#DC3545";           // selected grid no room -> red
      this.disableNewRoomTab = false;
      this.selectedTabIndexRoomInformation = 0;
    }else{
      this.grid[index].color = "green";             // selected grid room -> green
      this.disableNewRoomTab = true;
      this.selectedTabIndexRoomInformation = 1;
    }
  }

  sliderValue(value: number) {
    return value;
  }
}

export interface gridValue {
  index: number;
  value: null | Tile;
  color: string;
}
