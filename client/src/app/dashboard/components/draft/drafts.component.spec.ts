import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DraftsComponent} from './drafts.component';
import {MatDialogModule} from "@angular/material/dialog";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {CONFIG} from "../../../client-config";
import {LOCAL_CONFIG} from "../../../app.module";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetModule, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";

describe('DraftsComponent', () => {
  let component: DraftsComponent;
  let fixture: ComponentFixture<DraftsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DraftsComponent],
      providers: [
        { provide: MatBottomSheetRef, useValue: {} },
        {provide: MAT_BOTTOM_SHEET_DATA, useValue: []},
        {provide: CONFIG, useValue: LOCAL_CONFIG},
      ],
      imports: [
        MatDialogModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MatBottomSheetModule,
        NoopAnimationsModule
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DraftsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
