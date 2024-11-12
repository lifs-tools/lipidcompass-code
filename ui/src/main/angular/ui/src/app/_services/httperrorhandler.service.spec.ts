import { TestBed } from '@angular/core/testing';

import { HttperrorhandlerService } from './httperrorhandler.service';

describe('HttperrorhandlerService', () => {
  let service: HttperrorhandlerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttperrorhandlerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
