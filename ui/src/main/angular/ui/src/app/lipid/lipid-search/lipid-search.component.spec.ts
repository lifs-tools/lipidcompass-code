import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidSearchComponent } from './lipid-search.component';

describe('LipidSearchComponent', () => {
  let component: LipidSearchComponent;
  let fixture: ComponentFixture<LipidSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LipidSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
