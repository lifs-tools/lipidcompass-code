import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { SelectItem } from 'primeng/api';
import { Page, PageAndSort } from '../../_models/page';
import { LipidQuantityResultItem, LipidsearchService, SearchResponse } from '../../_services/lipidsearch.service';
import { TableLazyLoadEvent } from 'primeng/table';

@Component({
  selector: 'app-lipid-quantity-search',
  templateUrl: './lipid-quantity-search.component.html',
  styleUrls: ['./lipid-quantity-search.component.css']
})
export class LipidQuantitySearchComponent implements OnInit {

  private DEFAULT_PAGE: Page<LipidQuantityResultItem> = <Page<LipidQuantityResultItem>>{
    data: [],
    number: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  };

  level: SelectItem[];

  response: SearchResponse<LipidQuantityResultItem>;
  results: LipidQuantityResultItem[];
  page: Page<LipidQuantityResultItem> = JSON.parse(JSON.stringify(this.DEFAULT_PAGE));
  pageAndSort: PageAndSort = <PageAndSort>{ number: 0, size: 20 };

  autoCompleteResults: String[];
  rowsPerPageOptions: Number[] = [10, 20, 50, 100, 200];

  // testResponse: any = {
  //   "_embedded": {
  //     "lipidQuantityDocuments": [
  //       {
  //         "collection": "lipidQuantity",
  //         "transactionUuid": "solr-139",
  //         "createdBy": "nilshoffmann",
  //         "updatedBy": "nilshoffmann",
  //         "dateCreated": "2021-08-24T15:34:59.985+00:00",
  //         "dateLastModified": "2021-08-24T15:35:00.347+00:00",
  //         "revision": "_c1hK4fq--Y",
  //         "visibility": "PUBLIC",
  //         "owners": [
  //           "root",
  //           "nilshoffmann"
  //         ],
  //         "score": 1,
  //         "normalizedName": [
  //           "TAG 18:1-18:1-22:6"
  //         ],
  //         "lipidLevel": [
  //           "MOLECULAR_SUBSPECIES"
  //         ],
  //         "lipidCategory": [
  //           "GL"
  //         ],
  //         "lipidClass": [
  //           "TAG"
  //         ],
  //         "lipidSpecies": [
  //           "TAG 58:8"
  //         ],
  //         "lipidMolecularSubSpecies": [
  //           "TAG 18:1-18:1-22:6"
  //         ],
  //         "lipidStructuralSubSpecies": null,
  //         "lipidIsomericSubSpecies": null,
  //         "organismNames": [
  //           "Homo sapiens"
  //         ],
  //         "organismAccessions": [
  //           "NCBITaxon:9606"
  //         ],
  //         "tissueNames": [
  //           "blood plasma"
  //         ],
  //         "tissueAccessions": [
  //           "BTO:0000131"
  //         ],
  //         "cellTypeNames": null,
  //         "cellTypeAccessions": null,
  //         "diseaseNames": null,
  //         "diseaseAccessions": null,
  //         "sampleCustomNames": [
  //           "Performing Laboratory",
  //           "Institutional Review Board",
  //           "Current Smoker",
  //           "Gender",
  //           "Body Mass Index",
  //           "Age"
  //         ],
  //         "sampleCustomAccessions": [
  //           "NCIT:C64206",
  //           "NCIT:C16741",
  //           "NCIT:C67147",
  //           "NCIT:C17357",
  //           "NCIT:C16358",
  //           "NCIT:C25150"
  //         ],
  //         "sampleCustomValues": [
  //           "Baker IDI, Melbourne",
  //           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
  //           "1.0",
  //           "Male",
  //           "34.4",
  //           "45.0"
  //         ],
  //         "assayCustomNames": null,
  //         "assayCustomAccessions": null,
  //         "assayCustomValues": null,
  //         "goMolecularFunctionNames": null,
  //         "goMolecularFunctionAccessions": null,
  //         "goCellComponentNames": null,
  //         "goCellComponentAccessions": null,
  //         "goBiologicalProcessNames": null,
  //         "goBiologicalProcessAccessions": null,
  //         "mzTabResultId": "LCE1-1",
  //         "smlId": 279,
  //         "assayId": 199,
  //         "studyVariableId": 3,
  //         "assayQuantity": 2701.2588,
  //         "quantificationUnitAccession": "UO:0000072",
  //         "quantificationUnitName": "picomolal",
  //         "identificationReliability": "3",
  //         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
  //         "bestIdentificationConfidenceMeasureName": "fragmentation score",
  //         "bestIdConfidenceValue": 1,
  //         "studyVariableFactorNativeIds": null,
  //         "studyVariableFactorParameterAccessions": [
  //           "NCIT:C16564"
  //         ],
  //         "studyVariableFactorParameterNames": [
  //           "Ethnic Group"
  //         ],
  //         "studyVariableFactorValues": [
  //           "Malay"
  //         ],
  //         "studyVariableFactorUnits": null,
  //         "studyVariableFactorUnitAccessions": null
  //       },
  //       {
  //         "collection": "lipidQuantity",
  //         "transactionUuid": "solr-139",
  //         "createdBy": "nilshoffmann",
  //         "updatedBy": "nilshoffmann",
  //         "dateCreated": "2021-08-24T15:34:59.987+00:00",
  //         "dateLastModified": "2021-08-24T15:35:00.347+00:00",
  //         "revision": "_c1hK4fq--a",
  //         "visibility": "PUBLIC",
  //         "owners": [
  //           "root",
  //           "nilshoffmann"
  //         ],
  //         "score": 1,
  //         "normalizedName": [
  //           "TAG 18:1-18:1-22:6"
  //         ],
  //         "lipidLevel": [
  //           "MOLECULAR_SUBSPECIES"
  //         ],
  //         "lipidCategory": [
  //           "GL"
  //         ],
  //         "lipidClass": [
  //           "TAG"
  //         ],
  //         "lipidSpecies": [
  //           "TAG 58:8"
  //         ],
  //         "lipidMolecularSubSpecies": [
  //           "TAG 18:1-18:1-22:6"
  //         ],
  //         "lipidStructuralSubSpecies": null,
  //         "lipidIsomericSubSpecies": null,
  //         "organismNames": [
  //           "Homo sapiens"
  //         ],
  //         "organismAccessions": [
  //           "NCBITaxon:9606"
  //         ],
  //         "tissueNames": [
  //           "blood plasma"
  //         ],
  //         "tissueAccessions": [
  //           "BTO:0000131"
  //         ],
  //         "cellTypeNames": null,
  //         "cellTypeAccessions": null,
  //         "diseaseNames": null,
  //         "diseaseAccessions": null,
  //         "sampleCustomNames": [
  //           "Institutional Review Board",
  //           "Performing Laboratory",
  //           "Current Smoker",
  //           "Age",
  //           "Gender",
  //           "Body Mass Index"
  //         ],
  //         "sampleCustomAccessions": [
  //           "NCIT:C16741",
  //           "NCIT:C64206",
  //           "NCIT:C67147",
  //           "NCIT:C25150",
  //           "NCIT:C17357",
  //           "NCIT:C16358"
  //         ],
  //         "sampleCustomValues": [
  //           "National Health Group Institutional Review Board (IRB 10-434), Singapore",
  //           "Baker IDI, Melbourne",
  //           "0.0",
  //           "40.0",
  //           "Male",
  //           "24.84"
  //         ],
  //         "assayCustomNames": null,
  //         "assayCustomAccessions": null,
  //         "assayCustomValues": null,
  //         "goMolecularFunctionNames": null,
  //         "goMolecularFunctionAccessions": null,
  //         "goCellComponentNames": null,
  //         "goCellComponentAccessions": null,
  //         "goBiologicalProcessNames": null,
  //         "goBiologicalProcessAccessions": null,
  //         "mzTabResultId": "LCE1-1",
  //         "smlId": 279,
  //         "assayId": 200,
  //         "studyVariableId": 1,
  //         "assayQuantity": 897.2885,
  //         "quantificationUnitAccession": "UO:0000072",
  //         "quantificationUnitName": "picomolal",
  //         "identificationReliability": "3",
  //         "bestIdentificationConfidenceMeasureAccession": "MS:1002890",
  //         "bestIdentificationConfidenceMeasureName": "fragmentation score",
  //         "bestIdConfidenceValue": 1,
  //         "studyVariableFactorNativeIds": null,
  //         "studyVariableFactorParameterAccessions": [
  //           "NCIT:C16564"
  //         ],
  //         "studyVariableFactorParameterNames": [
  //           "Ethnic Group"
  //         ],
  //         "studyVariableFactorValues": [
  //           "Indian"
  //         ],
  //         "studyVariableFactorUnits": null,
  //         "studyVariableFactorUnitAccessions": null
  //       }
  //     ]
  //   },
  //   "_links": {
  //     "first": {
  //       "href": "http://127.0.0.1:8091/lipidQuantityDocuments/search/findByLipidQuery?page=0&size=2"
  //     },
  //     "self": {
  //       "href": "http://127.0.0.1:8091/lipidQuantityDocuments/search/findByLipidQuery?page=0&size=2"
  //     },
  //     "next": {
  //       "href": "http://127.0.0.1:8091/lipidQuantityDocuments/search/findByLipidQuery?page=1&size=2"
  //     },
  //     "last": {
  //       "href": "http://127.0.0.1:8091/lipidQuantityDocuments/search/findByLipidQuery?page=12923&size=2"
  //     }
  //   },
  //   "facets": [
  //     {
  //       "content": [
  //         {
  //           "valueCount": 25848,
  //           "value": "GL",
  //           "field": {
  //             "name": "lipidCategory"
  //           },
  //           "key": {
  //             "name": "lipidCategory"
  //           }
  //         }
  //       ],
  //       "pageable": {
  //         "sort": {
  //           "sorted": false,
  //           "unsorted": true,
  //           "empty": true
  //         },
  //         "offset": 0,
  //         "pageNumber": 0,
  //         "pageSize": 10,
  //         "paged": true,
  //         "unpaged": false
  //       },
  //       "facetResultPages": [],
  //       "facetQueryResult": {
  //         "content": [],
  //         "pageable": "INSTANCE",
  //         "totalPages": 1,
  //         "totalElements": 0,
  //         "last": true,
  //         "size": 0,
  //         "number": 0,
  //         "sort": {
  //           "sorted": false,
  //           "unsorted": true,
  //           "empty": true
  //         },
  //         "numberOfElements": 0,
  //         "first": true,
  //         "empty": true
  //       },
  //       "highlighted": [],
  //       "maxScore": null,
  //       "fieldStatsResults": {},
  //       "suggestions": [],
  //       "facetFields": [],
  //       "facetPivotFields": [],
  //       "allFacets": [
  //         null
  //       ],
  //       "alternatives": [],
  //       "totalPages": 1,
  //       "totalElements": 1,
  //       "size": 10,
  //       "number": 0,
  //       "sort": {
  //         "sorted": false,
  //         "unsorted": true,
  //         "empty": true
  //       },
  //       "numberOfElements": 1,
  //       "first": true,
  //       "last": true,
  //       "empty": false
  //     },
  //     {
  //       "content": [
  //         {
  //           "valueCount": 2872,
  //           "value": "TAG 48:2",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 2154,
  //           "value": "TAG 49:1",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 2154,
  //           "value": "TAG 50:2",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 2154,
  //           "value": "TAG 50:3",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 2154,
  //           "value": "TAG 51:2",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 1436,
  //           "value": "TAG 48:3",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 1436,
  //           "value": "TAG 50:1",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 1436,
  //           "value": "TAG 52:3",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 1436,
  //           "value": "TAG 52:4",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         },
  //         {
  //           "valueCount": 718,
  //           "value": "TAG 48:1",
  //           "field": {
  //             "name": "lipidSpecies"
  //           },
  //           "key": {
  //             "name": "lipidSpecies"
  //           }
  //         }
  //       ],
  //       "pageable": {
  //         "sort": {
  //           "sorted": false,
  //           "unsorted": true,
  //           "empty": true
  //         },
  //         "offset": 0,
  //         "pageNumber": 0,
  //         "pageSize": 10,
  //         "paged": true,
  //         "unpaged": false
  //       },
  //       "facetResultPages": [],
  //       "facetQueryResult": {
  //         "content": [],
  //         "pageable": "INSTANCE",
  //         "totalPages": 1,
  //         "totalElements": 0,
  //         "last": true,
  //         "size": 0,
  //         "number": 0,
  //         "sort": {
  //           "sorted": false,
  //           "unsorted": true,
  //           "empty": true
  //         },
  //         "numberOfElements": 0,
  //         "first": true,
  //         "empty": true
  //       },
  //       "highlighted": [],
  //       "maxScore": null,
  //       "fieldStatsResults": {},
  //       "suggestions": [],
  //       "facetFields": [],
  //       "facetPivotFields": [],
  //       "allFacets": [
  //         null
  //       ],
  //       "alternatives": [],
  //       "totalPages": 1,
  //       "totalElements": 10,
  //       "size": 10,
  //       "number": 0,
  //       "sort": {
  //         "sorted": false,
  //         "unsorted": true,
  //         "empty": true
  //       },
  //       "numberOfElements": 10,
  //       "first": true,
  //       "last": true,
  //       "empty": false
  //     }
  //   ],
  //   "page": {
  //     "size": 2,
  //     "totalElements": 25848,
  //     "totalPages": 12924,
  //     "number": 0
  //   }
  // };

