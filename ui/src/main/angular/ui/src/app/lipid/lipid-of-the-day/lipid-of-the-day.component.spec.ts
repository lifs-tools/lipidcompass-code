import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidOfTheDayComponent } from './lipid-of-the-day.component';

describe('LipidOfTheDayComponent', () => {
  let component: LipidOfTheDayComponent;
  let fixture: ComponentFixture<LipidOfTheDayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LipidOfTheDayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LipidOfTheDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
