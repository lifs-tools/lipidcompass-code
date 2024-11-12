import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MoleculeViewComponent } from './molecule-view.component';

describe('MoleculeViewComponent', () => {
  let component: MoleculeViewComponent;
  let fixture: ComponentFixture<MoleculeViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoleculeViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoleculeViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
