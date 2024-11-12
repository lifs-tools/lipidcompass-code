import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LipidTreeComponent } from './lipid-tree.component';

describe('LipidTreeComponent', () => {
  let component: LipidTreeComponent;
  let fixture: ComponentFixture<LipidTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LipidTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LipidTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
