import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CommandComponent} from './command.component';

describe('CommandComponent', () => {
  let component: CommandComponent;
  let fixture: ComponentFixture<CommandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommandComponent]
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

  it('should add new custom CommandConfig', () => {
    fixture = TestBed.createComponent(CommandComponent);
    component = fixture.componentInstance;

    component.isCustomCommand = true;
    component.commandSyntax = 'hit';
    component.customCommands = {};
    component.customCommands['command'] = 'action';

    component.addCommand();

    let length = Object.keys(component.customCommands).length;
    expect(length).toBe(2);
    expect(component.customCommands['command']).toEqual('action');
  });
});
