import {StringMap} from "./DungeonConfig";

export interface CommandConfig {
  aliases: StringMap<string>
  customCommands: StringMap<string>
}
