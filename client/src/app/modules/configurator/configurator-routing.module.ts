import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfigurationComponent } from "./components/configuration/configuration.component";

const routes: Routes = [
  {
    path: '',
    component: ConfigurationComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurationRoutingModule { }
export const routingComponents = [ConfigurationComponent];
