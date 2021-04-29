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

  it('should return 5 as next free ClassId. Already assigned IDs: 0,1,2,3,4 ', () => {
    fixture = TestBed.createComponent(ClassComponent);
    component = fixture.componentInstance;

    component.configuredClasses = [
      new ClassConfig(0, 'Test', 1, 1, 1, ''),
      new ClassConfig(1, 'Test', 1, 1, 1, ''),
      new ClassConfig(2, 'Test', 1, 1, 1, ''),
      new ClassConfig(3, 'Test', 1, 1, 1, ''),
      new ClassConfig(4, 'Test', 1, 1, 1, '')
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(5);
  });

  it('should return 2 as next free ClassId. Already assigned IDs: 0,1,3,4', () => {
    fixture = TestBed.createComponent(ClassComponent);
    component = fixture.componentInstance;

    component.configuredClasses = [
      new ClassConfig(0, 'Test', 1, 1, 1, ''),
      new ClassConfig(1, 'Test', 1, 1, 1, ''),
      new ClassConfig(3, 'Test', 1, 1, 1, ''),
      new ClassConfig(4, 'Test', 1, 1, 1, '')
    ];

    let calculatedId = component.getNextFreeId();

    expect(calculatedId).toBe(2);
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
    expect(component.configuredClasses[length-1].id).toBe(0);
    expect(component.configuredClasses[length-1].name).toEqual('Test');
    expect(component.configuredClasses[length-1].healthMultiplier).toBe(1);
    expect(component.configuredClasses[length-1].damage).toBe(2);
    expect(component.configuredClasses[length-1].attackSpeed).toBe(3);
    expect(component.configuredClasses[length-1].description).toEqual('TestDesc');
  });
});
