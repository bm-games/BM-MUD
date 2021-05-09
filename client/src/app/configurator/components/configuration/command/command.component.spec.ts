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

  it('should add new custom CommandConfig', () =>{
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;

    component.isCustomCommand = true;
    component.commandSyntax = 'hit';
    component.customCommands = new Map<string, string>();
    component.customCommands.set('command', 'action');

    component.addCommand();

    let length = component.customCommands.size;
    expect(length).toBe(2);
    expect(component.customCommands.get('command')).toEqual('action');
  });
});
