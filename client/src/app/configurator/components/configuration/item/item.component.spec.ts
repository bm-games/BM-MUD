import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ItemComponent} from './item.component';
import {EquipmentSlot} from "../../../models/EquipmentSlot";
import {EquipmentConfig} from "../../../models/EquipmentConfig";
import {ConsumableItemConfig, WeaponConfig} from "../../../models/Item";

describe('ItemComponent', () => {
  let component: ItemComponent;
  let fixture: ComponentFixture<ItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ItemComponent]
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

  it('should add new consumable Item', () => {
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isConsumable = true;
    component.name = 'Item';
    component.effect = 'effect';

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);
    let configuredItem = component.configuredItems[length - 1] as ConsumableItemConfig;
    expect(configuredItem.name).toEqual('Item');
    expect(configuredItem.effect).toEqual('effect');
  });

  it('should add new equipment Item', () => {
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isEquipment = true;
    component.name = 'Item';
    component.health = 1;
    component.damageModifier = 1;
    component.equipmentSlot = EquipmentSlot.head;

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);

    let item = component.configuredItems[length - 1] as EquipmentConfig;
    expect(item.name).toEqual('Item');
    expect(item.healthModifier).toBe(1);
  });

  it('should add new weapon Item', () => {
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;

    component.isWeapon = true;
    component.name = 'Item';
    component.damage = 1;
    component.damageModifier = 2;

    component.addItem();

    let length = component.configuredItems.length;

    expect(length).toBe(1);
    let item = component.configuredItems[length - 1] as WeaponConfig;
    expect(item.name).toEqual('Item');
    expect(item.damage).toBe(1);
  });
});
