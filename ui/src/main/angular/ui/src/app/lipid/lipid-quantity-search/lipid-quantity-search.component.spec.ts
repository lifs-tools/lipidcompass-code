import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidQuantitySearchComponent } from './lipid-quantity-search.component';

describe('LipidQuantitySearchComponent', () => {
  let component: LipidQuantitySearchComponent;
  let fixture: ComponentFixture<LipidQuantitySearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LipidQuantitySearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidQuantitySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
