import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ConfigurationComponent} from "./components/configuration/configuration.component";
import {RaceComponent} from "./components/configuration/race/race.component";

const routes: Routes = [
  {
    path: '',
    component: ConfigurationComponent,
    children: [
      {
        path: "race",
        component: RaceComponent
      }
    ]
    // pathMatch: "full"
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurationRoutingModule {
}

export const routingComponents = [ConfigurationComponent, RaceComponent];
