import { TestBed, inject } from '@angular/core/testing';

import { LipidsearchService } from './lipidsearch.service';

describe('LipidsearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LipidsearchService]
    });
  });

  it('should be created', inject([LipidsearchService], (service: LipidsearchService) => {
    expect(service).toBeTruthy();
  }));
});
