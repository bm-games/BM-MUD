import { TestBed } from '@angular/core/testing';

import { CookieInterceptor } from './cookie-interceptor.service';

describe('CookieInterceptorInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      CookieInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: CookieInterceptor = TestBed.inject(CookieInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
