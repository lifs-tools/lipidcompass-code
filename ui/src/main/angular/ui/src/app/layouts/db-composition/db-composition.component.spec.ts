/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DbCompositionComponent } from './db-composition.component';

describe('DbCompositionComponent', () => {
  let component: DbCompositionComponent;
  let fixture: ComponentFixture<DbCompositionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DbCompositionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DbCompositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
