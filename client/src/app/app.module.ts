import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ClientConfig, CONFIG} from "./client-config";
import {ConfiguratorModule} from "./configurator/configurator.module";
import {GameModule} from "./game/game.module";
import {DashboardModule} from "./dashboard/dashboard.module";
import {AuthenticationModule} from "./authentication/authentication.module";
import {AuthService} from "./authentication/services/auth.service";

export const LOCAL_CONFIG: ClientConfig = {
  endpoint: "http://play.bm-games.net",
  websocketEndpoint: "ws://play.bm-games.net/game",
};

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ConfiguratorModule,
    GameModule,
    DashboardModule,
    AuthenticationModule
  ],
  providers: [
    {provide: CONFIG, useValue: LOCAL_CONFIG},
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
