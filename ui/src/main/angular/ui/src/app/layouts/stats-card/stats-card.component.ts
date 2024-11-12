import { Component, Input } from '@angular/core';
import { StatsCard } from '../../_models/statscard';

@Component({
  selector: 'app-stats-card',
  standalone: false,
  //imports: [Card, DecimalPipe, NgIf, CommonModule, SharedModule, FontAwesomeModule],
  templateUrl: './stats-card.component.html',
  styleUrl: './stats-card.component.css'
})
export class StatsCardComponent {

  @Input()
  statsCard: StatsCard;
  constructor() { }

  // ngOnInit(): void {
  // }
}
