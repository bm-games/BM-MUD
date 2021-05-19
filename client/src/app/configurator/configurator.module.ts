import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConfigurationComponent} from './components/configuration/configuration.component';
import {ConfigurationRoutingModule, routingComponents} from "./configurator-routing.module";
import {ConfigService} from "./services/config.service";
import {MatTabsModule} from "@angular/material/tabs";
import {MatSliderModule} from "@angular/material/slider";
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatListModule} from '@angular/material/list';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatTableModule} from '@angular/material/table';
import {ClassComponent} from './components/configuration/class/class.component';
import {RoomComponent} from './components/configuration/room/room.component';
import {NPCComponent} from './components/configuration/npc/npc.component';
import {StartequipmentComponent} from './components/configuration/startequipment/startequipment.component';
import {CommandComponent} from './components/configuration/command/command.component';
import {ItemComponent} from './components/configuration/item/item.component';
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatInputModule} from "@angular/material/input";


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
        MatSliderModule,
        MatSelectModule,
        MatCheckboxModule,
        MatListModule,
        MatGridListModule,
        MatTableModule,
        FormsModule,
        MatButtonModule,
        MatTooltipModule,
        MatInputModule
    ],
  providers: [
    ConfigService
  ]
})
export class ConfiguratorModule { }