  matchingModes: String[] = [
    "EXACT",
    "FUZZY",
    "PREFIX",
    "SUFFIX"
  ];

  placeholder: 'Select Hierarchy Level';

  @Input()
  limit = 100;

  @Input()
  loading: boolean = false;

  @Input()
  disabled: boolean = false;

  @Input()
  selectedLevel: string = "UNDEFINED";

  @Input()
  query: string = "";

  @Input()
  matchingMode: string = "PREFIX";

  @Input()
  normalizeName: boolean = false;

  addDefaultFacets: boolean = true;

  constructor(
    translate: TranslateService,
    private search: LipidsearchService) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit() {
    this.level = [];
    // this.level.push({label: 'Select Hierarchy Level', value: null});
    this.level.push({ label: 'Any', value: 'UNDEFINED' });
    this.level.push({ label: 'Category', value: 'CATEGORY' });
    this.level.push({ label: 'Class', value: 'CLASS' });
    this.level.push({ label: 'Species', value: 'SPECIES' });
    this.level.push({ label: 'Mol. Subspecies', value: 'MOLECULAR_SUBSPECIES' });
    this.level.push({ label: 'Struct. Subpecies', value: 'STRUCTURAL_SUBSPECIES' });
    this.level.push({ label: 'Isomer. Subspecies', value: 'ISOMERIC_SUBSPECIES' });
  }

