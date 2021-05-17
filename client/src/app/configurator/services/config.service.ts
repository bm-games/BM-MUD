import {Inject, Injectable} from '@angular/core';
import {ClientConfig, CONFIG} from "../../client-config";
import {HttpClient} from "@angular/common/http";
import {DungeonConfig} from "../models/DungeonConfig";

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
    delete dungeonConfig.id
    return this.http.post<void>(`${this.CONFIG.endpoint}/configurator/createConfig`, dungeonConfig)
      .toPromise();
  }


  saveDraft(dungeonConfig: Partial<DungeonConfig>): void {
    if (!dungeonConfig.id) dungeonConfig = {...dungeonConfig, id: Date.now().toString()}
    localStorage.setItem("config.draft." + dungeonConfig.id, JSON.stringify(dungeonConfig))
  }

  getDrafts(): Partial<DungeonConfig>[] {
    return Object.entries(localStorage)
      .filter(([key, config]) => key?.startsWith("config.draft.") && config != null)
      .map(([_, config]) => JSON.parse(config))
  }

  deleteDraft(id?: string) {
    if (id) {
      localStorage.removeItem("config.draft." + id)
    }
  }
}
