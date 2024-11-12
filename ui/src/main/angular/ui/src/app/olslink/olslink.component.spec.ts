import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OlslinkComponent } from './olslink.component';

describe('OlslinkComponent', () => {
  let component: OlslinkComponent;
  let fixture: ComponentFixture<OlslinkComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OlslinkComponent]
    });
    fixture = TestBed.createComponent(OlslinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
