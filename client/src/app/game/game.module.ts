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
import { ChatComponent } from './components/chat/chat.component';
import { MasterComponent } from './components/master/master.component';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from "@angular/material/tabs";
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from "@angular/material/slider";
import {MatTooltipModule} from "@angular/material/tooltip";

@NgModule({
  declarations: [
    GameComponent,
    ConsoleComponent,
    ChatComponent,
    MasterComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    GameRoutingModule,
    AngularSplitModule,
    MatToolbarModule,
    MatGridListModule,
    MatListModule,
    MatTabsModule,
    MatSelectModule,
    MatSliderModule,
    MatTooltipModule
  ],
  providers: [
    GameService,
    CommandService,
    HttpClientModule
  ]
})
export class GameModule {
}
