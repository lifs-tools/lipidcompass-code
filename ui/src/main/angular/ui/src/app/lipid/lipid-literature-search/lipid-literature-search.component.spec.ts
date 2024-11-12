import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidLiteratureSearchComponent } from './lipid-literature-search.component';

describe('LipidLiteratureSearchComponent', () => {
  let component: LipidLiteratureSearchComponent;
  let fixture: ComponentFixture<LipidLiteratureSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LipidLiteratureSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidLiteratureSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
