/* eslint-disable @typescript-eslint/no-unused-vars */

import { TestBed, async, inject } from '@angular/core/testing';
import { PlotService } from './plot.service';

describe('Service: Plotservice', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PlotService]
    });
  });

  it('should ...', inject([PlotService], (service: PlotService) => {
    expect(service).toBeTruthy();
  }));
});
