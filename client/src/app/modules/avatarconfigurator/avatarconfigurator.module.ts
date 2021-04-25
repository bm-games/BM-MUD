import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AvatarConfigurationRoutingModule, routingComponents } from "./avatarconfigurator-routing.module";
import {MatTabsModule} from "@angular/material/tabs";
import {MatSliderModule} from "@angular/material/slider";
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatListModule} from '@angular/material/list';
import {MatGridListModule} from '@angular/material/grid-list';

import {FormsModule} from "@angular/forms";
import {AvatarconfiguratorComponent} from "./components/avatarconfigurator.component";


@NgModule({
  declarations: [
    AvatarconfiguratorComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    AvatarConfigurationRoutingModule,
    MatTabsModule,
    MatSliderModule,
    MatSelectModule,
    MatCheckboxModule,
    MatListModule,
    MatGridListModule,
    FormsModule
  ]
})
export class ConfiguratorModule { }
