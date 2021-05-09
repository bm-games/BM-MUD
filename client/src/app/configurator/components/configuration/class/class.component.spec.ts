import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassComponent } from './class.component';
import {ClassConfig} from "../../../models/ClassConfig";

describe('ClassComponent', () => {
  let component: ClassComponent;
  let fixture: ComponentFixture<ClassComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClassComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add new ClassConfig', () =>{
    fixture = TestBed.createComponent(ClassComponent);
    component = fixture.componentInstance;

    component.name = 'Test';
    component.healthMultiplier = 1;
    component.damage = 2;
    component.attackSpeed = 3;
    component.description = 'TestDesc';

    component.addClass();

    let length = component.configuredClasses.length;
    expect(length).toBe(1);
    expect(component.configuredClasses[length-1].name).toEqual('Test');
    expect(component.configuredClasses[length-1].healthMultiplier).toBe(1);
    expect(component.configuredClasses[length-1].damage).toBe(2);
    expect(component.configuredClasses[length-1].attackSpeed).toBe(3);
    expect(component.configuredClasses[length-1].description).toEqual('TestDesc');
  });
});
