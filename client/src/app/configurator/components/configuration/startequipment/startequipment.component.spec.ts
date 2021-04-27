import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartequipmentComponent } from './startequipment.component';

describe('StartequipmentComponent', () => {
  let component: StartequipmentComponent;
  let fixture: ComponentFixture<StartequipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StartequipmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StartequipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
