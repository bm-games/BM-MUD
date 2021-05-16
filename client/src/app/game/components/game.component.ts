import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {RoomConfig} from "../../configurator/models/RoomConfig";
import {NPC} from "../../configurator/models/NPCConfig";
import {Item} from "../../configurator/models/Item";
import {CommandService, SocketConnection} from "../services/command.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatMessage} from "./chat/chat.component";
import {RoomMap, Tile} from "../../shared/model/map";


@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  private connection!: SocketConnection;
  commands = new Subject<string>()
  chat = new Subject<ChatMessage>()
  game!: string;
  avatar!: string;

  map: RoomMap = {tiles: [[null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: [], npcs: [{ name: 'npc1', commandOnInteraction: '', messageOnTalk: 'hallo', items: [], type: "net.bmgames.state.model.NPC.Friendly"}, { name: 'npc2', health: 3, damage: 3, items: [], type: "net.bmgames.state.model.NPC.Hostile"}], name: 'room 1' }],
      [null, null, { north: false, east: false, south: false, west: false, color: 'lightgreen', items: [{name: 'item 1', effect: 'heilen', type: 'net.bmgames.state.model.Consumable'}], npcs: [], name: 'Keller' }],
      [null, null, null]] }       // map.tiles[i][j] -> gridValue[mapColumns * i + j]

  //Grid
  // -> neighbours of a gridValue are: index -> [-1],[+1],[-mapColumns},[+mapColumns]
  mapColumns = 8;
  grid: Array<gridValue> = [];

  constructor(private commandService: CommandService,
              route: ActivatedRoute,
              router: Router) {
    const game = route.snapshot.paramMap.get("game")
    const avatar = route.snapshot.paramMap.get("avatar")
    if (!game || !avatar) {
      router.navigateByUrl("/")
    } else {
      this.game = game
      this.avatar = avatar
      this.connection = this.commandService.connect(this.game, this.avatar)
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
    // initialize grid
    this.mapColumns = this.map.tiles[0].length
    /*for (let i = 0; i < this.mapColumns * this.mapColumns; i++) {
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
        if(tile != undefined){
          this.grid[gridIndex] = {index: gridIndex, value: tile, color: tile.color}
        }else{
          //this.grid[gridIndex] = {index: gridIndex, value: null, color: '#C0C0C0'}
          this.grid[gridIndex] = {index: gridIndex, value: null, color: 'white'}
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
    this.connection.send(command)
  }
}

export interface gridValue {
  index: number;
  value: null | Tile;
  color: string;
}
