import { Component, Input } from '@angular/core';
import { StatsCard } from '../../_models/statscard';

@Component({
  selector: 'app-stats-card-grid',
  standalone: false,
  // imports: [],
  templateUrl: './stats-card-grid.component.html',
  styleUrl: './stats-card-grid.component.css'
})
export class StatsCardGridComponent {
  @Input() 
  cards: StatsCard[];

  constructor() {}
}
