import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MztabComponent } from './mztab.component';

describe('MztabComponent', () => {
  let component: MztabComponent;
  let fixture: ComponentFixture<MztabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MztabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MztabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
