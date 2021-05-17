import {Component, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {AuthService} from "../../../authentication/services/auth.service";
import {User} from "../../../authentication/model/user";
import {GameService} from "../../../game/services/game.service";
import {GameOverview} from "../../../game/model/game-overview";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordDialog} from "../password/change-password-dialog";
import {FeedbackService} from "../../../shared/services/feedback.service";
import {DungeonConfig} from "../../../configurator/models/DungeonConfig";
import {ConfigService} from "../../../configurator/services/config.service";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {DraftsComponent} from "../draft/drafts.component";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  title = "BM-MUD: Dashboard";

  user: User | null = null;
  games: GameOverview[] = [];
  filteredGames: GameOverview[] = [];
  drafts!: Partial<DungeonConfig>[];

  constructor(private titleService: Title,
              private gameService: GameService,
              private configService: ConfigService,
              private auth: AuthService,
              private router: Router,
              public dialog: MatDialog,
              private feedback: FeedbackService,
              private draftsSheet: MatBottomSheet) {
  }

  ngOnInit(): void {
    this.setTitle(this.title);

    this.feedback.showLoadingOverlay()
    this.gameService.getAvailableGames().toPromise()
      .then(games => {
        this.games = games
        this.searchedGameName = ""
      })
      .catch(error => this.feedback.showError(error))
      .finally(() => this.feedback.stopLoadingOverlay())
    this.auth.user.subscribe(user => this.user = user)
  }

  public setTitle(newTitle: string) {
    this.titleService.setTitle(newTitle);
  }

  logout() {
    this.auth.logout()
      .then(() => this.router.navigateByUrl("/auth"))
      .catch(console.error)
  }

  set searchedGameName(name: string) {
    this.filteredGames = this.games
      .filter(game => game.name.toLowerCase().includes(name.toLowerCase()))
  }

  changePasswordDialog() {
    const dialogRef = this.dialog.open(ChangePasswordDialog, {
      width: '300px'
    });
  }

  showDrafts() {
    this.draftsSheet.open(DraftsComponent, {data: this.configService.getDrafts()})
  }
}
