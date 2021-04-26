import {Component, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {AuthService} from "../../../authentication/services/auth.service";
import {User} from "../../../authentication/model/user";
import {GameService} from "../../../game/services/game.service";
import {GameOverview} from "../../../game/model/game-overview";
import {Router} from "@angular/router";

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

  constructor(private titleService: Title,
              private gameService: GameService,
              private auth: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.setTitle(this.title);

    this.gameService.getAvailableGames().subscribe(games => {
      this.games = games
      this.searchedGameName = ""
    })
    this.auth.user.subscribe(user => this.user = user)
  }

  public setTitle(newTitle: string) {
    this.titleService.setTitle(newTitle);
  }

  logout() {
    this.auth.logout()
      .then(() => this.router.navigateByUrl("/"));
  }

  set searchedGameName(name: string) {
    this.filteredGames = this.games
      .filter(game => game.config.name.toLowerCase().includes(name.toLowerCase()))
  }
}
