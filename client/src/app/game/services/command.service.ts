import {Inject, Injectable} from '@angular/core';
import {ClientConfig, CONFIG} from "../../client-config";
import {identity, Observable, Subject} from "rxjs";
import {webSocket} from 'rxjs/webSocket';
import {Message} from "../model/message";
import {map, tap} from "rxjs/operators";

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

  connectAsPlayer(game: string, avatar: string): SocketConnection {
    return this.connect(`${this.CONFIG.websocketEndpoint}/game/play/${game}/${avatar}`)
  }

  connectAsMaster(game: string): SocketConnection {
    return this.connect(`${this.CONFIG.websocketEndpoint}/game/control/${game}`)
  }


  private connect(url: string): SocketConnection {
    const close$ = new Subject<string>()
    const socket = webSocket<string>({
      url,
      serializer: identity,
      deserializer: ({data}) => data,
      closeObserver: {
        next(closeEvent) {
          close$.next(closeEvent.reason)
        }
      }
    })

    return {
      incoming: socket.pipe(map(message => JSON.parse(message))),
      send(command: string) {
        socket.next(command);
      },
      disconnect() {
        socket.complete()
      }
    }
  }

}
