import {Component, Input} from '@angular/core';
import {GameOverview} from "../../../game/model/game-overview";
import {MatDialog} from "@angular/material/dialog";
import {GameService} from "../../../game/services/game.service";
import {Router} from "@angular/router";
import {ErrorService} from "../../../shared/services/error.service";
import {DeleteDialog} from "./delete-dialog.component";

@Component({
  selector: 'app-game-overview',
  templateUrl: './game-overview.component.html',
  styleUrls: ['./game-overview.component.scss']
})
export class GameOverviewComponent {

  @Input()
  game !: GameOverview

  constructor(public dialog: MatDialog,
              private gameService: GameService,
              private router: Router,
              private error: ErrorService) {
  }


  join() {

  }

  requestJoin() {
    this.gameService.requestJoin(this.game.name)
      .then(() => {
        this.game = {...this.game, userPermitted: "Pending"}
      })
      .catch(err => this.error.alert(err))
  }

  info() {
    alert("Coming soon")

  }

  copy() {
    alert("Coming soon")
  }

  delete() {
    const dialogRef = this.dialog.open(DeleteDialog, {data: {game: this.game}});

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.gameService.delete(this.game.name)
          .then(() => this.router.navigateByUrl('/dashboard'))
          .catch(err => this.error.alert(err))
      }
    });
  }
}

