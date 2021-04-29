import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommandComponent } from './command.component';
import {ClassComponent} from "../class/class.component";
import {ClassConfig} from "../../../models/ClassConfig";
import {CommandConfig} from "../../../models/CommandConfig";

describe('CommandComponent', () => {
  let component: CommandComponent;
  let fixture: ComponentFixture<CommandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommandComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return 5 as next free CommandId. Already assigned IDs: 0,1,2,3,4 ', () => {
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;

    component.customCommands = [
      new CommandConfig(0, '', ['']),
      new CommandConfig(1, '', ['']),
      new CommandConfig(2, '', ['']),
      new CommandConfig(3, '', ['']),
      new CommandConfig(4, '', [''])
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(5);
  });

  it('should return 2 as next free CommandId. Already assigned IDs: 0,1,3,4', () => {
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;

    component.customCommands = [
      new CommandConfig(0, '', ['']),
      new CommandConfig(1, '', ['']),
      new CommandConfig(3, '', ['']),
      new CommandConfig(4, '', [''])
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(2);
  });

  it('should add new custom CommandConfig', () =>{
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;

    component.isCustomCommand = true;
    component.commandSyntax = 'hit $player'
    component.selectedActions = ['Action'];

    component.addCommand();

    let length = component.customCommands.length;
    expect(length).toBe(1);
    expect(component.customCommands[length-1].id).toBe(0);
    expect(component.customCommands[length-1].command).toEqual('hit $player');
    expect(component.customCommands[length-1].actions).toEqual(['Action']);
  });
});
