import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GameService} from "./services/game.service";
import {CommandService} from "./services/command.service";
import {ConfigurationRoutingModule} from "../configurator/configurator-routing.module";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [
    GameService,
    CommandService,
    HttpClientModule
  ]
})
export class GameModule {
}
