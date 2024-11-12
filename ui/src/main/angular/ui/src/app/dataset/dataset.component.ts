import { Component, Input, OnInit, Query } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import {
  CvParameterFacet,
  CvParameterFacetValue,
  EntityModelCvParameterFacet,
  EntityModelMzTabResult,
  Lipid,
  LipidCompassQuery,
  LipidControllerService,
  MzTabResult,
  MzTabResultControllerService,
  PlottableLipidQuantityControllerService,
} from "../../../modules/lipidcompass-backend-client";
import { paging, queries } from "../../environments/environment";
// import { BaseLipid.LipidLevelEnumEnum } from "../_models/LipidLevelEnum";
import { TranslateService } from "@ngx-translate/core";
import { FilterMatchMode, SelectItem, TreeNode } from "primeng/api";
import { Pageable } from "../../../modules/lipidcompass-backend-client/model/pageable";
interface MzTabLipid {
  name: string;
  level: string;
  taxonomy: string;
}

@Component({
  selector: "app-dataset",
  templateUrl: "./dataset.component.html",
  styleUrls: ["./dataset.component.css"],
})
export class DatasetComponent implements OnInit {
  pageable: Pageable = { ...paging.defaultPageable };

  repositoryLipids: Lipid[] = [];

  lipids: MzTabLipid[] = [
    {
      level: "STRUCTURAL_SUBSPECIES",
      name: "Cer 18:0;2/22:0",
      taxonomy: "CER",
    },
    {
      level: "SPECIES",
      name: "PC 32:0",
      taxonomy: "GP",
    },
    {
      level: "SPECIES",
      name: "SM 32:0",
      taxonomy: "SP",
    },
    {
      level: "SPECIES",
      name: "SM 41:2",
      taxonomy: "SP",
    },
  ];
  selectedPlotLipids: MzTabLipid[] = [
    {
      level: "SPECIES",
      name: "PC 30:0",
      taxonomy: "GP",
    },
    {
      level: "SPECIES",
      name: "PC 30:1",
      taxonomy: "GP",
    },
    {
      level: "SPECIES",
      name: "PC 32:0",
      taxonomy: "GP",
    },
    {
      level: "SPECIES",
      name: "PC 32:1",
      taxonomy: "GP",
    },
    {
      level: "SPECIES",
      name: "PC 32:2",
      taxonomy: "GP",
    },
  ];

  selectedLipids: Lipid[] = [];

  queryJson: string = "";

  studyFactors: Object[] = [];
  quantificationUnits: Object[] = [];

  mzTabResultData: EntityModelMzTabResult[] = [];
  totalRecords: number = 0;
  // organismsPage: Page<any>;

  selectedMzTabResults: Array<MzTabResult> = [];
  selectedMzTabResultsForComparison: Array<MzTabResult> = [];
  selectedMzTabResultsForComparisonSet: Set<MzTabResult> = new Set();
  loading: boolean = true;
  filter: boolean = false;
  checkbox: boolean = true;
  minFilter: number = 8;

  level: SelectItem[];

  @Input()
  selectedLevel: Lipid.LipidLevelEnum = Lipid.LipidLevelEnum.CATEGORY;

  @Input()
  matchingMode: string = "PREFIX";

  @Input()
  normalizeName: boolean = true;

  availableLipids: Array<string> = [];

  cols: Array<any> = [
    { field: "lipidCategory", header: "Lipid Category" },
    { field: "lipidClass", header: "Lipid Class" },
    { field: "normalizedShorthandName", header: "Shorthand Name" },
    { field: "LipidLevelEnum", header: "Level" },
    { field: "chemicalFormula", header: "Sum Formula" },
    { field: "exactMass", header: "Exact Mass" },
  ];

  placeholder: "Select Hierarchy Level";

  matchingModes: Array<any> = [
    {
      label: "Exact",
      value: "EXACT",
    },
    {
      label: "Fuzzy",
      value: "FUZZY",
    },
    {
      label: "Prefix",
      value: "PREFIX",
    },
    {
      label: "Suffix",
      value: "SUFFIX",
    },
  ];

  // x = <any>['day 1', 'day 1', 'day 1', 'day 1', 'day 1', 'day 1',
  //        'day 2', 'day 2', 'day 2', 'day 2', 'day 2', 'day 2'];

  // public graph = {
  public data: Plotly.Data[] = [];

  public layout: Plotly.Layout = {
    autosize: true,
    title: "Lipid Quantity Distribution",
    yaxis: {
      title: "quantity",
      zeroline: false,
    },
    boxmode: "group",
  };
  query: LipidCompassQuery;
  facetSelection: Map<string, CvParameterFacetValue[]> = new Map<
    string,
    CvParameterFacetValue[]
  >();
  private activatedRouteParams: any;
  selectedFacets: Array<CvParameterFacetValue>;

