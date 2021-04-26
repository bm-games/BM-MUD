import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {AuthComponent} from "./components/auth/auth.component";
import {ResetPasswordComponent} from "./components/reset-password/reset-password.component";

const routes: Routes = [
  {
    path: '', component: AuthComponent,
    children: [
      {path: '', component: LoginComponent},
      {path: 'register', component: RegisterComponent},
      {path: 'reset-password', component: ResetPasswordComponent},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthenticationRoutingModule {
}
