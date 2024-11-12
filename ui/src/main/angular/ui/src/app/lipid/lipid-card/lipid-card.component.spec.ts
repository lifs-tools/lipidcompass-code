import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidCardComponent } from './lipid-card.component';

describe('LipidCardComponent', () => {
  let component: LipidCardComponent;
  let fixture: ComponentFixture<LipidCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LipidCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
