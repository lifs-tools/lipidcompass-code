import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import {ListboxModule} from 'primeng/listbox';
import { Correction } from '../../_models/correction';

@Component({
  selector: 'app-dataset-corrections',
  templateUrl: './dataset-corrections.component.html',
  styleUrls: ['./dataset-corrections.component.css']
})
export class DatasetCorrectionsComponent implements OnInit {

  @Input() corrections: Correction[] = [ 
    new Correction("Cholester(ol/yl) Ester Correction", "Correct Cholesterol and Cholesteryl Esters according to the method described in Höring et al. 2019 (PMID: 30707563)."),
    new Correction("Renormalize to NIST SRM 1950 concentrations", "Renormalize lipid concentrations using the corresponding NIST SRM 1950 concentrations."),
  ];

  @Output() updatedCorrectionSelectionEvent = new EventEmitter<Correction[]>();
  
  selectedCorrections: Correction[] = [];

  constructor() { }

  ngOnInit() {
  }

  updateSelection(event: any) {
    var selection = event.value;
    this.updatedCorrectionSelectionEvent.emit(this.selectedCorrections);
  }

}
