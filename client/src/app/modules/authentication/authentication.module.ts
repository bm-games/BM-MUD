import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthComponent } from './components/auth/auth.component';
import { AuthenticationRoutingModule, routingComponents } from "./authentication-routing.module";
import {AuthService} from "./services/auth.service";
import {AuthGuard} from "./services/auth.guard";


@NgModule({
  declarations: [
    AuthComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule
  ],
  providers:[AuthGuard]
})
export class AuthenticationModule { }
