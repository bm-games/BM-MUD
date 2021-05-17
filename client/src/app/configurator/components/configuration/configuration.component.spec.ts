import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ConfigurationComponent} from './configuration.component';
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ConfiguratorModule} from "../../configurator.module";
import {MatDialogModule} from "@angular/material/dialog";

describe('ConfigurationComponent', () => {
  let component: ConfigurationComponent;
  let fixture: ComponentFixture<ConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // declarations: [ConfigurationComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ConfiguratorModule,
        MatDialogModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
