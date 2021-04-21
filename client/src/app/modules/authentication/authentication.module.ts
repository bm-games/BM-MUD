import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthComponent } from './components/auth/auth.component';
import { AuthenticationRoutingModule, routingComponents } from "./authentication-routing.module";


@NgModule({
  declarations: [
    AuthComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule
  ]
})
export class AuthenticationModule { }
