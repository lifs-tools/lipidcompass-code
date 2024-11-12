/* eslint-disable @typescript-eslint/no-unused-vars */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FacetSearchComponent } from './facet-search.component';

describe('FacetSearchComponent', () => {
  let component: FacetSearchComponent;
  let fixture: ComponentFixture<FacetSearchComponent>;

  const testResponse: any = require('../../assets/test/spring-data-solr/query-response.json');

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FacetSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FacetSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should parse a spring-data-solr response', () => {
    
  })
});
