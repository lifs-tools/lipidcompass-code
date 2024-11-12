import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import { LazyLoadEvent, SelectItem } from "primeng/api";
import { Observable } from "rxjs";
import { LipidDocumentEntityControllerService, LipidDocumentSearchControllerService, LipidQuantityDocumentEntityControllerService, LipidQuantityDocumentSearchControllerService, MzTabResultDocumentEntityControllerService, MzTabResultDocumentSearchControllerService, StudyDocumentEntityControllerService, StudyDocumentSearchControllerService } from "../../../../modules/lipidcompass-search-client";
import { Page, PageAndSort } from "../../_models/page";
import {
  LipidResultItem,
  LipidsearchService,
  SearchResponse,
} from "../../_services/lipidsearch.service";
import { UserService } from "../../_services/user.service";

@Component({
  selector: "app-main-menu-search",
  templateUrl: "./main-menu-search.component.html",
  styleUrls: ["./main-menu-search.component.scss"],
})
export class MainMenuSearchComponent implements OnInit {
  private DEFAULT_PAGE: Page<LipidResultItem> = <Page<LipidResultItem>>{
    data: [],
    number: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0,
  };

  level: SelectItem[];

  response: SearchResponse<LipidResultItem>;
  results: LipidResultItem[];
  page: Page<LipidResultItem> = JSON.parse(JSON.stringify(this.DEFAULT_PAGE));
  pageAndSort: PageAndSort = <PageAndSort>{ number: 0, size: 20 };

  autoCompleteResults: LipidResultItem[];
  rowsPerPageOptions: Number[] = [10, 20, 50, 100, 200];

  placeholder: "Select Hierarchy Level";

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

  constructor(
    translate: TranslateService,
    
    public userService: UserService,
    private router: Router
  ) {
    translate.setDefaultLang("en");
    translate.use("en");
  }

  ngOnInit() {
    this.level = [];
    // this.level.push({label: 'Select Hierarchy Level', value: null});
    this.level.push({ label: "Any", value: "UNDEFINED" });
    this.level.push({ label: "Category", value: "CATEGORY" });
    this.level.push({ label: "Class", value: "CLASS" });
    this.level.push({ label: "Species", value: "SPECIES" });
    this.level.push({
      label: "Mol. Subspecies",
      value: "MOLECULAR_SUBSPECIES",
    });
    this.level.push({
      label: "Struct. Subpecies",
      value: "STRUCTURAL_SUBSPECIES",
    });
    this.level.push({
      label: "Isomer. Subspecies",
      value: "ISOMERIC_SUBSPECIES",
    });
  }

  autocompleteSearch(event: { query: any }) {
    // this.lipidSearchService
    //   .searchLipid(
    //     this.query,
    //     "UNDEFINED",
    //     this.matchingMode,
    //     true,
    //     false,
    //     this.DEFAULT_PAGE
    //   )
    //   .then((response) => {
    //     console.debug("Response: " + JSON.stringify(response));
    //     this.autoCompleteResults = response.page.data;
    //   })
    //   .catch((errorResponse) => {});
  }

  handleSelect(event: any) {
    //   //TODO add handling of other types
    console.debug("Received handleSelect on value: " + JSON.stringify(event));
    this.results = [event];
    this.handleSelection();
    // this.router.navigate(['lipid', result.itemId]);
    //   // [routerLink]="['/lipid', result.itemId]"
  }

  handleUnSelect(event: any) {
    console.debug("Received handleUnSelect on value: " + JSON.stringify(event));
    //   //TODO add handling of other types
    this.results = [];
    // this.router.navigate(['lipid', result.itemId]);
    //   // [routerLink]="['/lipid', result.itemId]"
  }

  resetState() {
    this.response = null;
    this.results = [];
    this.autoCompleteResults = [];
    this.page = this.DEFAULT_PAGE;
    this.pageAndSort = <PageAndSort>{ number: 0, size: 20 };
    this.disabled = false;
    this.loading = false;
  }

  onKeyUp(event: any) {
    console.debug("Received onKeyUp event");
    if (event.keyCode === 13) {
      console.debug("onKeyUp for ENTER");
      this.handleSelection();
    }
  }

  onSubmit(event: any) {
    console.debug("Received onSubmit");
    event.preventDefault();
    this.handleSelection();
  }

  handleSelection() {
    if (this.results && this.results.length == 1) {
      this.disabled = false;
      this.loading = false;
      console.debug("State of results: " + JSON.stringify(this.results));
      this.router.navigate(["lipid", this.results[0].itemId]);
    } else {
      this.resetState();
    }
  }

  loadLipid(pageAndSort: PageAndSort) {
    // this.lipidSearchService
    //   .searchLipid(
    //     this.query,
    //     this.selectedLevel,
    //     this.matchingMode,
    //     this.normalizeName,
    //     false,
    //     pageAndSort
    //   )
    //   .then((response) => {
    //     this.response = response;
    //     this.results = this.response.page.data;
    //     this.page = this.response.page;
    //     this.page.totalElements = this.response.page.totalElements;
    //     this.page.size = this.response.page.size;
    //     this.loading = false;
    //     this.disabled = false;
    //   })
    //   .catch((errorResponse) => {
    //     this.loading = false;
    //     this.disabled = false;
    //   });
  }

  loadQueryPage(event: LazyLoadEvent) {
    this.loading = true;
    // var divisor = this.pageAndSort[0];
    var totalElements = this.page.totalElements;
    var number = 0;
    if (totalElements > 0) {
      number = Math.floor(event.first / this.page.size);
    }
    var pageAndSort = <PageAndSort>{
      number: number,
      size: event.rows,
    };
    console.debug("Event size before load: " + event.rows);
    console.debug(
      "Loading page " +
        pageAndSort.number +
        " with " +
        pageAndSort.size +
        " elements"
    );
    this.loadLipid(pageAndSort);
    this.loading = false;
  }
}