  autocompleteSearch(event: { query: any; }) {
    // this.search.searchLipid(
    //   this.query,
    //   this.selectedLevel,
    //   this.matchingMode,
    //   this.normalizeName,
    //   this.addDefaultFacets,
    //   this.DEFAULT_PAGE
    // ).then(
    //   response => {
    //     console.debug("Response: " + JSON.stringify(response));
    //     this.autoCompleteResults = response.page.data.map((res) => { return res.name; });
    //   }
    // ).catch(errorResponse => {

    // });
  }

  resetState() {
    this.response = null;
    this.results = [];
    this.page = this.DEFAULT_PAGE;
    this.pageAndSort = <PageAndSort>{ number: 0, size: 20 };
    this.disabled = false;
    this.loading = false;
  }

  onEnter(f: NgForm) {
    console.debug("Received onEnter event!");
    this.resetState();
    this.disabled = true;
    this.loading = true;
    console.debug("Query: " + this.query + " Level: " + this.selectedLevel + " Matching Mode: " + this.matchingMode + " Normalize Name: " + this.normalizeName);
    this.loadLipid(this.pageAndSort);
  }

  onSubmit(f: NgForm) {
    this.resetState();
    this.disabled = true;
    this.loading = true;
    console.debug("Query: " + this.query + " Level: " + this.selectedLevel + " Matching Mode: " + this.matchingMode + " Normalize Name: " + this.normalizeName);
    this.loadLipid(this.pageAndSort);
  }

