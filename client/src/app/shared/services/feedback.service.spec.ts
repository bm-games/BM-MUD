import { TestBed } from '@angular/core/testing';

import { FeedbackService } from './feedback.service';
import {MatDialogHarness} from "@angular/material/dialog/testing";
import {MatDialogModule} from "@angular/material/dialog";

describe('FeedbackService', () => {
  let service: FeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule
      ]
    });
    service = TestBed.inject(FeedbackService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
