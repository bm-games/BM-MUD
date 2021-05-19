import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, Subscription} from "rxjs";
import {CommandService, SocketConnection} from "../../services/command.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatMessage} from "../chat/chat.component";
import {RoomMap, Tile} from "../../../shared/model/map";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {GridValue} from "../game.component";
import {Title} from "@angular/platform-browser";
import {ConfigService} from "../../../configurator/services/config.service";
import {NPC} from "../../../configurator/models/NPCConfig";
import {Item} from "../../../configurator/models/Item";


@Component({
  selector: 'app-game',
  templateUrl: './master.component.html',
  styleUrls: ['./master.component.scss']
})
export class MasterComponent implements OnInit, OnDestroy {
  private subscription!: Subscription;
  private connection!: SocketConnection;

  game!: string;
  status: "Connected" | "Connecting" | "Disconnected" = "Disconnected"

  commands = new Subject<string>()
  chat = new Subject<ChatMessage>()
  onlinePlayers: Subject<string[]> = new Subject<string[]>()


  map?: RoomMap
  mapColumns = 8;
  grid: Array<GridValue> = [];
  private width: number = 0;
  private height: number = 0;

  constructor(private commandService: CommandService,
              private configService: ConfigService,
              private route: ActivatedRoute,
              private router: Router,
              private feedback: FeedbackService,
              title: Title) {

    const game = route.snapshot.paramMap.get("game")
    if (!game) {
      router.navigateByUrl("/")
    } else {
      this.game = game
      title.setTitle(game + " | Master")
    }
  }

  ngOnInit(): void {
    this.status = "Connecting"
    this.feedback.showLoadingOverlay()
    this.connection = this.commandService.connectAsMaster(this.game)
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
    this.configService.getDungeonConfig(this.game).then((config) => {
      this.allDungeonNPCs = Object.values(config.npcConfigs)
      this.allDungeonItems = Object.values(config.itemConfigs)
    })
  }


  ngOnDestroy(): void {
    this.connection?.disconnect()
    this.subscription?.unsubscribe()
  }

  sendCommand(command: string): void {
    this.connection.send(command)
  }

