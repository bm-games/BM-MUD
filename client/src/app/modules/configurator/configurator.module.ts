import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationComponent } from './components/configuration/configuration.component';
import { ConfigurationRoutingModule, routingComponents } from "./configurator-routing.module";


@NgModule({
  declarations: [
    ConfigurationComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    ConfigurationRoutingModule
  ]
})
export class ConfiguratorModule { }
