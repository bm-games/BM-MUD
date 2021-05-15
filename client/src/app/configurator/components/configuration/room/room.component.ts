import {Component, OnInit} from '@angular/core';
import {ConfigurationComponent} from "../configuration.component";
import {NPC} from "../../../models/NPCConfig";
import {Item} from "../../../models/Item";
import {RoomConfig} from "../../../models/RoomConfig";


@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  allNPCs: NPC[] = [];
  allItems: Item[] = [];

  selectedGridValueIndex: number = 0;
  selectedRoomName: string = '';
  selectedRoomMessage: string = '';
  //selectedRoomNPCs: StringMap<NPC> = {};
  selectedRoomNPCs: NPC[] = [];
  selectedRoomItems: Item[] = [];
  selectedStartRoomName: string | undefined;

  configuredRooms: RoomConfig[] = [];

  //Grid
  // -> neighbours of a gridValue are: index -> [-1],[+1],[-mapColumns},[+mapColumns]
  mapColumns = 8;
  grid: Array<gridValue> = [];

  constructor() { }

  ngOnInit(): void {
    this.configuredRooms = ConfigurationComponent.allRooms;
    this.selectedStartRoomName = ConfigurationComponent.startRoom;
    // initialize grid with rooms
    for(let i = 0; i<this.mapColumns*this.mapColumns; i++){
      let room = this.getRoomConfigById(i);
      if(room != undefined){
        this.grid[i] = {
          index: i,
          value: room,
          color: "lightgreen"
        }
      }else{
        this.grid[i] = {
          index: i,
          value: null,
          color: "#C0C0C0"
        }
      }
    }
    this.highlightSelectedValue(0);
    this.allNPCs = ConfigurationComponent.allNPCs;
    this.allItems = ConfigurationComponent.allItems;
  }

  /**
   * Finds a RoomConfig in the 'configuredRooms' list by it's ID
   * @param id Id of the room to be found
   * @returns RoomConfig if a room was found
   */
  getRoomConfigById(id: number): RoomConfig | undefined {
    let room;
    for (let i = 0; i < this.configuredRooms.length; i++) {
      if(this.configuredRooms[i].id == id) room = this.configuredRooms[i];
    }
    return room;
  }

  /**
   * Sets 'selectedGridValueIndex' to the selected grid value and sets all UI-inputs to the selected grid value data
   * @param gridV gridValue, which was selected in the UI
   */
  gridRoomSelected(gridV: gridValue){
    this.selectedGridValueIndex = gridV.index;
    this.setInputValuesToSelected(this.selectedGridValueIndex);
    this.highlightSelectedValue(this.selectedGridValueIndex);
    console.log(this.grid[this.selectedGridValueIndex].value);
  }

  /**
   * Finds all neighbours of the new room and generates a new RoomConfig.
   * All existing neighbours getting updated.
   */
  addRoom() {
    if(!this.checkContainsName()){
      let northNeighbour = this.searchForNeighbour(this.selectedGridValueIndex, 'n');
      let eastNeighbour = this.searchForNeighbour(this.selectedGridValueIndex, 'e');
      let southNeighbour = this.searchForNeighbour(this.selectedGridValueIndex, 's');
      let westNeighbour = this.searchForNeighbour(this.selectedGridValueIndex, 'w');
      this.grid[this.selectedGridValueIndex] = {
        index: this.selectedGridValueIndex,
        value: {
          id: this.selectedGridValueIndex,           // replace this with real id
          name: this.selectedRoomName,
          message: this.selectedRoomMessage,
          npcs: this.selectedRoomNPCs,
          items: this.selectedRoomItems,
          north: northNeighbour,
          east: eastNeighbour,
          south: southNeighbour,
          west: westNeighbour
        },
        color: "lightgreen"
      }

      if(northNeighbour > -1) this.updateNeighbour(northNeighbour);
      if(eastNeighbour > -1) this.updateNeighbour(eastNeighbour);
      if(southNeighbour > -1) this.updateNeighbour(southNeighbour);
      if(westNeighbour > -1) this.updateNeighbour(westNeighbour);

      this.configuredRooms = [];
      this.grid.forEach(gridValue => {
        if(gridValue.value != null) this.configuredRooms.push(gridValue.value);
      });
      ConfigurationComponent.allRooms = this.configuredRooms;
    }else{
      window.alert("Es existiert bereits ein Raum mit dem Namen: " + this.selectedRoomName);
    }
    console.log(this.configuredRooms);
  }

  checkContainsName(): boolean{
    for (let i = 0; i < this.configuredRooms.length; i++) {
      if(this.configuredRooms[i].name == this.selectedRoomName){
        return true;
      }
    }
    return false;
  }

  /**
   * Searches for a neighbour of a room in the target direction.
   * @param index index of the target room, which neighbour should be found.
   * @param direction direction, in which the neighbour should be found. Possible values: 'n','e','s','w'.
   * @returns neighbourId - the id of the neighbour. Returns -1 if no neighbour was found.
   */
  searchForNeighbour(index: number, direction: string) : number{
    let neighbourId;
    let i;
    switch(direction){
      case "n":
        i = index - this.mapColumns;
        if(i >= 0 && this.grid[i].value != null){                                       // i >= 0 -> check if i out of grid (north)
          neighbourId = this.grid[i].value?.id;
        }
        break;
      case "e":
        i = index + 1;
        if(i % this.mapColumns != 0 && this.grid[i].value != null){                     // i % mapColumns != 0 -> check if i out of grid (east)
          neighbourId = this.grid[i].value?.id
        }
        break;
      case "s":
        i = index + this.mapColumns;
        if(i < this.mapColumns*this.mapColumns && this.grid[i].value != null){          // i < mapColumns * mapColumns -> check if i out of grid (south)
          neighbourId = this.grid[i].value?.id;
        }
        break;
      case "w":
        i = index - 1;
        if(index % this.mapColumns != 0 && this.grid[i].value ! != null){               // index % mapColumns != 0 -> check if i out of grid (west)
          neighbourId = this.grid[i].value?.id;
        }
        break;
    }
    if(neighbourId != null){
      return neighbourId
    }else{
      return -1;
    }
  }

  /**
   * Updates all neighbours of a room.
   * @param target id of the room, that should be updated
   */
  private updateNeighbour(target: number) {
    console.log("target: " + target);
    let index = this.grid.find(r => r.value?.id == target)?.index;        // index of room in grid
    console.log('index: ' + index);
    if(index != null){
      let name = this.grid[index].value?.name;
      let msg = this.grid[index].value?.message;
      let npcs = this.grid[index].value?.npcs;
      let items = this.grid[index].value?.items;
      let northNeighbour = this.searchForNeighbour(index, 'n');
      let eastNeighbour = this.searchForNeighbour(index, 'e');
      let southNeighbour = this.searchForNeighbour(index, 's');
      let westNeighbour = this.searchForNeighbour(index, 'w');
      if(name != undefined && msg != undefined && npcs != undefined && items != undefined){
        this.grid[index] = {
          index: index,
          value: {
            id: index,                              // id = index in grid
            name: name,
            message: msg,
            npcs: npcs,
            items: items,
            north: northNeighbour,
            east: eastNeighbour,
            south: southNeighbour,
            west: westNeighbour
          },
          color: "lightgreen"
        }
      }
    }
  }

  /**
   * Sets the UI components to the data of the selected grid value
   * @param index index of the selected grid value
   */
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
  }

  /**
   * Changes the color of the selected grid value
   * @param index index of the selected grid value
   */
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
    }else{
      this.grid[index].color = "green";             // selected grid room -> green
    }
  }

  /**
   * Sets the startroom of the Dungeon
   * @param id id of the startroom
   */
  startroomChanged(name: string){
    if(name != null){
      this.selectedStartRoomName = name;
      ConfigurationComponent.startRoom = name;
    }
  }
}

export interface gridValue{
  index: number;
  value: null | RoomConfig;
  color: string;
}