  sendChat({msg, senderOrRecipient}: ChatMessage): void {
    if (senderOrRecipient != null) {
      this.connection.send(`whisper ${senderOrRecipient} ${msg}`)
    } else {
      this.connection.send(`say ${msg}`)
    }
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

  updateGrid(map: RoomMap) {
    // initialize grid with rooms
    this.mapColumns = map.tiles.length
    this.grid = []
    const players = []
    this.allDungeonRooms = []
    this.width = map.tiles.length
    this.height = map.tiles[0].length

    for (let x = 0; x < this.width; x++) {
      for (let y = 0; y < this.height; y++) {
        let tile = map.tiles[x][y]
        let gridIndex = this.mapColumns * y + x;

        if (tile != null) {
          this.grid[gridIndex] = {index: gridIndex, value: tile, color: "lightgreen"}
          players.push(...(tile.players || []))
          this.allDungeonRooms.push(tile.name)
          /*tile.npcs?.forEach(n => {
            if (!this.checkContainsNPC(n, this.allDungeonNPCs)) this.allDungeonNPCs.push(n)
          })
          tile.items?.forEach(i => {
            if (!this.checkContainsItem(i, this.allDungeonItems)) this.allDungeonItems.push(i)
          })*/
          tile.players?.forEach(p => {
            if (!this.checkContainsPlayer(p, this.allDungeonPlayers)) this.allDungeonPlayers.push(p)
          })
        } else {
          this.grid[gridIndex] = {index: gridIndex, value: null, color: '#C0C0C0'}
        }
      }
    }
    this.onlinePlayers.next(players)
    if (this.grid[this.selectedGridValueIndex]) {
      this.gridRoomSelected(this.grid[this.selectedGridValueIndex])
    }
  }


  selectedGridValueIndex: number = 0;
  selectedRoomName: string = '';
  selectedRoomMessage: string = '';
  selectedRoomNPCs: string[] = []
  npcsToAdd: NPC[] = [];
  selectedRoomItems: string[] = []
  itemsToAdd: Item[] = []
  selectedRoomPlayers: string[] = []
  selectedPlayerInRoom: string | undefined;
  commandsOnPlayer: string[] = ['Spieler rauswerfen', 'Spieler teleportieren', 'Charakter LP abziehen', 'Spieler Item geben']
  playerToInviteName: string | undefined;
  selectedCommandOnPlayer!: string;
  selectedRoomToTeleportTo!: string;
  allDungeonNPCs: NPC[] = [];
  allDungeonItems: Item[] = [];
  allDungeonRooms: string[] = [];

  allDungeonPlayers: string[] = [];
  disableNewRoomTab: boolean = true;


  selectedTabIndexRoomInformation: number = 1;
  selectedItemForPlayer: string = '';
  selectedItemForNPC: string = '';
  selectedNPCs: string[] = [];

  /**
   * Removes the selected NPC from the room. The NPC is still available in the list allDungeonNPCs
   */
  removeNPCFromRoom() {
    console.log(this.selectedGridValueIndex)
    //TODO
  }

  /**
   * Adds the selected NPCs to the selected room
   */
  addNPCToRoom() {
    this.npcsToAdd.forEach(npc => {
      if (!this.checkContainsNPC(npc.name, this.selectedRoomNPCs)) {
        this.selectedRoomNPCs.push(npc.name)
        let commandString = 'spawn npc ' + encodeURIComponent(this.grid[this.selectedGridValueIndex].value?.name || "null") + ' ' + encodeURIComponent(npc.name || "null")
        this.sendCommand(commandString)
      }
    })
    this.npcsToAdd = []
  }

  /**
   * Removes the selected NPC from the room. The NPC is still available in the list allDungeonNPCs
   */
  removeItemFromRoom() {
    console.log(this.selectedGridValueIndex)
    //save this.selectedRoomItems to the room items (send command)
    //TODO
  }

  /**
   * Adds the selected NPC to the selected room
   */
  addItemToRoom() {
    this.itemsToAdd.forEach(item => {
      if (!this.checkContainsItem(item.name, this.selectedRoomItems)) {
        this.selectedRoomItems.push(item.name)
        let commandString = 'spawn item ' + encodeURIComponent(this.grid[this.selectedGridValueIndex].value?.name || "null") + ' ' + encodeURIComponent(item.name)
        console.log(commandString)
        this.sendCommand(commandString)
      }
    })
    this.itemsToAdd = []
  }

  /**
   * Sends a kick command to the server which kicks the player out of the dungeon
   */
  kickPlayer() {
    if (this.selectedPlayerInRoom != undefined) {
      let commandString = 'kick ' + encodeURIComponent(this.selectedPlayerInRoom)
      this.sendCommand(commandString)
      this.selectedPlayerInRoom = undefined
    } else {
      alert("Es wurde kein Spieler ausgewählt")
    }
  }

  /**
   * Sends a teleport command to the server which teleports the player to the selected room
   */
  teleportPlayer() {
    if (this.selectedPlayerInRoom != undefined) {
      let commandString = 'teleport ' + encodeURIComponent(this.selectedPlayerInRoom) + ' ' + encodeURIComponent(this.selectedRoomToTeleportTo || "null")
      this.sendCommand(commandString)
      this.selectedPlayerInRoom = undefined
    } else {
      alert("Es wurde kein Spieler ausgewählt")
    }
  }


  /**
   * Sends a give item command to the server
   */
  giveItem(item: string, room: string, target: string) {
    if (item && room && target) {
      let commandString = `give ${encodeURIComponent(target)} ${encodeURIComponent(room)} ${encodeURIComponent(item)}`
      this.sendCommand(commandString)
      this.selectedItemForPlayer = ''
      this.selectedItemForNPC = ''
      this.selectedPlayerInRoom = undefined
    } else {
      alert("Es wurde kein Spieler, Item oder Raum ausgewählt")
    }
  }

  giveItemToNPCs() {
    console.log(this.selectedNPCs)
    this.selectedNPCs.forEach(npc => this.giveItem(this.selectedItemForNPC || '', this.grid[this.selectedGridValueIndex].value?.name || '', npc || ''))
  }

  /**
   * Hits a player and decrements his health
   * @param damage the number healthpoints that are going to be subtracted from players health
   */
  hitPlayer(damage: number | null) {
    if (this.selectedPlayerInRoom != undefined && damage != null) {
      let commandString = 'hit ' + encodeURIComponent(this.selectedPlayerInRoom) + ' ' + encodeURIComponent(this.grid[this.selectedGridValueIndex].value?.name || "null") + ' ' + damage
      this.sendCommand(commandString)
      this.selectedPlayerInRoom = undefined
    } else {
      alert("Es wurde kein Spieler ausgewählt")
    }
  }

  /**
   * Deletes the selected room
   * This is only possible, if the selected room doesn't contains any characters
   */
  deleteRoom() {
    console.log(this.selectedGridValueIndex)
    let commandString = 'deleteroom ' + encodeURIComponent(this.grid[this.selectedGridValueIndex].value?.name || 'null')
    this.sendCommand(commandString)
  }

  /**
   * Creates a new room and adds it at the selected position to the dungeon
   * It sets the input values name, description, NPCs and Items
   */
  addNewRoom() {
    console.log(this.selectedRoomName)
    console.log(this.selectedRoomMessage)
    console.log(this.selectedRoomNPCs)
    console.log(this.selectedRoomItems)
    const n = this.encodeDirection('n');
    const e = this.encodeDirection('e');
    const s = this.encodeDirection('s');
    const w = this.encodeDirection('w');
    console.log('Norden: ' + n)
    console.log('Osten: ' + e)
    console.log('Süden: ' + s)
    console.log('Westen: ' + w)

    if (this.selectedRoomName != '' && this.selectedRoomMessage != '') {
      if (!this.checkContainsRoom(this.selectedRoomName, this.allDungeonRooms)) {
        let commandString = `createroom ${n} ${e} ${s} ${w} ${encodeURIComponent(this.selectedRoomName)} ${encodeURIComponent(this.selectedRoomMessage)}`
        this.sendCommand(commandString)
        this.selectedRoomNPCs.forEach(n => {
          commandString = 'spawn npc ' + encodeURIComponent(this.selectedRoomName) + ' ' + encodeURIComponent(n || "null")
          this.sendCommand(commandString)
        })
        this.selectedRoomItems.forEach(i => {
          commandString = 'spawn item ' + encodeURIComponent(this.selectedRoomName) + ' ' + encodeURIComponent(i || "null")
          this.sendCommand(commandString)
        })
      } else {
        alert("Es gibt bereits einen Raum mit dem Namen: " + this.selectedRoomName)
      }
    } else {
      alert("Es wurden nicht alle Daten eingegeben")
    }

    this.selectedRoomName = ''
    this.selectedRoomMessage = ''
    this.selectedRoomNPCs = []
    this.selectedRoomItems = []
  }

  encodeDirection(dir: string): string {
    const neighbour = this.searchForNeighbour(this.selectedGridValueIndex, dir);
    if (neighbour) {
      return `-${dir} ${encodeURIComponent(neighbour)}`
    }
    return neighbour
  }

  /**
   * Sends a invitePlayer command to the server to send an invitation to the player
   */
  invitePlayer() {
    console.log("Invite player: " + this.playerToInviteName)
    this.playerToInviteName = undefined
    //TODO
  }

  /**
   * iterates trough the list in the params and checks whether the list contains the npc (by name) or not
   * @param npcName the name of the npc that should be checked
   * @param list the list to check if it contains the npcName
   */
  checkContainsNPC(npcName: string, list: string[]): boolean {
    for (let i = 0; i < list.length; i++) {
      if (list[i] == npcName) return true
    }
    return false
  }

  /**
   * iterates trough the list in the params and checks whether the list contains the item (by name) or not
   * @param itemName the name of the item that should be checked
   * @param list the list to check if it contains the itemName
   */
  checkContainsItem(itemName: string, list: string[]): boolean {
    for (let i = 0; i < list.length; i++) {
      if (list[i] == itemName) return true
    }
    return false
  }

  checkContainsPlayer(playerName: string, list: string[]): boolean {
    for (let i = 0; i < list.length; i++) {
      if (list[i] == playerName) return true
    }
    return false
  }

  checkContainsRoom(roomName: string, list: string[]): boolean {
    for (let i = 0; i < list.length; i++) {
      if (list[i] == roomName) return true
    }
    return false
  }

  gridRoomSelected(gridV: gridValue) {
    this.selectedGridValueIndex = gridV.index;
    this.setInputValuesToSelected(this.selectedGridValueIndex);
    this.highlightSelectedValue(this.selectedGridValueIndex);
    console.log(this.grid[this.selectedGridValueIndex].value);
  }

  setInputValuesToSelected(index: number) {
    let name = this.grid[index].value?.name;
    let npcs = this.grid[index].value?.npcs;
    let items = this.grid[index].value?.items;
    let players = this.grid[index].value?.players;

    // check if values are undefined, if not set the values of the selected grid value
    if (name == undefined) {
      this.selectedRoomName = '';
    } else {
      this.selectedRoomName = name;
    }
    if (npcs == undefined) {
      this.selectedRoomNPCs = [];
    } else {
      this.selectedRoomNPCs = npcs;
    }
    if (items == undefined) {
      this.selectedRoomItems = [];
    } else {
      this.selectedRoomItems = items;
    }
    if (players == undefined) {
      this.selectedRoomPlayers = [];
    } else {
      this.selectedRoomPlayers = players;
    }
  }

  highlightSelectedValue(index: number) {
    for (let i = 0; i < this.grid.length; i++) {
      if (this.grid[i].value == null) {
        this.grid[i].color = "#C0C0C0";             // no room in this grid -> grey
      } else {
        this.grid[i].color = "lightgreen";          // room in this grid -> lightgreen
      }
    }
    if (this.grid[index].value == null) {
      this.grid[index].color = "#DC3545";           // selected grid no room -> red
      this.disableNewRoomTab = false;
      this.selectedTabIndexRoomInformation = 0;
    } else {
      this.grid[index].color = "green";             // selected grid room -> green
      this.disableNewRoomTab = true;
      this.selectedTabIndexRoomInformation = this.selectedTabIndexRoomInformation == 0 ? 1 : this.selectedTabIndexRoomInformation;
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
  searchForNeighbour(index: number, direction: string): string {
    let neighbourName;
    let i;

    switch (direction) {
      case "n":
        i = index - this.width;
        if (i >= 0 && this.grid[i]?.value != null) {                                       // i >= 0 -> check if i out of grid (north)
          neighbourName = this.grid[i].value?.name;
        }
        break;
      case "e":
        i = index + 1;
        if (i % this.width != 0 && this.grid[i]?.value != null) {                     // i % mapColumns != 0 -> check if i out of grid (east)
          neighbourName = this.grid[i].value?.name
        }
        break;
      case "s":
        i = index + this.width;
        if (i < this.width * this.width && this.grid[i]?.value != null) {          // i < mapColumns * mapColumns -> check if i out of grid (south)
          neighbourName = this.grid[i].value?.name
        }
        break;
      case "w":
        i = index - 1;
        if (index % this.width != 0 && this.grid[i]?.value ! != null) {               // index % mapColumns != 0 -> check if i out of grid (west)
          neighbourName = this.grid[i].value?.name
        }
        break;
    }
    if (neighbourName != null) {
      return neighbourName
    } else {
      return '';
    }
  }
}

export interface gridValue {
  index: number;
  value: null | Tile;
  color: string;
}
