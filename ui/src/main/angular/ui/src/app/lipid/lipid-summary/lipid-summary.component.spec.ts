import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidSummaryComponent } from './lipid-summary.component';

describe('LipidSummaryComponent', () => {
  let component: LipidSummaryComponent;
  let fixture: ComponentFixture<LipidSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LipidSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
