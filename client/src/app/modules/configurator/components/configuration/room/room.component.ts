import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  allNPCs: string[] = ['GeOrk', 'Drache', 'Händler'];
  allItems: string[] = ['Lampe', 'Stein', 'Apfel'];

  selectedGridValueIndex: number = 0;
  selectedRoomName: string = '';
  selectedRoomMessage: string = '';
  selectedRoomNPCs: string[] = [];
  selectedRoomItems: string[] = [];

  //Grid
  // -> neighbours of a gridValue are: index -> [-1],[+1],[-mapColumns},[+mapColumns]
  mapColumns = 8;
  grid: Array<gridValue> = [];

  constructor() { }

  ngOnInit(): void {
    // initialize grid with rooms
    for(let i = 0; i<this.mapColumns*this.mapColumns; i++){
      this.grid[i] = {
        index: i,
        value: null,
        color: "#C0C0C0"
      }
    }
    this.highlightSelectedValue(0);
  }

  gridRoomSelected(gridV: gridValue){
    this.selectedGridValueIndex = gridV.index;
    this.setInputValuesToSelected(this.selectedGridValueIndex);
    this.highlightSelectedValue(this.selectedGridValueIndex);
    console.log(this.grid[this.selectedGridValueIndex].value);
  }

  addRoom() {
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
  }

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

  private updateNeighbour(target: number) {
    let index = this.grid.find(r => r.value?.id == target)?.index;        // index of room in grid
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
            id: index,                              // replace this with real id
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
      }
    }
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
    }else{
      this.grid[index].color = "green";             // selected grid room -> green
    }
  }
}

export interface gridValue{
  index: number;
  value: null | room;
  color: string;
}

// replace this with a real model in "configurator/models"
export interface room{
  id: number;
  name: string;
  message: string;

  // replace these two with arrays of models
  npcs: string[];
  items: string[];

  // contains id from NeighbourRoom -> -1 means no Neighbour
  north: number;
  east: number;
  south: number;
  west: number;
}
