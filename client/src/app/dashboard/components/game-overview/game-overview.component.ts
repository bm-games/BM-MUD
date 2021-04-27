import {Component, Input, OnInit} from '@angular/core';
import {GameOverview} from "../../../game/model/game-overview";

@Component({
  selector: 'app-game-overview',
  templateUrl: './game-overview.component.html',
  styleUrls: ['./game-overview.component.scss']
})
export class GameOverviewComponent implements OnInit {

  @Input()
  game !: GameOverview

  constructor() {
  }

  ngOnInit(): void {
  }

}
