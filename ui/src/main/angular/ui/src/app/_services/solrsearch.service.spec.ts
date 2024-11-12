import { TestBed, inject } from '@angular/core/testing';

import { SolrsearchService } from './solrsearch.service';

describe('SolrsearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SolrsearchService]
    });
  });

  it('should be created', inject([SolrsearchService], (service: SolrsearchService) => {
    expect(service).toBeTruthy();
  }));
});
