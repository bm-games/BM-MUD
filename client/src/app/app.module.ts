import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ClientConfig, CONFIG} from "./client-config";
import {ConfiguratorModule} from "./modules/configurator/configurator.module";
import {GameModule} from "./modules/game/game.module";

const LOCAL_CONFIG: ClientConfig = {
  endpoint: "http://localhost:8080",
  websocketEndpoint: "ws://localhost/game",
};

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ConfiguratorModule,
    GameModule
  ],
  providers: [
    {provide: CONFIG, useValue: LOCAL_CONFIG}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
