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

  getDungeonConfig(name: string): Promise<DungeonConfig> {
    return this.http.get<DungeonConfig>(`${this.CONFIG.endpoint}/configurator/get/${name}`)
      .toPromise();
  }

  createDungeon(dungeonConfig: DungeonConfig): Promise<void> {
    console.log(dungeonConfig);
    console.log(JSON.stringify(dungeonConfig));

    return this.http.post<void>(`${this.CONFIG.endpoint}/configurator/createConfig`, dungeonConfig).toPromise();
  }

}
