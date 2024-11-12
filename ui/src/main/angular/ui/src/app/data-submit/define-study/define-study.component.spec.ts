/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DefineStudyComponent } from './define-study.component';

describe('DefineStudyComponent', () => {
  let component: DefineStudyComponent;
  let fixture: ComponentFixture<DefineStudyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DefineStudyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DefineStudyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
