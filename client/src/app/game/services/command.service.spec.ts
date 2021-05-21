import {TestBed} from '@angular/core/testing';

import {CommandService} from './command.service';
import {CONFIG} from "../../client-config";
import {LOCAL_CONFIG} from "../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CommandService', () => {
  let service: CommandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG},
      ]
    });
    service = TestBed.inject(CommandService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