  // };

  constructor(
    private mzTabResultControllerService: MzTabResultControllerService,
    private plottableLipidQuantityControllerService: PlottableLipidQuantityControllerService,
    private lipidControllerService: LipidControllerService,
    private route: ActivatedRoute,
    private router: Router,
    private translateService: TranslateService
  ) {
    this.activatedRouteParams = route.snapshot.params;
    this.query = { ...queries.defaultLipidCompassQuery };
  }

  ngOnInit() {
    var pageable: Pageable = paging.defaultPageable as Pageable;
    this.selectedFacets = [];
    this.facetSelection.clear();
    this.level = [];
    // this.level.push({label: 'Select Hierarchy Level', value: null});
    this.level.push({
      label: "Any",
      value: Lipid.LipidLevelEnum.UNDEFINED_LEVEL,
    });
    this.level.push({
      label: "Category",
      value: Lipid.LipidLevelEnum.CATEGORY,
    });
    this.level.push({ label: "Class", value: Lipid.LipidLevelEnum.CLASS });
    this.level.push({
      label: "Species",
      value: Lipid.LipidLevelEnum.SPECIES,
    });
    this.level.push({
      label: "Molecular Species",
      value: Lipid.LipidLevelEnum.MOLECULAR_SPECIES,
    });
    this.level.push({
      label: "SN Position",
      value: Lipid.LipidLevelEnum.SN_POSITION,
    });
    this.level.push({
      label: "Structure Defined",
      value: Lipid.LipidLevelEnum.STRUCTURE_DEFINED,
    });
    this.level.push({
      label: "Complete Structure",
      value: Lipid.LipidLevelEnum.COMPLETE_STRUCTURE,
    });
    this.level.push({
      label: "Full Structure",
      value: Lipid.LipidLevelEnum.FULL_STRUCTURE,
    });
    if (this.activatedRouteParams.mzTabResultIds) {
      console.debug("Loading mzTabResults by ids provided via router!");
      this.mzTabResultData = [];
      this.activatedRouteParams.mzTabResultIds.forEach(
        (value: string, index: any, array: any) => {
          this.mzTabResultControllerService
            .getById(value)
            .subscribe((response) => {
              console.debug(
                "Received mztabresult: " + JSON.stringify(response)
              );
              this.mzTabResultData.push(response);
              this.totalRecords = 1;
            });
        }
      );
    } else {
      this.mzTabResultControllerService
        .get(pageable.page, pageable.size, pageable.sort)
        .subscribe(
          (mzTabResults) => {
            console.debug(
              "Received mztabresults: " + JSON.stringify(mzTabResults)
            );
            this.mzTabResultData = mzTabResults?._embedded?.mzTabResults;
            console.debug("Setting mzTabResultData: "+JSON.stringify(this.mzTabResultData));
            this.totalRecords = mzTabResults?.page?.totalElements;
            this.loading = false;
          },
          (error) => {
            this.loading = false;
          }
        );
    }
    this.mzTabResultControllerService.getQuery().subscribe(
      (query) => {
        console.debug("Received query: " + JSON.stringify(query));
        this.query = <LipidCompassQuery>query;
        this.selectedLevel = query.lipidLevel;
        this.lipidControllerService
          .findByLipidQuery(query, pageable.page, pageable.size, pageable.sort)
          .subscribe((lipids) => {
            console.debug("Received lipids: " + JSON.stringify(lipids));
            this.availableLipids = [];
            this.repositoryLipids = [];
            lipids?._embedded?.lipids.forEach((value) => {
              var lipid = <Lipid>value;
              this.repositoryLipids.push(lipid);
              this.availableLipids.push(lipid.normalizedShorthandName);
            });
          });
      },
      (error) => {
        this.loading = false;
      }
    );
  }

  levelInputOnChange(event: any) {
    console.debug("Selected lipid level: " + JSON.stringify(event));
    this.query.lipidLevel = event?.value;
    // this.updateQuery(this.query);
  }

  matchingModesOnChange(event: any) {
    console.debug("Selected matching mode: " + JSON.stringify(event));
    this.query.matchMode = event?.value;
    // this.updateQuery(this.query);
  }

  lipidNameInputOnChange(event: any) {
    console.debug("Selected names: " + JSON.stringify(event));
    this.query.names = event?.value;
    // this.updateQuery(this.query);
  }

