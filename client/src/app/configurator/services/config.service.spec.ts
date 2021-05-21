import {TestBed} from '@angular/core/testing';

import {ConfigService} from './config.service';
import {CONFIG} from "../../client-config";
import {LOCAL_CONFIG} from "../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ConfigService', () => {
  let service: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG},
      ]
    });
    service = TestBed.inject(ConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
