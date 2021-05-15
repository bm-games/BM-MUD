import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvatarConfigComponent } from './avatar-config.component';

describe('AvatarconfiguratorComponent', () => {
  let component: AvatarConfigComponent;
  let fixture: ComponentFixture<AvatarConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvatarConfigComponent ]
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
