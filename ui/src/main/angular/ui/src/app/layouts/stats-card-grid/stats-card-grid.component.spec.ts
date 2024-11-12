import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsCardGridComponent } from './stats-card-grid.component';

describe('StatsCardGridComponent', () => {
  let component: StatsCardGridComponent;
  let fixture: ComponentFixture<StatsCardGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatsCardGridComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StatsCardGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
