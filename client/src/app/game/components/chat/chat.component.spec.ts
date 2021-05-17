import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ChatComponent} from './chat.component';
import {Subject} from "rxjs";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChatComponent],
      imports: [NoopAnimationsModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
    component.incomingMessages = new Subject()
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
