/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AdminControlledVocabularyComponent } from './admin-controlledvocabularies.component';

describe('AdminControlledVocabularyComponent', () => {
  let component: AdminControlledVocabularyComponent;
  let fixture: ComponentFixture<AdminControlledVocabularyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminControlledVocabularyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminControlledVocabularyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
