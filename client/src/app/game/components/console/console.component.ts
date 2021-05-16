import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Observable} from "rxjs";

@Component({
  selector: 'app-console',
  template: `
    <div id="terminal" (click)="inputline.focus()">
      <pre class="output" [innerHTML]="log" [scrollTop]="output.scrollHeight" #output></pre>
      <div class="input">
        <textarea rows="1" (keydown)="input($event)" [(ngModel)]="inputLine" #inputline></textarea>
      </div>
    </div>
  `,
  styleUrls: ['./console.component.scss']
})
export class ConsoleComponent implements OnInit {

  @Input()
  lines!: Observable<string>

  @Output()
  readonly lineEntered = new EventEmitter<string>();

  log = "";
  inputLine = "";

  ngOnInit() {
    this.lines.subscribe(line => this.appendLine(line))
  }

  appendLine(line: string) {
    this.log += '<span class="command">' + line + '\n</span>';
  }

  appendInput(line: string) {
    this.log += '<span class="command">$ ' + line + '\n</span>';
  }

  input(e: any) {
    if (e.keyCode == 13) {
      const value = e.target.value.replaceAll('\n', '');
      if (value == "") return;
      this.inputLine = ""
      if (value == "clear") {
        this.log = "";
        return;
      }
      this.appendInput(value)
      this.lineEntered.emit(value)

    }
  }
}
