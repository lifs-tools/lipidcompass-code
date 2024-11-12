import { Component } from '@angular/core';
import { EntityModelLipid, EntityModelLipidSvg, LipidControllerService } from '../../../../modules/lipidcompass-backend-client';
import { Observable } from 'rxjs';
import { LipidSvg } from '../../_models/lipid';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lipid-of-the-day',
  standalone: false,
  // imports: [],
  templateUrl: './lipid-of-the-day.component.html',
  styleUrl: './lipid-of-the-day.component.css'
})
export class LipidOfTheDayComponent {
  lipidOfTheDay: EntityModelLipid;
  lipidOfTheDaySvg: Observable<EntityModelLipidSvg>;
  lipidOfTheDayTitle: string = "Random Lipid";

  constructor(
    private router: Router,
    private lipidService: LipidControllerService
  ) {
    this.lipidService.getRandom().subscribe((lipid) => {
      this.lipidOfTheDay = lipid;
      this.lipidOfTheDayTitle = lipid.normalizedShorthandName;
      this.lipidOfTheDaySvg = this.lipidService.getSmilesSvg(lipid.id);
    });
  }

  goToLipidOfTheDay(event: any) {
    if (this.lipidOfTheDay) {
      this.router.navigate(['/lipid', this.lipidOfTheDay.id]);
      //this.lipidOfTheDay.id
      //lipidOfTheDay['_links']['self']";
    }
  }

}
