import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GameService} from "./services/game.service";
import {CommandService} from "./services/command.service";
import {GameComponent} from './components/game.component';
import {AngularSplitModule} from 'angular-split';
import {MatToolbarModule} from "@angular/material/toolbar";
import {GameRoutingModule} from "./game-routing.module";
import {ConsoleComponent} from './components/console/console.component';
import {MatGridListModule} from "@angular/material/grid-list";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChatComponent} from './components/chat/chat.component';
import {MasterComponent} from './components/master/master.component';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from "@angular/material/tabs";
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from "@angular/material/slider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {AvatarConfigComponent} from "./components/avatar/avatar-config.component";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatCardModule} from "@angular/material/card";
import { OverviewComponent } from './components/overview/overview.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatChipsModule} from "@angular/material/chips";
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [
    GameComponent,
    ConsoleComponent,
    ChatComponent,
    MasterComponent,
    AvatarConfigComponent,
    OverviewComponent
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
    MatTooltipModule,
    MatFormFieldModule,
    MatSelectModule,
    MatCardModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule
  ],
  providers: [
    GameService,
    CommandService
  ]
})
export class GameModule {
}
