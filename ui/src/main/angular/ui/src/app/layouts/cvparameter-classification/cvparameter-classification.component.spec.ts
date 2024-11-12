/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CvparameterClassificationComponent } from './cvparameter-classification.component';

describe('CvparameterClassificationComponent', () => {
  let component: CvparameterClassificationComponent;
  let fixture: ComponentFixture<CvparameterClassificationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CvparameterClassificationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CvparameterClassificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
