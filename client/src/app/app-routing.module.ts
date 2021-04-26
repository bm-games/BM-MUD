import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AuthGuard} from "./modules/authentication/services/auth.guard";

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./modules/authentication/authentication.module').then(m => m.AuthenticationModule)
  },
  {
    path: 'configurator',
    loadChildren: () => import('./modules/configurator/configurator.module').then(m => m.ConfiguratorModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'avatarConfigurator',
    loadChildren: () => import('./modules/avatarconfigurator/avatarconfigurator.module').then(m => m.ConfiguratorModule),
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    // pathMatch: 'full',
    redirectTo: 'auth'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
