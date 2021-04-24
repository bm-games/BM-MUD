import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationComponent } from './components/configuration/configuration.component';
import { ConfigurationRoutingModule, routingComponents } from "./configurator-routing.module";
import {ConfigService} from "./services/config.service";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    ConfigurationComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    ConfigurationRoutingModule,
    HttpClientModule
  ],
  providers: [
    ConfigService
  ]
})
export class ConfiguratorModule { }
