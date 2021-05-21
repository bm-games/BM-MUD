import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AvatarConfigComponent} from './avatar-config.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {ReactiveFormsModule} from "@angular/forms";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";

describe('AvatarConfigComponent', () => {
  let component: AvatarConfigComponent;
  let fixture: ComponentFixture<AvatarConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AvatarConfigComponent],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG},
        {
          provide: MAT_DIALOG_DATA, useValue: {
            players: [],
            races: [],
            classes: [],
            isMaster: true,
            name: ""
          }
        },
        {provide: MatDialogRef, useValue: {}},
      ],
      imports: [
        MatDialogModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        MatInputModule,
        MatSelectModule,
        NoopAnimationsModule
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AvatarConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
