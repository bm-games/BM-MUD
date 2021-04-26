import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvatarconfiguratorComponent } from './avatarconfigurator.component';

describe('AvatarconfiguratorComponent', () => {
  let component: AvatarconfiguratorComponent;
  let fixture: ComponentFixture<AvatarconfiguratorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvatarconfiguratorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AvatarconfiguratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
