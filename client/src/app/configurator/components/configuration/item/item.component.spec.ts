import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ItemComponent} from './item.component';
import {EquipmentSlot} from "../../../models/EquipmentSlot";

describe('ItemComponent', () => {
  let component: ItemComponent;
  let fixture: ComponentFixture<ItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add new consumable Item', () =>{
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isConsumable = true;
    component.name = 'Item';
    component.effect = 'effect';

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);
    expect(component.configuredItems[length-1].name).toEqual('Item');
    expect(component.configuredItems[length-1].effect).toEqual('effect');
    expect(component.configuredItems[length-1].isConsumable).toBe(true);
  });

  it('should add new equipment Item', () =>{
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isEquipment = true;
    component.name = 'Item';
    component.health = 1;
    component.equipmentSlot = EquipmentSlot.head;

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);
    expect(component.configuredItems[length-1].name).toEqual('Item');
    expect(component.configuredItems[length-1].isConsumable).toBe(false);
  });

  it('should add new weapon Item', () =>{
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isWeapon = true;
    component.name = 'Item';
    component.damage = 1;
    component.damageMultiplier = 2;

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);
    expect(component.configuredItems[length-1].name).toEqual('Item');
    expect(component.configuredItems[length-1].isConsumable).toBe(false);
  });
});
