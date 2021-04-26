import {Component} from '@angular/core';
import {GameService} from "./modules/game/services/game.service";
import {ConfigService} from "./modules/configurator/services/config.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Client';

  constructor( ) {
  }
}
