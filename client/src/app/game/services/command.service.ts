import {Inject, Injectable} from '@angular/core';
import {ClientConfig, CONFIG} from "../../client-config";
import {Observable, Subject} from "rxjs";
import {webSocket} from 'rxjs/webSocket';
import {Message} from "../model/message";
import {map} from "rxjs/operators";

export interface SocketConnection {
  incoming: Observable<Message>,

  send(command: string): void

  disconnect(): void
}

@Injectable({
  providedIn: 'root'
})
export class CommandService {


  constructor(
    @Inject(CONFIG) private CONFIG: ClientConfig) {
  }

  connect(game: string, avatar: string): SocketConnection {
    const socket = webSocket<string>(`${this.CONFIG.websocketEndpoint}/api/game/play/${game}/${avatar}`)

    return {
      incoming: socket.pipe(map(msg => JSON.parse(msg))),
      send(command: string) {
        socket.next(command);
      },
      disconnect() {
        socket.complete()
      }
    }
  }


}
