import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceComponent } from './race.component';
import {NPCComponent} from "../npc/npc.component";
import {FriendlyNPCConfig} from "../../../models/FriendlyNPCConfig";
import {HostileNPCConfig} from "../../../models/HostileNPCConfig";
import {RaceConfig} from "../../../models/RaceConfig";
import {NPCType} from "../../../models/NPCType";

describe('RaceComponent', () => {
  let component: RaceComponent;
  let fixture: ComponentFixture<RaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RaceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return 1 as next free RaceId. Already assigned IDs: 0,2', () => {
    fixture = TestBed.createComponent(RaceComponent);
    component = fixture.componentInstance;

    component.configuredRaces = [
      { id: 0, name: 'test', health: 1, description: '', damageMultiplier: 2},
      { id: 2, name: 'test', health: 1, description: '', damageMultiplier: 2},
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(1);
  });

  it('should add new Race', () =>{
    fixture = TestBed.createComponent(RaceComponent);
    component = fixture.componentInstance;

    component.name = 'race';
    component.damage = 1;
    component.health = 2;
    component.description = 'desc';

    component.addRace();

    let length = component.configuredRaces.length;

    expect(length).toBe(1);
    expect(component.configuredRaces[length-1].id).toEqual(0);
    expect(component.configuredRaces[length-1].name).toEqual('race');
    expect(component.configuredRaces[length-1].description).toEqual('desc');
    expect(component.configuredRaces[length-1].damageMultiplier).toBe(1);
    expect(component.configuredRaces[length-1].health).toBe(2);
  });
});
