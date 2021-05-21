import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterComponent } from './master.component';
import {RouterTestingModule} from "@angular/router/testing";
import {MatDialogModule} from "@angular/material/dialog";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {CommandService} from "../../services/command.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('MasterComponent', () => {
  let component: MasterComponent;
  let fixture: ComponentFixture<MasterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterComponent ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
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
    fixture = TestBed.createComponent(MasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