  loadLipid(pageAndSort: PageAndSort) {
    // this.search.findByLipidQuery()

    // this.search.searchLipidQuantity(
    //   this.query,
    //   this.selectedLevel,
    //   this.matchingMode,
    //   this.normalizeName,
    //   this.addDefaultFacets,
    //   pageAndSort
    // ).then(
    //   response => {
    //     this.response = response;
    //     this.results = this.response.page.data;
    //     this.page = this.response.page;
    //     this.page.totalElements = this.response.page.totalElements;
    //     this.page.size = this.response.page.size;
    //     this.loading = false;
    //     this.disabled = false;
    //   }
    // ).catch(errorResponse => {
    //   this.loading = false;
    //   this.disabled = false;
    // });
  }

  loadQueryPage(event: TableLazyLoadEvent) {
    this.loading = true;
    // var divisor = this.pageAndSort[0];
    var totalElements = this.page.totalElements;
    var number = 0;
    if (totalElements > 0) {
      number = Math.floor(event.first / this.page.size);
    }
    var pageAndSort = <PageAndSort>{
      number: number,
      size: event.rows
    };
    console.debug("Event size before load: " + event.rows);
    console.debug("Loading page " + pageAndSort.number + " with " + pageAndSort.size + " elements");
    this.loadLipid(pageAndSort);
    this.loading = false;
  }

}
