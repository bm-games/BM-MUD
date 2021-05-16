import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameComponent} from './game.component';
import {CommandService} from "../services/command.service";
import {CONFIG} from "../../client-config";
import {LOCAL_CONFIG} from "../../app.module";
import {RouterTestingModule} from "@angular/router/testing";
import {MatDialogModule} from "@angular/material/dialog";

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GameComponent],
      imports: [
        RouterTestingModule,
        MatDialogModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG},
        CommandService,
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
