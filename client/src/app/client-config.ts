import {InjectionToken} from "@angular/core";

export interface ClientConfig {
  readonly endpoint: string
  readonly websocketEndpoint: string
}


export const CONFIG = new InjectionToken<ClientConfig>("CLIENT_CONFIG");
