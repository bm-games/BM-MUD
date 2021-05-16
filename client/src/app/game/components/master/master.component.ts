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
  selectedRoomNPCs: string[] = []
  npcsToAdd: string[] = [];
  selectedRoomItems: string[] = []
  itemsToAdd: string[] = []
  selectedRoomPlayers: string[] = []
  selectedPlayerInRoom: string | undefined;
  onlinePlayers: string[] = []
  commandsOnPlayer: string[] = ['Spieler rauswerfen', 'Spieler teleportieren', 'Charakter LP abziehen']
  playerToInviteName: string | undefined;
  selectedCommandOnPlayer!: string;
  selectedRoomToTeleportTo!: string;
  allDungeonNPCs: string[] = [];
  allDungeonItems: string[] = [];
  allDungeonRooms: string[] = [];
  allDungeonPlayers: string[] = [];

  disableNewRoomTab: boolean = true;
  selectedTabIndexRoomInformation: number = 1;

  map: RoomMap = {tiles: [[null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: [], npcs: ['npc1', 'npc2'], name: 'room 1', players: ['Hans']}],
                          [null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: ['item 1'], npcs: [], name: 'Keller', players: [] }],
                          [{ north: false, east: false, south: false, west: false, color: 'lightgreen', items: ['iiiiteeem'], npcs: [], name: 'Kraftwerk', players: [] }, null, null]] }       // map.tiles[i][j] -> gridValue[mapColumns * i + j]

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
            //set "this.onlinePlayers"
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

    for (let i = 0; i < this.map.tiles[0].length; i++) {
      for (let j = 0; j < this.map.tiles[1].length; j++) {
        let tile = this.map.tiles[i][j]
        let gridIndex = this.mapColumns * i + j;
        console.log(gridIndex)
        console.log(tile)
        if(tile != null){
          this.grid[gridIndex] = {index: gridIndex, value: tile, color: tile.color}
          this.allDungeonRooms.push(tile.name)
          tile.npcs.forEach(n => {
            if(!this.checkContainsNPC(n, this.allDungeonNPCs)) this.allDungeonNPCs.push(n)
          })
          tile.items.forEach(i => {
            if(!this.checkContainsItem(i, this.allDungeonItems)) this.allDungeonItems.push(i)
          })
          tile.players.forEach(p => {
            if(!this.checkContainsPlayer(p, this.allDungeonPlayers)) this.allDungeonPlayers.push(p)
          })
        }else{
          this.grid[gridIndex] = {index: gridIndex, value: null, color: '#C0C0C0'}
        }
      }
    }
  }

  sendChat({msg, senderOrRecipient}: ChatMessage): void {
    if (senderOrRecipient) {
      this.connection.send(`whisper ${senderOrRecipient} ${msg}`)
    } else {
      this.connection.send(`say ${msg}`)
    }
  }

  sendCommand(command: string): void {
    console.log(command)
    this.connection.send(command)
  }

  leaveGame(){
    //TODO
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
    this.npcsToAdd.forEach(n => {
      if(!this.checkContainsNPC(n, this.selectedRoomNPCs)){
        this.selectedRoomNPCs.push(n)
        let commandString = 'spawn npc ' + n + ' ' + this.grid[this.selectedGridValueIndex].value?.name + ' 1'
        this.sendCommand(commandString)
      }
    })
    this.npcsToAdd = []
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
    this.itemsToAdd.forEach(n => {
      if(!this.checkContainsItem(n, this.selectedRoomItems)){
        this.selectedRoomItems.push(n)
        let commandString = 'spawn item ' + n + ' ' + this.grid[this.selectedGridValueIndex].value?.name + ' 1'
        this.sendCommand(commandString)
      }
    })
    this.itemsToAdd = []
  }

  /**
   * Sends a kick command to the server which kicks the player out of the dungeon
   */
  kickPlayer(){
    if(this.selectedPlayerInRoom != undefined){
      let commandString = 'kick ' + this.selectedPlayerInRoom
      this.sendCommand(commandString)
    }else{
      alert("Es wurde kein Spieler ausgewählt")
    }
  }

  /**
   * Sends a teleport command to the server which teleports the player to the selected room
   */
  teleportPlayer(){
    if(this.selectedPlayerInRoom != undefined){
      let commandString = 'teleport ' + this.selectedPlayerInRoom + ' ' + this.selectedRoomToTeleportTo
      this.sendCommand(commandString)
    }else{
      alert("Es wurde kein Spieler ausgewählt")
    }
  }

  /**
   * Hits a player and decrements his health
   * @param damage the number healthpoints that are going to be subtracted from players health
   */
  hitPlayer(damage: number | null){
    if(this.selectedPlayerInRoom != undefined && damage != null){
      let commandString = 'hit ' + this.selectedPlayerInRoom + ' ' + damage
      this.sendCommand(commandString)
    }else{
      alert("Es wurde kein Spieler ausgewählt")
    }
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
    console.log('Norden: ' + this.searchForNeighbour(this.selectedGridValueIndex, 'n'))
    console.log('Osten: ' + this.searchForNeighbour(this.selectedGridValueIndex, 'e'))
    console.log('Süden: ' + this.searchForNeighbour(this.selectedGridValueIndex, 's'))
    console.log('Westen: ' + this.searchForNeighbour(this.selectedGridValueIndex, 'w'))

    if(this.selectedRoomName != '' && this.selectedRoomMessage != ''){
      if(!this.checkContainsRoom(this.selectedRoomName, this.allDungeonRooms)){
        let commandString = 'createroom ' + this.selectedRoomName + ' ' + this.selectedRoomMessage
        this.sendCommand(commandString)
        this.selectedRoomNPCs.forEach(n => {
          commandString = 'spawn npc ' + n + ' ' + this.selectedRoomName + ' 1'
          this.sendCommand(commandString)
        })
        this.selectedRoomItems.forEach(i => {
          commandString = 'spawn item ' + i + ' ' + this.selectedRoomName + ' 1'
          this.sendCommand(commandString)
        })
      }else{
        alert("Es gibt bereits einen Raum mit dem Namen: " +  this.selectedRoomName)
      }
    }else{
      alert("Es wurden nicht alle Daten eingegeben")
    }

    this.selectedRoomName = ''
    this.selectedRoomMessage = ''
    this.selectedRoomNPCs = []
    this.selectedRoomItems = []
  }

  /**
   * Sends a invitePlayer command to the server to send an invitation to the player
   */
  invitePlayer(){
    console.log("Invite player: " + this.playerToInviteName)
    this.playerToInviteName = undefined
    //TODO
  }

  /**
   * iterates trough the list in the params and checks whether the list contains the npc (by name) or not
   * @param npcName the name of the npc that should be checked
   * @param list the list to check if it contains the npcName
   */
  checkContainsNPC(npcName: string, list: string[]) : boolean{
    for (let i = 0; i < list.length; i++) {
      if(list[i] == npcName) return true
    }
    return false
  }
  /**
   * iterates trough the list in the params and checks whether the list contains the item (by name) or not
   * @param itemName the name of the item that should be checked
   * @param list the list to check if it contains the itemName
   */
  checkContainsItem(itemName: string, list: string[]) : boolean{
    for (let i = 0; i < list.length; i++) {
      if(list[i] == itemName) return true
    }
    return false
  }

  checkContainsPlayer(playerName: string, list: string[]): boolean{
    for (let i = 0; i < list.length; i++) {
      if(list[i] == playerName) return true
    }
    return false
  }

  checkContainsRoom(roomName: string, list: string[]): boolean{
    for (let i = 0; i < list.length; i++) {
      if(list[i] == roomName) return true
    }
    return false
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
    let players = this.grid[index].value?.players;

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
    if(players == undefined){
      this.selectedRoomPlayers = [];
    }else{
      this.selectedRoomPlayers = players;
    }
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

  /**
   * Searches for a neighbour of a room in the target direction.
   * @param index index of the target room, which neighbour should be found.
   * @param direction direction, in which the neighbour should be found. Possible values: 'n','e','s','w'.
   * @returns neighbourName - the name of the neighbour. Returns '' if no neighbour was found.
   */
  searchForNeighbour(index: number, direction: string) : string{
    let neighbourName;
    let i;
    switch(direction){
      case "n":
        i = index - this.mapColumns;
        if(i >= 0 && this.grid[i].value != null){                                       // i >= 0 -> check if i out of grid (north)
          neighbourName = this.grid[i].value?.name;
        }
        break;
      case "e":
        i = index + 1;
        if(i % this.mapColumns != 0 && this.grid[i].value != null){                     // i % mapColumns != 0 -> check if i out of grid (east)
          neighbourName = this.grid[i].value?.name
        }
        break;
      case "s":
        i = index + this.mapColumns;
        console.log(i)
        console.log(this.mapColumns)
        if(i < this.mapColumns*this.mapColumns && this.grid[i].value != null){          // i < mapColumns * mapColumns -> check if i out of grid (south)
          neighbourName = this.grid[i].value?.name
        }
        break;
      case "w":
        i = index - 1;
        if(index % this.mapColumns != 0 && this.grid[i].value ! != null){               // index % mapColumns != 0 -> check if i out of grid (west)
          neighbourName = this.grid[i].value?.name
        }
        break;
    }
    if(neighbourName != null){
      return neighbourName
    }else{
      return '';
    }
  }
}

export interface gridValue {
  index: number;
  value: null | Tile;
  color: string;
}
