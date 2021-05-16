import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {GameService} from "../../services/game.service";
import {GameDetail} from "../../model/game-detail";
import {MatDialog} from "@angular/material/dialog";
import {AvatarConfigComponent} from "../avatar/avatar-config.component";

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  readonly game!: string;
  detail!: GameDetail;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private feedback: FeedbackService,
              private gameService: GameService,
              private dialog: MatDialog) {
    this.game = route.snapshot.paramMap.get("game") || ''
    if (!this.game) {
      router.navigateByUrl("/dasboard")
      return
    }

  }

  ngOnInit(): void {
    this.feedback.showLoadingOverlay()
    this.gameService.getGameDetail(this.game)
      .then(detail => this.detail = detail)
      .catch(error => {
        this.feedback.showError(error)
        return this.router.navigateByUrl("/dashboard")
      })
      .finally(() => this.feedback.stopLoadingOverlay())
  }

  createAvatar() {
    this.dialog.open(AvatarConfigComponent, {data: this.detail})
  }
}
