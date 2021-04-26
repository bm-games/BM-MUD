import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AvatarConfigurationRoutingModule} from "./avatarconfigurator-routing.module";
import {MatTabsModule} from "@angular/material/tabs";
import {MatSliderModule} from "@angular/material/slider";
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatListModule} from '@angular/material/list';
import {MatGridListModule} from '@angular/material/grid-list';

import {FormsModule} from "@angular/forms";
import {AvatarconfiguratorComponent} from "./components/avatarconfigurator.component";
import {MatCardModule} from "@angular/material/card";


@NgModule({
  declarations: [
    AvatarconfiguratorComponent
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
        FormsModule,
        MatCardModule
    ]
})
export class ConfiguratorModule { }
