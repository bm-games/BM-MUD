import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthComponent} from './components/auth/auth.component';
import {AuthenticationRoutingModule} from "./authentication-routing.module";
import {AuthGuard} from "./services/auth.guard";
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {MatIconModule} from "@angular/material/icon";
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    AuthComponent,
    LoginComponent,
    RegisterComponent,
    ResetPasswordComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    MatIconModule,
    ReactiveFormsModule
  ],
  providers:[AuthGuard]
})
export class AuthenticationModule { }
