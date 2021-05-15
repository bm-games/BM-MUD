import {Inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {GameOverview} from "../model/game-overview";
import {ClientConfig, CONFIG} from "../../client-config";
import {AuthService} from "../../authentication/services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(
    @Inject(CONFIG) private CONFIG: ClientConfig,
    private http: HttpClient,
    private auth: AuthService) {
  }

  getAvailableGames(): Observable<GameOverview[]> {
    return this.http.get<GameOverview[]>(this.CONFIG.endpoint + '/game/list')
  }

  createAvatar(gameName: string, race: string, damage: number, health: number, avatarname: string){

  }

  delete(gameName: string): Promise<void> {
    return this.http.delete(`${this.CONFIG.endpoint}/game/delete/${gameName}`)
      .toPromise().then(() => {
      })
  }

  requestJoin(gameName: string): Promise<void> {
    return this.http.post(`${this.CONFIG.endpoint}/game/join/${gameName}`, null)
      .toPromise().then(() => {
      })
  }
}
