import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomComponent } from './room.component';

describe('RoomComponent', () => {
  let component: RoomComponent;
  let fixture: ComponentFixture<RoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoomComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should find north neighbour and return 10 as neighbours id', () =>{
    fixture = TestBed.createComponent(RoomComponent);
    component = fixture.componentInstance;

    component.mapColumns = 8;
    component.grid[10] = {
      index: 10,
      value: {
        id: 10,
        name: 'name',
        message: 'msg',
        npcs: [],
        items: [],
        north: -1,
        east: -1,
        south: -1,
        west: -1
      },
      color: 'lightgreen'
    }

    let neighbourId = component.searchForNeighbour(18, 'n');

    expect(neighbourId).toBe(10);
  });

  it('should return -1 because there is no west neighbour of a room on left grid side', () =>{
    fixture = TestBed.createComponent(RoomComponent);
    component = fixture.componentInstance;

    component.mapColumns = 8;
    component.grid[15] = {
      index: 10,
      value: {
        id: 10,
        name: 'name',
        message: 'msg',
        npcs: [],
        items: [],
        north: -1,
        east: -1,
        south: -1,
        west: -1
      },
      color: 'lightgreen'
    }

    let neighbourId = component.searchForNeighbour(16, 'w');

    expect(neighbourId).toBe(-1);
  });

  it('should return -1 because there is no east neighbour of a room on right grid side', () =>{
    fixture = TestBed.createComponent(RoomComponent);
    component = fixture.componentInstance;

    component.mapColumns = 8;
    component.grid[8] = {
      index: 10,
      value: {
        id: 10,
        name: 'name',
        message: 'msg',
        npcs: [],
        items: [],
        north: -1,
        east: -1,
        south: -1,
        west: -1
      },
      color: 'lightgreen'
    }

    let neighbourId = component.searchForNeighbour(7, 'e');

    expect(neighbourId).toBe(-1);
  });
});
