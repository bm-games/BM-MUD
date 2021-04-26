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

  constructor(
    public gameService: GameService,
    public configService: ConfigService
  ) {
  }

  ngOnInit() {
    this.gameService.getAvailableGames().subscribe(console.log)
    this.configService.getDungeonConfig("Test").subscribe(console.log)
    this.configService.createDungeon({name: "Test"}).subscribe(console.log)
  }
}
