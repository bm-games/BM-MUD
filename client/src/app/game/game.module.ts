import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GameService} from "./services/game.service";
import {CommandService} from "./services/command.service";
import {HttpClientModule} from "@angular/common/http";
import {GameComponent} from './components/game.component';
import {AngularSplitModule} from 'angular-split';
import {MatToolbarModule} from "@angular/material/toolbar";
import {GameRoutingModule} from "./game-routing.module";
import { ConsoleComponent } from './components/console/console.component';
import {MatGridListModule} from "@angular/material/grid-list";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    GameComponent,
    ConsoleComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    GameRoutingModule,
    AngularSplitModule,
    MatToolbarModule,
    MatGridListModule
  ],
  providers: [
    GameService,
    CommandService,
    HttpClientModule
  ]
})
export class GameModule {
}
