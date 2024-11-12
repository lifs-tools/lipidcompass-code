import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Filter } from '../../_models/filter';

@Component({
  selector: 'app-dataset-filters',
  templateUrl: './dataset-filters.component.html',
  styleUrl: './dataset-filters.component.css'
})
export class DatasetFiltersComponent {

  @Input() filters: Filter[] = [ 
    // new Filter("Shared Lipids", "Select lipids that are shared among all datasets."),
    // new Filter("Dataset Unique Lipids", "Select lipids that are unique to each dataset."),
    // new Filter("Known Isobaric Lipids", "Select lipids that are known to be isobaric and may have been misidentified."),
    // new Filter("Fatty Acid Chains", "Select lipids based on their fatty acid chains."),
    // new Filter("Lipid Level", "Select lipids based on their lipid shorthand level."),
  ];

  @Output() updatedFilterSelectionEvent = new EventEmitter<Filter[]>();
  
  selectedFilters: Filter[] = [];

}
