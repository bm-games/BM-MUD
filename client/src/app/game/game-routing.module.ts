import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GameComponent} from "./components/game.component";
import {MasterComponent} from "./components/master/master.component";
import {AvatarConfigComponent} from "./components/avatar/avatar-config.component";
import {OverviewComponent} from "./components/overview/overview.component";

const routes: Routes = [
  {path: 'overview/:game', component: OverviewComponent},
  {path: 'player/:game/:avatar', component: GameComponent},
  {path: 'master/:game', component: MasterComponent},
  {path: 'avatar/create/:game', component: AvatarConfigComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GameRoutingModule {
}

