import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationComponent } from './components/configuration/configuration.component';
import { ConfigurationRoutingModule, routingComponents } from "./configurator-routing.module";
import {MatTabsModule} from "@angular/material/tabs";


@NgModule({
  declarations: [
    ConfigurationComponent,
    routingComponents
  ],
    imports: [
        CommonModule,
        ConfigurationRoutingModule,
        MatTabsModule
    ]
})
export class ConfiguratorModule { }
