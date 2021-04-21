import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DashboardRoutingModule, routingComponents } from "./dashboard-routing.module";


@NgModule({
  declarations: [
    DashboardComponent,
    routingComponents
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule
  ]
})
export class DashboardModule { }
