import {Inject, Injectable} from '@angular/core';
import {ClientConfig, CONFIG} from "../../client-config";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {DungeonConfig} from "../models/DungeonConfig";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor(
    @Inject(CONFIG) private CONFIG: ClientConfig,
    private http: HttpClient) {
  }

  getDungeonConfig(name: string): Observable<DungeonConfig | null> {
    return this.http.get<DungeonConfig>(`${this.CONFIG.endpoint}/configurator/get/${name}`)
      .pipe(catchError(() => of(null)));
  }

  createDungeon(dungeonConfig: DungeonConfig): Observable<any> {
    console.log(dungeonConfig);
    return this.http.post(`${this.CONFIG.endpoint}/configurator/create`, dungeonConfig)
  }

}
