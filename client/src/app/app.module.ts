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
import {environment} from "../environments/environment";
import {CookieInterceptor} from "./authentication/services/cookie-interceptor.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ErrorDialog, FeedbackService} from "./shared/services/feedback.service";
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {FullscreenOverlayContainer, OverlayContainer} from "@angular/cdk/overlay";

export const LOCAL_CONFIG: ClientConfig = {
  endpoint: environment.endpoint,
  websocketEndpoint: environment.websocketEndpoint,
};

@NgModule({
  declarations: [
    AppComponent, ErrorDialog
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    ConfiguratorModule,
    GameModule,
    DashboardModule,
    AuthenticationModule,
    MatDialogModule,
    MatButtonModule
  ],
  providers: [
    {provide: CONFIG, useValue: LOCAL_CONFIG},
    {provide: HTTP_INTERCEPTORS, useClass: CookieInterceptor, multi: true},
    {provide: OverlayContainer, useClass: FullscreenOverlayContainer},
    AuthService,
    FeedbackService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
