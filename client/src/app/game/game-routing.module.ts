import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GameComponent} from "./components/game.component";
import {MasterComponent} from "./components/master/master.component";

const routes: Routes = [
  {
    path: ':game/:avatar',
    component: GameComponent
  },
  {
    path: 'master',
    component: MasterComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GameRoutingModule { }

