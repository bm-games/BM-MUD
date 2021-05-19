import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subject, Subscription} from "rxjs";
import {CommandService, SocketConnection} from "../services/command.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatMessage} from "./chat/chat.component";
import {RoomMap, Tile} from "../../shared/model/map";
import {FeedbackService} from "../../shared/services/feedback.service";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  private subscription!: Subscription;
  private connection!: SocketConnection;

  game!: string;
  avatar!: string;
  status: "Connected" | "Connecting" | "Disconnected" = "Disconnected"

  readonly commands = new Subject<string>()
  readonly players = new Subject<string[]>();
  readonly chat = new Subject<ChatMessage>()

  map?: RoomMap
  mapColumns = 8;
  grid: Array<GridValue> = [];

  constructor(private commandService: CommandService,
              private route: ActivatedRoute,
              private router: Router,
              private feedback: FeedbackService,
              title: Title) {

    const game = route.snapshot.paramMap.get("game")
    const avatar = route.snapshot.paramMap.get("avatar")
    if (!game || !avatar) {
      router.navigateByUrl("/")
    } else {
      this.game = game
      this.avatar = avatar
      title.setTitle(game + " | " + avatar)
    }
  }

  ngOnInit(): void {
    this.status = "Connecting"
    this.feedback.showLoadingOverlay()
    this.connection = this.commandService.connectAsPlayer(this.game, this.avatar)
    this.subscription = this.connection.incoming.subscribe(msg => {
        this.feedback.stopLoadingOverlay()
        this.status = "Connected"
        console.log(msg)
        switch (msg.type) {
          case "net.bmgames.game.message.Message.Text":
            this.commands.next(msg.text)
            break;
          case "net.bmgames.game.message.Message.Map":
            this.updateGrid(msg.map)
            break;
          case "net.bmgames.game.message.Message.Chat":
            this.chat.next({msg: msg.message, senderOrRecipient: msg.sender})
            break
          case "net.bmgames.game.message.Message.Close":
            this.setDisconnected("Grund: " + msg.reason,)
            break;
        }
      },
      (error) => {
        this.feedback.stopLoadingOverlay()
        if (error.reason) {
          this.setDisconnected("Grund: " + error.reason)
        } else {
          this.status = "Disconnected"
          this.feedback.alert({
            title: "Deine Verbindung ist abgebrochen",
            message: "Bitte überprüfe deine Internetverbindung und versuche es erneut.",
            primaryAction: ["Erneut verbinden", () => this.ngOnInit()],
            secondaryAction: ["Abbrechen", () => this.router.navigateByUrl('/dashboard')]
          })
        }
        console.error(error)
      },
      () => setTimeout(() => this.setDisconnected("Die Verbindung wurde unerwartet getrennt."), 100)
    )
  }

  ngOnDestroy(): void {
    this.connection?.disconnect()
    this.subscription?.unsubscribe()
  }

  private setDisconnected(reason: string) {
    if (this.status === "Disconnected") return
    this.feedback.stopLoadingOverlay()

    this.status = "Disconnected"
    this.feedback.alert({
      title: "Deine Verbindung wurde getrennt",
      message: reason,
      secondaryAction: ["Erneut verbinden", () => this.ngOnInit()]
    })
  }

  private updateGrid(map: RoomMap) {
    this.mapColumns = map.tiles.length
    const players: string[] = []
    this.grid = []
    for (let x = 0; x < map.tiles.length; x++) {
      for (let y = 0; y < map.tiles[0].length; y++) {
        let tile = map.tiles[x][y]
        let gridIndex = this.mapColumns * y + x;

        if (tile != undefined) {
          if (tile.players) players.push(...tile.players)
          this.grid[gridIndex] = {index: gridIndex, value: tile, color: this.typeToColor(tile.type)}
        } else {
          //this.grid[gridIndex] = {index: gridIndex, value: null, color: '#C0C0C0'}
          this.grid[gridIndex] = {index: gridIndex, value: null, color: 'white'}
        }
      }
    }
    this.players.next(players)
  }

  typeToColor(type: "Visited" | "NotVisited" | "Current") {
    switch (type) {
      case "Visited":
        return "orange"
      case "NotVisited":
        return "gray"
      case "Current":
        return "red"
    }
  }

  sendChat({msg, senderOrRecipient}: ChatMessage): void {
    if (senderOrRecipient != null) {
      this.connection.send(`whisper ${senderOrRecipient} ${msg}`)
    } else {
      this.connection.send(`say ${msg}`)
    }
  }

  sendCommand(command: string): void {
    this.connection.send(command)
  }
}

export interface GridValue {
  index: number;
  value: null | Tile;
  color: string;
}
