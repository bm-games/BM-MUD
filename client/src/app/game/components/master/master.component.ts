import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {RoomConfig} from "../../../configurator/models/RoomConfig";
import {NPC} from "../../../configurator/models/NPCConfig";
import {Item} from "../../../configurator/models/Item";
import {CommandService, SocketConnection} from "../../services/command.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatMessage} from "../chat/chat.component";


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
  selectedRoomNPCs: NPC[] = [];
  selectedRoomItems: Item[] = [];
  selectedRoomValues: string[] = ['Stock', 'GeOrk', 'Stock', 'GeOrk', 'Stock', 'GeOrk'];
  selectedRoomPlayers: string[] = [];
  onlinePlayers: string[] = ['Hans', 'Martin', 'Peter'];
  commandsOnPlayer: string[] = ['Spieler rauswerfen', 'Spieler teleportieren', 'Charakter LP abziehen']
  selectedCommandOnPlayer!: string;
  allDungeonNPCs: string[] = [];
  allDungeonItems: string[] = [];
  allDungeonRooms: string[] = [];

  disableNewRoomTab: boolean = true;
  selectedTabIndexRoomInformation: number = 1;

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
    for(let i = 0; i<this.mapColumns*this.mapColumns; i++){
      this.grid[i] = {
        index: i,
        value: null,
        color: "#C0C0C0"
      }
    }
  }

  gridRoomSelected(gridV: gridValue){
    this.selectedGridValueIndex = gridV.index;
    this.setInputValuesToSelected(this.selectedGridValueIndex);
    this.highlightSelectedValue(this.selectedGridValueIndex);
    console.log(this.grid[this.selectedGridValueIndex].value);
  }

  setInputValuesToSelected(index: number){
    let name = this.grid[index].value?.name;
    let msg = this.grid[index].value?.message;
    let npcs = this.grid[index].value?.npcs;
    let items = this.grid[index].value?.items;

    // check if values are undefined, if not set the values of the selected grid value
    if(name == undefined){
      this.selectedRoomName = '';
    }else{
      this.selectedRoomName = name;
    }
    if(msg == undefined){
      this.selectedRoomMessage = '';
    }else{
      this.selectedRoomMessage = msg;
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
  value: null | RoomConfig;
  color: string;
}
