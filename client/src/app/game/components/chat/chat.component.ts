import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from "rxjs";

export type ChatMessage = {

  /**
   * If its null and this message is outgoing, the message goes to every player in the room
   * */
  senderOrRecipient?: string,
  msg: string
}

@Component({
  selector: 'app-chat',
  template: `
    <div id="chat">
      <mat-form-field class="recipient">
        <mat-label>Senden an:</mat-label>
        <mat-select [(ngModel)]="selectedRecipient">
          <mat-option>Alle</mat-option>
          <mat-option *ngFor="let player of players | async" [value]="player">
            {{player}}
          </mat-option>
        </mat-select>
      </mat-form-field>
      <mat-divider style="margin-top: -1rem"></mat-divider>
      <pre class="output" [innerHTML]="log" [scrollTop]="output.scrollHeight" #output></pre>
      <mat-divider></mat-divider>
      <div class="input">
        <input (keydown)="input($event)"
               [(ngModel)]="inputLine"
               placeholder="Nachricht" #msg>
        <button mat-icon-button (click)="send(msg.value)" color="primary">
          <mat-icon>send</mat-icon>
        </button>
      </div>
    </div>
  `,
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  @Input()
  incomingMessages!: Observable<ChatMessage>

  @Input()
  players!: Observable<string[]>

  @Output()
  readonly outgoingMessages = new EventEmitter<ChatMessage>();

  log = "";
  inputLine = "";
  selectedRecipient?: string;

  ngOnInit() {
    this.incomingMessages.subscribe(({senderOrRecipient, msg}) => {
      this.appendLine(`${senderOrRecipient}: ${msg}`, senderOrRecipient ? "received" : "master")
    })
  }

  appendLine(line: string, color: "master" | "sent" | "received") {
    this.log += '<span class="command ' + color + '">' + line + '\n</span>';
  }

  input(e: any) {
    if (e.keyCode == 13) {
      this.send(e.target.value)
    }
  }

  send(msg: string) {
    msg = msg.replace('\n', '');
    if (msg == "") return;
    this.inputLine = ""
    this.appendLine(`Du: ${msg}`, "sent")
    this.outgoingMessages.emit({
      senderOrRecipient: this.selectedRecipient ? encodeURIComponent(this.selectedRecipient) : undefined,
      msg: encodeURIComponent(msg)
    })
  }
}