  handleFacetSelection(referenceType: string, event: any) {
    console.debug(
      "Selected facet: " +
        referenceType +
        " with values: " +
        JSON.stringify(event.value)
    );
    if (this.query) {
      var selectedFacetsForType = this.facetSelection.get(referenceType);
      if (!selectedFacetsForType) {
        selectedFacetsForType = [];
      }
      selectedFacetsForType.push(event.value.pop());
      this.facetSelection.set(referenceType, selectedFacetsForType);
      console.debug("Facet selection: " + JSON.stringify(this.facetSelection));
      const facets: CvParameterFacet[] = [];
      this.selectedFacets = [];
      this.facetSelection.forEach(
        (value: CvParameterFacetValue[], key: string) => {
          if (value.length > 0) {
            facets.push(<CvParameterFacet>{
              referenceType: key,
              facetValues: value,
            });
            value.forEach((facetValue: CvParameterFacetValue) => {
              this.selectedFacets.push(facetValue);
            });
            console.debug("Facets: " + JSON.stringify(facets));
          }
        }
      );

      console.debug("Using these facets for query: " + JSON.stringify(facets));
      this.query.selectedFacets = facets;
      console.debug(JSON.stringify(this.query));
      // this.updateQuery(this.query);
    }
  }

  compare(event: any) {
    if (this.selectedMzTabResultsForComparisonSet.size > 1) {
      console.debug(JSON.stringify(this.selectedMzTabResultsForComparison));
      this.router.navigate(["dataset/compare"], {
        state: {
          datasets: Array.from(this.selectedMzTabResultsForComparisonSet),
        },
      });
    }
  }

  addToSelection(event: any) {
    if (this.selectedMzTabResults) {
      console.debug(JSON.stringify(this.selectedMzTabResults));
      this.selectedMzTabResults.forEach((value) => {
        this.selectedMzTabResultsForComparisonSet.add(value);
      });
      this.selectedMzTabResultsForComparison = Array.from(
        this.selectedMzTabResultsForComparisonSet
      );
    }
  }

  handleSelectedMzTabResultRemoved(event: any) {
    var mzTabResult = event.value;
    this.selectedMzTabResultsForComparisonSet.delete(mzTabResult);
    this.selectedMzTabResultsForComparison = Array.from(
      this.selectedMzTabResultsForComparisonSet
    );
  }

  handleSelectedFacetRemoved(event: any) {
    console.debug("handleSelectedFacetRemoved: " + JSON.stringify(event));
    var facetValue: CvParameterFacetValue = event.value;
    var otherFacets: CvParameterFacetValue[] = [];
    this.selectedFacets = this.selectedFacets.filter(
      (fv) => fv.accession != facetValue.accession
    );
    var referenceType = undefined;
    this.facetSelection.forEach(
      (value: CvParameterFacetValue[], key: string) => {
        value.forEach((lookupFacetValue: CvParameterFacetValue) => {
          if (lookupFacetValue.accession === facetValue.accession) {
            referenceType = key;
          }
        });
      }
    );
    if (referenceType !== undefined) {
      this.facetSelection.set(
        referenceType,
        this.facetSelection
          .get(referenceType)
          .filter((fv) => fv.accession != facetValue.accession)
      );
    }
    //   this.handleFacetSelection(referenceType, { value: otherFacets });
    // }
  }

  resetState() {
    this.ngOnInit();
  }

  updateQuery(query: LipidCompassQuery) {
    this.loading = true;
    this.queryJson = JSON.stringify(this.query);
    this.mzTabResultControllerService
      .postQuery(
        query,
        this.pageable.page,
        this.pageable.size,
        this.pageable.sort
      )
      .subscribe(
        (response) => {
          console.debug("Received mztabresults: " + JSON.stringify(response));
          this.mzTabResultData = response?._embedded?.mzTabResults;
          console.debug("Setting mzTabResultData: "+JSON.stringify(this.mzTabResultData));
          this.totalRecords = response?.page?.totalElements;
          // FIXME update the query within our backend
          this.mzTabResultControllerService
            .getFacets(this.query.selectedFacets)
            .subscribe((response) => {
              this.query.facets = <CvParameterFacet[]>(
                response?._embedded.cvParameterFacets
              );
              this.lipidControllerService
                .findByLipidQuery(
                  this.query,
                  this.pageable.page,
                  this.pageable.size,
                  this.pageable.sort
                )
                .subscribe((lipids) => {
                  console.debug("Received lipids: " + JSON.stringify(lipids));
                  this.availableLipids = [];
                  this.repositoryLipids = [];
                  lipids?._embedded?.lipids.forEach((value) => {
                    var lipid = <Lipid>value;
                    this.repositoryLipids.push(lipid);
                    this.availableLipids.push(
                      (<Lipid>value).normalizedShorthandName
                    );
                  });
                });
            });
          this.loading = false;
        },
        (error) => {
          this.loading = false;
        }
      );
  }

  comparisonDisabled(): boolean {
    if (this.selectedMzTabResultsForComparisonSet) {
      return this.selectedMzTabResultsForComparisonSet.size < 2;
    }
    return false;
  }

  onChange(event: any) {
    if (!(event.keyCode == 8 || event.keyCode == 46)) {
      return false;
    }
  }
}
