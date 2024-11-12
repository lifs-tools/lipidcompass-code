import { Component, Input, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { TranslateService } from "@ngx-translate/core";
import { SelectItem, SortMeta } from "primeng/api";
import {
  EntityModelLipid,
  Lipid,
  LipidCompassQuery,
  LipidControllerService,
  PagedModelEntityModelLipid,
  PageMetadata,
} from "../../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import { paging } from "../../../environments/environment";

// class Filter {
//   facetCategory: string;
//   facetValue: string;
// }

@Component({
  selector: "app-lipid-search",
  templateUrl: "./lipid-search.component.html",
  styleUrls: ["./lipid-search.component.css"],
})
export class LipidSearchComponent implements OnInit {
  pageable: Pageable = { ...paging.defaultPageable };
  pageMetadata: PageMetadata = null;

  level: SelectItem[];

  response: PagedModelEntityModelLipid;
  results: EntityModelLipid[];
  // page: PageMetadata = JSON.parse(JSON.stringify(this.DEFAULT_PAGE));
  // pageAndSort: PageAndSort = <PageAndSort>{ number: 0, size: 20 };

  lipidCompassQuery: LipidCompassQuery = <LipidCompassQuery>{};

  facets: any[] = [
    // { "field":"lipidSpecies", "values":[ {"name": "asds", "value": 23}, {"name": "asdasd", "value":12312}]},
    // { "field":"studyVariables", "values":[ {"name": "asds", "value": 1251}, {"name": "asdukjh adasjkh asd", "value":61616136}]}
  ];

  autoCompleteResults: string[];
  rowsPerPageOptions: number[] = [25, 50, 100, 200];

  matchingModes: string[] = ["PREFIX", "EXACT"]; //["EXACT", "FUZZY", "PREFIX", "SUFFIX"];

  placeholder: "Select Hierarchy Level";

  @Input()
  limit = 100;

  @Input()
  loading: boolean = false;

  @Input()
  disabled: boolean = false;

  @Input()
  selectedLevel: LipidCompassQuery.LipidLevelEnum = "UNDEFINED_LEVEL";

  @Input()
  queryString: string[] = [];
  lipidNameAutocompleteResults: string[] = [];

  @Input()
  matchMode: LipidCompassQuery.MatchModeEnum = "PREFIX";

  @Input()
  sumFormulaQuery: string = "";

  @Input()
  normalizeName: boolean = true;

  @Input()
  filterByMass: boolean = false;

  massFilter: any = { 
    "minMass": 0.0,
    "maxMass": 2000.0
  };

  addDefaultFacets: boolean = true;

  query: LipidCompassQuery = <LipidCompassQuery>{
    lipidLevel: LipidCompassQuery.LipidLevelEnum.SPECIES,
    matchMode: this.matchMode,
    mzTabResults: [],
    names: this.queryString,
    normalizeName: true,
    selectedFacets: [],
    facets: [],
    unit: null,
    minMass: this.massFilter.minMass,
    maxMass: this.massFilter.maxMass,
    sumFormula: null,
    minValue: 0.0,
    maxValue: Number.MAX_VALUE,
  };

  constructor(
    translate: TranslateService,
    private lipidQuantitySearchService: LipidControllerService
  ) {
    translate.setDefaultLang("en");
    translate.use("en");
    this.pageable.sort = [
      "e.lipidCategory,asc",
      "e.lipidClass,asc",
      "e.lipidShorthandName,asc"
    ];
  }

  ngOnInit() {
    this.level = [];
    // this.level.push({label: 'Select Hierarchy Level', value: null});
    this.level.push({
      label: "Category",
      value: LipidCompassQuery.LipidLevelEnum.CATEGORY,
    });
    this.level.push({
      label: "Class",
      value: LipidCompassQuery.LipidLevelEnum.CLASS,
    });
    this.level.push({
      label: "Species",
      value: LipidCompassQuery.LipidLevelEnum.SPECIES,
    });
    this.level.push({
      label: "Molecular Species",
      value: LipidCompassQuery.LipidLevelEnum.MOLECULAR_SPECIES,
    });
    this.level.push({
      label: "SN Position",
      value: LipidCompassQuery.LipidLevelEnum.SN_POSITION,
    });
    this.level.push({
      label: "Structure Defined",
      value: LipidCompassQuery.LipidLevelEnum.STRUCTURE_DEFINED,
    });
    this.level.push({
      label: "Complete Structure",
      value: LipidCompassQuery.LipidLevelEnum.COMPLETE_STRUCTURE,
    });
    this.level.push({
      label: "Full Structure",
      value: LipidCompassQuery.LipidLevelEnum.FULL_STRUCTURE,
    });
    this.level.push({
      label: "Any",
      value: LipidCompassQuery.LipidLevelEnum.UNDEFINED_LEVEL,
    });
    this.pageable.sort = <Array<string>>[
      "e.lipidCategory,asc",
      "e.lipidClass,asc",
      "e.lipidShorthandName,asc"
    ];
    this.pageMetadata = <PageMetadata>{
      number: this.pageable.page,
      size: this.pageable.size,
      totalElements: 0,
      totalPages: 0,
    };
    this.loadLipid(this.pageable);
  }

  // autocompleteSearch(event: { query: any; }) {
  //   this.lipidSearchService.searchLipid(
  //     this.query,
  //     this.selectedLevel,
  //     this.matchingMode,
  //     this.normalizeName,
  //     this.addDefaultFacets,
  //     this.DEFAULT_PAGE
  //   ).then(
  //     response => {
  //       console.debug("Response: " + JSON.stringify(response));
  //       this.autoCompleteResults = response.page.data.map((res) => { return res.name; });
  //     }
  //   ).catch(errorResponse => {

  //   });
  // }

  resetState() {
    this.response = null;
    this.results = [];
    this.disabled = false;
    this.loading = false;
    this.massFilter.minMass = 0.0;
    this.massFilter.maxMass = 2000.0;
    this.query.lipidLevel = "SPECIES";
    this.query.matchMode = "PREFIX";
    this.query.names = [];
    this.query.minMass = null;
    this.query.maxMass = null;
    this.query.sumFormula = null;
    this.selectedLevel = "SPECIES";
    this.matchMode = "PREFIX";
    this.autoCompleteResults = [];
    this.queryString = [];
    this.filterByMass = false;
    this.pageable = { ...paging.defaultPageable };
    this.pageable.sort = [
      "e.lipidCategory,asc",
      "e.lipidClass,asc",
      "e.lipidShorthandName,asc"
    ];
    this.pageMetadata = <PageMetadata>{
      number: this.pageable.page,
      size: this.pageable.size,
      totalElements: 0,
      totalPages: 0,
    };
    this.runQuery();
  }

  onEnter(f: NgForm) {
    console.debug("Received onEnter event!");
    this.runQuery();
  }

  onSubmit(f: NgForm) {
    this.runQuery();
  }

  onAdd(event: any) {
    // console.debug("Adding name: " + JSON.stringify(event.value));
    if (this.queryString) {
      this.queryString.push(event.value.normalizedShorthandName);
    } else {
      this.queryString = [event.value.normalizedShorthandName];
    }
    // console.debug("Current names:: " + JSON.stringify(this.query.names));
    console.debug("Selected names: " + JSON.stringify(event));
    this.query.names = this.queryString;
  }

  handleNameRemoved(event: any) {
    var name = event.value;
    var idx = this.queryString.indexOf(name, 0);
    if (idx > -1) {
      this.queryString.splice(idx, 1);
    }
    console.debug("Selected names: " + JSON.stringify(event));
    this.query.names = this.queryString;
  }

  customSort(event: any) {
    console.debug("SortEvent: " + JSON.stringify(event));
  }

  runQuery(): void {
    this.disabled = true;
    this.loading = true;
    console.debug(
      "Query: " +
        JSON.stringify(this.query) +
        " Level: " +
        this.query.lipidLevel +
        " Matching Mode: " +
        this.query.matchMode +
        " Normalize Name: " +
        this.query.normalizeName
    );
    this.loadLipid(this.pageable);
  }

  loadLipid(pageable: Pageable) {
    // var sortOrder = <Array<string>>["e.lipidCategory","e.lipidClass"];
    console.debug("Current queryString: " + JSON.stringify(this.queryString));
    this.query.names = this.queryString;
    this.query.lipidLevel = this.selectedLevel;
    if (this.filterByMass) {
      console.log("Filtering by mass: " + JSON.stringify(this.massFilter));
      this.query.minMass = this.massFilter.minMass;
      this.query.maxMass = this.massFilter.maxMass;
    } else {
      console.log("Not filtering by mass");
      this.query.minMass = null;
      this.query.maxMass = null;
    }
    this.lipidQuantitySearchService
      .findByLipidQuery(this.query, pageable.page, pageable.size, pageable.sort)
      .subscribe(
        (response) => {
          this.response = response;
          this.results = this.response?._embedded?.lipids;
          this.pageable.page = this.response.page.number;
          this.pageable.size = this.response.page.size;
          this.pageMetadata = this.response.page;
          // this.page = this.response.page;
          // this.page.totalElements = this.response.page.totalElements;
          // this.page.size = this.response.page.size;
          // this.page.number = this.response.page.number;
          this.loading = false;
          this.disabled = false;
        },
        (error) => {
          this.loading = false;
          this.disabled = false;
        }
      );
  }

  sortPage(event: any) {
    console.debug("SortEvent: " + JSON.stringify(event));
    if (event?.multisortmeta) {
      var sorts: SortMeta[] = event.multisortmeta;
      var sortColumns: string[] = sorts.map((value) => {
        return "e." + value.field + "," + (value.order === 1 ? "asc" : "desc");
      });
      this.pageable.sort = sortColumns;
      this.loadLipid(this.pageable);
    }
  }

  onPageChange(event: any) {
    console.debug("onPageChange event: " + JSON.stringify(event));
    this.loading = true;
    let pageIndex = event.first / event.rows;
    console.debug("Page event for page: " + (pageIndex + 1));
    console.debug("Event size before load: " + event.rows);
    console.debug(
      "Loading page " +
        (pageIndex + 1) +
        " with " +
        this.pageable.size +
        " elements"
    );
    this.pageable.page = pageIndex;
    this.pageable.size = event.rows;
    this.loadLipid(this.pageable);
    this.loading = false;
  }

  levelInputOnChange(event: any) {
    console.debug("Selected lipid level: " + JSON.stringify(event.value));
    this.query.lipidLevel = this.selectedLevel;
  }

  matchingModesOnChange(event: any) {
    console.debug("Selected matching mode: " + JSON.stringify(event));
    this.query.matchMode = this.matchMode;
  }

  lipidNameInputOnChange(event: any) {
    console.debug("Selected names: " + JSON.stringify(event));
    console.debug("Current names: " + JSON.stringify(this.query.names));
    console.debug("Current queryString: " + JSON.stringify(this.queryString));
    this.query.names = event.query;
  }

  lipidNameAutocomplete(event: any) {
    console.debug("Selected names: " + JSON.stringify(event));
    this.loading = true;
    // this.lipidNameAutocompleteResults = [];
    const autoCompleteQuery: LipidCompassQuery = <LipidCompassQuery>{
      lipidLevel: this.selectedLevel,
      matchMode: this.matchMode,
      mzTabResults: [],
      names: [event.query],
      normalizeName: this.normalizeName,
      selectedFacets: [],
      facets: [],
      unit: null,
      minMass: this.massFilter.minMass,
      maxMass: this.massFilter.maxMass,
      sumFormula: this.query.sumFormula,
      minValue: 0.0,
      maxValue: Number.MAX_VALUE,
      limit: 50,
    };
    this.lipidQuantitySearchService
      .findByLipidQuery(autoCompleteQuery, 0, 50, ["e.lipidCategory,asc","e.lipidClass,asc","e.lipidShorthandName,asc"])
      .subscribe(
        (response) => {
          this.lipidNameAutocompleteResults = response?._embedded?.lipids?.map(
            (entityLipid) => {
              return (entityLipid as Lipid).normalizedShorthandName;
            }
          );
          // this.page = this.response.page;
          // this.page.totalElements = this.response.page.totalElements;
          // this.page.size = this.response.page.size;
          // this.page.number = this.response.page.number;
          this.loading = false;
          this.disabled = false;
        },
        (error) => {
          this.loading = false;
          this.disabled = false;
        }
      );
  }
}
