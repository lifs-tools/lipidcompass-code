import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidMsSearchComponent } from './lipid-ms-search.component';

describe('LipidMsSearchComponent', () => {
  let component: LipidMsSearchComponent;
  let fixture: ComponentFixture<LipidMsSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LipidMsSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidMsSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
