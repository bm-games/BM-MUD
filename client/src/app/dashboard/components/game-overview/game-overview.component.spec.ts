import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameOverviewComponent} from './game-overview.component';
import {MatDialogModule} from "@angular/material/dialog";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {CommandService} from "../../../game/services/command.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";

describe('GameOverviewComponent', () => {
  let component: GameOverviewComponent;
  let fixture: ComponentFixture<GameOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GameOverviewComponent],
      imports: [
        MatDialogModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameOverviewComponent);
    component = fixture.componentInstance;
    component.game = {
      name: "test",
      userPermitted: "Yes",
      isMaster: true,
      description: "",
      avatarCount: 2,
      masterOnline: false,
      onlinePlayers: 2
    }
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
