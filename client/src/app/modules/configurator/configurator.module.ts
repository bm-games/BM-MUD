import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationComponent } from './components/configuration/configuration.component';
import { ConfigurationRoutingModule, routingComponents } from "./configurator-routing.module";
import {MatTabsModule} from "@angular/material/tabs";
import {MatSliderModule} from "@angular/material/slider";
import { ClassComponent } from './components/configuration/class/class.component';
import { RoomComponent } from './components/configuration/room/room.component';
import { NPCComponent } from './components/configuration/npc/npc.component';
import { StartequipmentComponent } from './components/configuration/startequipment/startequipment.component';
import { CommandComponent } from './components/configuration/command/command.component';
import { ItemComponent } from './components/configuration/item/item.component';


@NgModule({
  declarations: [
    ConfigurationComponent,
    routingComponents,
    ClassComponent,
    RoomComponent,
    NPCComponent,
    StartequipmentComponent,
    CommandComponent,
    ItemComponent
  ],
  imports: [
    CommonModule,
    ConfigurationRoutingModule,
    MatTabsModule,
    MatSliderModule
  ]
})
export class ConfiguratorModule { }
