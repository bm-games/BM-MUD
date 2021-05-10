import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NPCComponent} from './npc.component';
import {NPCType} from "../../../models/NPCType";
import {FriendlyNPCConfig} from "../../../models/FriendlyNPCConfig";

describe('NPCComponent', () => {
  let component: NPCComponent;
  let fixture: ComponentFixture<NPCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NPCComponent]
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

  it('should add new friendly NPC', () => {
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    component.name = 'NPC 1';
    component.isHostile = false;
    component.messageOnTalk = 'msg';
    component.selectedCommandOnInteraction = 'command';

    component.addNPC();

    let length = component.configuredNPCs.length;

    expect(length).toBe(1);
    let npc = component.configuredNPCs[length - 1];
    expect(npc.name).toEqual('NPC 1');
    expect(npc.type).toEqual("net.bmgames.state.model.NPC.Friendly");
  });

  it('should add new hostile NPC', () => {
    fixture = TestBed.createComponent(NPCComponent);
    component = fixture.componentInstance;

    component.name = 'NPC 1';
    component.isHostile = true;
    component.damage = 1;
    component.health = 1;

    component.addNPC();

    let length = component.configuredNPCs.length;

    expect(length).toBe(1);
    let npc = component.configuredNPCs[length - 1];
    expect(npc.name).toEqual('NPC 1');
    expect(npc.type).toEqual("net.bmgames.state.model.NPC.Hostile");
  });

  it('should show alert because input value is missing', () => {
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
    expect(window.alert).toHaveBeenCalledWith('Ungültiger Name. Entweder es ist kein Name eingetragen oder es exisitert bereits ein NPC mit diesem Namen.');
  });
});
