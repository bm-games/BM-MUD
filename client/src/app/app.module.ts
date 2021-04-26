import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ClientConfig, CONFIG} from "./client-config";
import {ConfiguratorModule} from "./modules/configurator/configurator.module";
import {GameModule} from "./modules/game/game.module";
import {DashboardModule} from "./modules/dashboard/dashboard.module";
import {AuthenticationModule} from "./modules/authentication/authentication.module";
import {AuthService} from "./modules/authentication/services/auth.service";

export const LOCAL_CONFIG: ClientConfig = {
  endpoint: "http://localhost:8080",
  websocketEndpoint: "ws://localhost/game",
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
