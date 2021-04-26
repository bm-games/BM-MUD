import {Inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {GameOverview} from "../model/game-overview";
import {ClientConfig, CONFIG} from "../../client-config";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(
    @Inject(CONFIG) private CONFIG: ClientConfig,
    private http: HttpClient) {
  }

  getAvailableGames(): Observable<GameOverview[]> {
    return this.http.get<GameOverview[]>(this.CONFIG.endpoint + '/game/availableGames')
  }

}
