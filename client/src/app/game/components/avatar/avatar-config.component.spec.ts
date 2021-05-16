import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AvatarConfigComponent} from './avatar-config.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {RaceConfig} from "../../../configurator/models/RaceConfig";
import {ClassConfig} from "../../../configurator/models/ClassConfig";
import {PlayerDetail} from "../../model/game-detail";
import {MatDialogHarness} from "@angular/material/dialog/testing";

xdescribe('AvatarConfigComponent', () => {
  let component: AvatarConfigComponent;
  let fixture: ComponentFixture<AvatarConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AvatarConfigComponent],
      imports: [
        MatDialogModule,
        MatDialogRef
      ],
      providers: [
        {
          provide: MAT_DIALOG_DATA, useValue: {
            players: [],
            races: [],
            classes: [],
            isMaster: true,
            name: ""
          }
        }
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AvatarConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
