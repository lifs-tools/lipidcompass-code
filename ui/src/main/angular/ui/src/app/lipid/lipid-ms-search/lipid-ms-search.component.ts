import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { SelectItem } from 'primeng/api';
import {
  AdductIon, LipidsearchService,
  Ms1ResultItem
} from '../../_services/lipidsearch.service';


@Component({
  selector: 'app-lipid-ms-search',
  templateUrl: './lipid-ms-search.component.html',
  styleUrls: ['./lipid-ms-search.component.css']
})
export class LipidMsSearchComponent implements OnInit {

  adductIons: SelectItem[] = [];
  response: Ms1ResultItem[] = [];

  @Input()
  msQueryAdductIons: SelectItem[] = [];

  @Input()
  msQueryInput: string;

  constructor(translate: TranslateService, private lipidSearchService: LipidsearchService) {

  }

  ngOnInit() {
    //this.lipidSearchService.adductIons().then(adductIons => this.adductIons = this.toSelectItemArray(adductIons.list));
  }

  onSubmit(msSearchForm: NgForm) {
    /*
    Ms1QueryParams params = new Ms1QueryParams();
    this.lipidSearchService.searchMs(this.query, this.selectedLevel).then(
      response => {this.response = response.list; }
    );
     */
  }

  private toAdductIonArray(msQueryAdductIons: Array<SelectItem>): Array<AdductIon> {
    const adductIons = Array<AdductIon>();
    msQueryAdductIons.forEach(element => {
      adductIons.push(<AdductIon> element.value);
    });
    return adductIons;
  }

  private toSelectItemArray(adductIons: Array<AdductIon>): Array<SelectItem> {
    const msQueryAdductIons = Array<SelectItem>();
    adductIons.forEach(element => {
      msQueryAdductIons.push(<SelectItem>{ 'label': element.name, 'value': element});
    });
    return msQueryAdductIons;
  }

}
