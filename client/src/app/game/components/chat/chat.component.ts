import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from "rxjs";

export type ChatMessage = {

  /**
   * If its null and this message is outgoing, the message goes to every player in the room
   * */
  senderOrRecipient: string,
  msg: string
}

@Component({
  selector: 'app-chat',
  template: `
    <div id="terminal" (click)="inputline.focus()">
      <pre class="output" [innerHTML]="log" [scrollTop]="output.scrollHeight" #output></pre>
      <div class="inputArea">
        <mat-form-field class="dropDownRecipient">
          <mat-label>Senden an:</mat-label>
          <mat-select [(ngModel)]="selectedRecipient">
            <mat-option [value]="'Alle'">Alle</mat-option>
            <mat-option *ngFor="let player of players | async" [value]="player">
              {{player}}
            </mat-option>
          </mat-select>
        </mat-form-field>
        <div class="input">
          <textarea rows="1" (keydown)="input($event)" [(ngModel)]="inputLine" #inputline></textarea>
        </div>
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
  selectedRecipient = "Alle";

  ngOnInit() {
    this.incomingMessages.subscribe(({senderOrRecipient, msg}) => {
      this.appendLine(`${senderOrRecipient}: ${msg}`)
    })
  }

  appendLine(line: string) {
    this.log += '<span class="command">' + line + '\n</span>';
  }

  input(e: any) {
    if (e.keyCode == 13) {
      const msg: string = e.target.value.replaceAll('\n', '');
      if (msg == "") return;
      this.inputLine = ""
      this.appendLine(`You: ${msg}`)
      if(this.selectedRecipient == 'Alle'){
        this.outgoingMessages.emit({
         senderOrRecipient: '',
          msg,
        })
      }else{
        this.outgoingMessages.emit({
          senderOrRecipient: this.selectedRecipient,
          msg
        })
      }
    }
  }

}
