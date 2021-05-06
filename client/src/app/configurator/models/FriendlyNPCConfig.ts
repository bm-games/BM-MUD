import {NPCConfig} from "./NPCConfig";

export interface FriendlyNPCConfig extends NPCConfig{
  commandOnInteraction: string | undefined;
  messageOnTalk: string | undefined;
}
