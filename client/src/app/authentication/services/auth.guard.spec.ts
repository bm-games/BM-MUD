import {TestBed} from '@angular/core/testing';

import {AuthGuard} from './auth.guard';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {CONFIG} from "../../client-config";
import {LOCAL_CONFIG} from "../../app.module";

describe('AuthGuard', () => {
  let guard: AuthGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        {provide: CONFIG, useValue: LOCAL_CONFIG},
      ]
    });
    guard = TestBed.inject(AuthGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
