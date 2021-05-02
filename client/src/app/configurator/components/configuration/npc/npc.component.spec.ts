import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NPCComponent} from './npc.component';
import {FriendlyNPCConfig} from "../../../models/FriendlyNPCConfig";
import {HostileNPCConfig} from "../../../models/HostileNPCConfig";
import {NPCType} from "../../../models/NPCType";

describe('NPCComponent', () => {
  let component: NPCComponent;
  let fixture: ComponentFixture<NPCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NPCComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return 1 as next free NPCId. Already assigned IDs: 0,2', () => {
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    component.configuredNPCs = [
      {id: 0, type: NPCType.Friendly, name: '', items: [], loottable: [], commandOnInteraction: '', messageOnTalk: ''},
      {id: 2, type: NPCType.Hostile, name: '', items: [], loottable: [], commandOnInteraction: '', messageOnTalk: ''}
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(1);
  });

  it('should add new friendly NPC', () =>{
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    component.name = 'NPC 1';
    component.isHostile = false;
    component.messageOnTalk = 'msg';
    component.selectedCommandOnInteraction = 'command';

    component.addNPC();

    let length = component.configuredNPCs.length;

    expect(length).toBe(1);
    expect(component.configuredNPCs[length-1].id).toEqual(0);
    expect(component.configuredNPCs[length-1].name).toEqual('NPC 1');
    expect(component.configuredNPCs[length-1].type).toEqual(NPCType.Friendly);
  });

  it('should add new hostile NPC', () =>{
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    component.name = 'NPC 1';
    component.isHostile = true;
    component.damage = 1;
    component.health = 1;

    component.addNPC();

    let length = component.configuredNPCs.length;

    expect(length).toBe(1);
    expect(component.configuredNPCs[length-1].id).toEqual(0);
    expect(component.configuredNPCs[length-1].name).toEqual('NPC 1');
    expect(component.configuredNPCs[length-1].type).toEqual(NPCType.Hostile);
  });

  it('should show alert because input value is missing', () =>{
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    spyOn(window, 'alert');

    component.isHostile = false;
    component.name = undefined;
    component.messageOnTalk = 'msg';
    component.selectedCommandOnInteraction = 'command';

    component.addNPC();

    let length = component.configuredNPCs.length;

    expect(length).toBe(0);
    expect(window.alert).toHaveBeenCalledWith('Es wurden nicht alle Daten eingegeben');
  });
});
