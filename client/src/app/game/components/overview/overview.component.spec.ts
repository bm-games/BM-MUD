import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewComponent } from './overview.component';
import {RouterTestingModule} from "@angular/router/testing";
import {MatDialogModule} from "@angular/material/dialog";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

xdescribe('OverviewComponent', () => {
  let component: OverviewComponent;
  let fixture: ComponentFixture<OverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverviewComponent ],
      imports: [
        RouterTestingModule,
        MatDialogModule,
        HttpClientTestingModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
