import {Component} from '@angular/core';
import {GameService} from "./game/services/game.service";
import {ConfigService} from "./configurator/services/config.service";

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
