import {Component, Input} from '@angular/core';
import {GameOverview} from "../../../game/model/game-overview";
import {MatDialog} from "@angular/material/dialog";
import {GameService} from "../../../game/services/game.service";
import {Router} from "@angular/router";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {DeleteDialog} from "./delete-dialog.component";
import {ConfigService} from "../../../configurator/services/config.service";
import {ConfigurationComponent} from "../../../configurator/components/configuration/configuration.component";
import {RoomConfig} from "../../../configurator/models/RoomConfig";

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
              private configService: ConfigService,
              private router: Router,
              private feedback: FeedbackService) {
  }


  join() {
    alert("Coming soon")
    // this.feedback.showLoadingOverlay()
    // this.feedback.stopLoadingOverlay()

  }

  requestJoin() {
    this.feedback.showLoadingOverlay()
    this.gameService.requestJoin(this.game.name)
      .then(() => {
        this.game = {...this.game, userPermitted: "Pending"}
      })
      .catch(err => this.feedback.showError(err))
      .finally(() => this.feedback.stopLoadingOverlay())
  }

  info() {
    alert("Coming soon")
    // this.feedback.showLoadingOverlay()
    // this.feedback.stopLoadingOverlay()
  }

  async copy() {
    this.feedback.showLoadingOverlay()
    this.configService.getDungeonConfig(this.game.name)
      .then(config => this.router.navigateByUrl("/configurator")
        .then(() => {
          ConfigurationComponent.allRaces = config.races
          ConfigurationComponent.allClasses = config.classes
          ConfigurationComponent.allItems = Object.values(config.itemConfigs)
          ConfigurationComponent.allNPCs = Object.values(config.npcConfigs)
          ConfigurationComponent.commandConfig = config.commandConfig
          ConfigurationComponent.allRooms = Object.values(config.rooms)
            .map<RoomConfig>(({name, message, npcs, items}, index) =>
              ({
                id: index,
                name,
                message,
                npcs: Object.values(npcs),
                items,
                north: undefined,
                west: undefined,
                south: undefined,
                east: undefined
              }))
          ConfigurationComponent.startequipment = config.startEquipment
          ConfigurationComponent.startRoom = config.startRoom
        })
      ).catch(error => this.feedback.showError(error))
      .finally(() => this.feedback.stopLoadingOverlay())
  }

  delete() {
    this.feedback.showLoadingOverlay()
    const dialogRef = this.dialog.open(DeleteDialog, {data: {game: this.game}});

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.gameService.delete(this.game.name)
          .then(() => this.router.navigateByUrl('/dashboard'))
          .catch(err => this.feedback.showError(err))
          .finally(() => this.feedback.stopLoadingOverlay())
      } else {
        this.feedback.stopLoadingOverlay()
      }
    });
  }
}

