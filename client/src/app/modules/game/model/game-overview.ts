import {DungeonConfig} from "../../configurator/model/dungeon-config";

export interface GameOverview {
  readonly config: DungeonConfig;
  readonly onlinePlayers: number;
  readonly masterOnline: boolean;
  readonly avatarCount: number;
  readonly userPermitted: boolean;
}
