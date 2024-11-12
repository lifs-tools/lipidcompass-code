import { DecimalPipe } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import {
  Lipid,
  LipidCompassQuery,
  LipidControllerService,
  PlottableLipidQuantityControllerService,
  StatsControllerService,
  SummarizedLipidStatistics,
} from "../../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import {
  colorScales,
  paging,
  plotlyConfig,
} from "../../../environments/environment";
import { LipidLevel } from "../../_models/lipidLevel";
import { defaultColorScales } from "../../../environments/common";

@Component({
  selector: "app-db-composition",
  templateUrl: "./db-composition.component.html",
  styleUrls: ["./db-composition.component.css"],
})
export class DbCompositionComponent implements OnInit {
  pageable: Pageable = { ...paging.defaultPageable };
  loadingStats: Boolean = false;
  loadingComposition: Boolean = false;

  data: Plotly.Data[];
  lipidStatsData: Plotly.Data[];
  lipidQuantStatsData: Plotly.Data[];
  treeView: any[] = [600, 400];
  showLegend = false;
  showLabels = true;
  explodeSlices = false;
  doughnut = false;
  maxLabelLength = 20;

  public categoryAndClassPlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Lipids by Structural Level",
    // yaxis: {
    //   title: "# in DB",
    //   zeroline: true,
    // },
    margin: {
      l: 0,
      r: 0,
      b: 0,
      // t: 0,
      // pad: 50
    },
    // boxmode: "group",
    branchvalues: "remainder",
    count: "leaves",
    treemapcolorway: colorScales.colorbrewer.RdYlBu[8],
    extendsunburstcolorway: true,
    showlegend: true,
    annotations: [
      {
        text: "No data",
        xref: "paper",
        yref: "paper",
        showarrow: false,
        font: {
          size: 20,
        },
      },
    ],
  };

  public compositionPlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Lipid Counts by Structural Level",
    margin: {
      l: 0,
      r: 0,
      b: 0,
      //   t: 0,
      //   pad: 0
    },
    branchvalues: "remainder",
    count: "leaves",
    treemapcolorway: colorScales.colorbrewer.GnBu[9],
    extendtreemapcolors: true,
    showlegend: false,
    annotations: [
      {
        text: "No data",
        xref: "paper",
        yref: "paper",
        showarrow: false,
        font: {
          size: 20,
        },
      },
    ],
  };

  // public speciesPlotLayout: Plotly.Layout = {
  //   autosize: true,
  //   title: "Lipid Species",
  //   yaxis: {
  //     title: "# in DB",
  //     zeroline: true,
  //   },
  //   "annotations": [
  //     {
  //         "text": "No data",
  //         "xref": "paper",
  //         "yref": "paper",
  //         "showarrow": false,
  //         "font": {
  //             "size": 28
  //         }
  //     }
  //   ]
  // };
  public homePlotlyConfig: Plotly.Config = plotlyConfig;
  public compositionPlotlyConfig: Plotly.Config = plotlyConfig;
  lipidStats: SummarizedLipidStatistics[];

  constructor(
    private plottableLipidQuantityService: PlottableLipidQuantityControllerService,
    private lipidService: LipidControllerService,
    private statsService: StatsControllerService,
    private router: Router,
    private decimalPipe: DecimalPipe
  ) {}

  ngOnInit() {
    this.loadingStats = true;
        this.statsService.stats().subscribe(
            (response) => {
                if (response) {
                    console.debug(JSON.stringify(response?.statistics));
                    const stats = response?.statistics;
                    this.data = [
                        {
                            labels: [
                                "Category",
                                "Class",
                                "Species",
                                "Molecular Species",
                                "SN Position",
                                "Structure Defined",
                                "Full Structure",
                                "Complete Structure",
                            ],
                            values: [
                                stats.categories,
                                stats.classes,
                                stats.species,
                                stats.molecularSpecies,
                                stats.snPosition,
                                stats.structureDefined,
                                stats.fullStructure,
                                stats.completeStructure,
                            ],
                            parents: [
                                "",
                                "Category",
                                "Class",
                                "Species",
                                "Molecular Species",
                                "SN Position",
                                "Structure Defined",
                                "Full Structure",
                            ],
                            marker: { colors: defaultColorScales.lipidShorthandLevels },
                            textinfo: "label+value", //+percent parent+percent entry",
                            type: "treemap",
                            branchvalues: "remainder",
                            count: "branches+leaves",
                            // count: "branches+leaves"
                            // branchvalues: "total"
                        },
                    ];
                    this.loadingStats = false;
                }
            },
            (error) => {
                console.error("Error while loading stats!");
                this.loadingStats = false;
            }
        );
    var label = "Category";
    var lipidLevel = this.mapLabelToLevel(label);
    this.loadingComposition = true;
    this.plottableLipidQuantityService
      .findByLipidLevel(lipidLevel)
      .subscribe((statsList) => {
        if (statsList.length == 0) {
          this.data = [];
          this.lipidStatsData = [];
          this.lipidQuantStatsData = [];
          this.loadingComposition = false;
        } else {
          this.prepareTreeMapData(statsList, label);
        }
      },
      (error) => {
        this.loadingComposition = false; 
      },
      () => {
        this.loadingComposition = false;
      }
      );
  }

  private prepareTreeMapData(
    statsList: SummarizedLipidStatistics[],
    label: string
  ) {
    if (statsList) {
      this.lipidStatsData = [];
      this.lipidQuantStatsData = [];
      var totalCount = 0;
      var totalGroups = 0;
      statsList.forEach((stats) => {
        totalCount += stats.groupCount.reduce(
          (sum, current) => sum + current,
          0
        );
        totalGroups += stats.groupCount.length;
        this.prepareLipidCountTreeMapData(stats, label);
        this.prepareLipidQuantTreeMapData(stats, label);
      });
      if (totalGroups !== 0) {
        this.compositionPlotLayout.title =
          this.decimalPipe.transform(totalCount, "1.0-0", "en-US") +
          " Lipid Counts in " +
          totalGroups +
          " Groups on " +
          label +
          " Level";
        this.compositionPlotLayout.annotations = [];
        this.lipidStats = statsList;
      }
    }
    this.loadingComposition = false;
  }

  prepareLipidCountTreeMapData(
    stats: SummarizedLipidStatistics,
    level: string
  ) {
    if (stats) {
      // console.debug("labels length: "+stats.x.length);
      // console.debug("values length: "+stats.groupCount.length);
      var emptyParents = Array.from(
        { length: stats.x.length },
        (_, i) => level
      );
      if (!this.lipidStatsData) {
        this.lipidStatsData = [];
      }
      // console.debug("parents length: "+emptyParents.length);
      this.lipidStatsData.push({
        labels: stats.x,
        values: stats.groupCount,
        parents: emptyParents,
        name: stats.quantificationUnitParamName,
        // marker: { colors: defaultColorScales.lipidShorthandLevels },
        // textinfo: "label",
        // hoverinfo: "label+value+percent+name",
        // hole: 0.5,
        // type: "pie",
        type: "treemap",
        branchvalues: "total",
        count: "branches+leaves",
        tiling: {
          packing: "squarify",
          squarifyratio: 1.618034,
        },
        sort: true,
      });
      this.categoryAndClassPlotLayout.annotations = [];
    }
  }

  prepareLipidQuantTreeMapData(
    stats: SummarizedLipidStatistics,
    level: string
  ) {
    if (stats) {
      // console.debug("labels length: "+stats.x.length);
      // console.debug("values length: "+stats.groupCount.length);
      var emptyParents = Array.from(
        { length: stats.x.length },
        (_, i) => level
      );
      if (!this.lipidQuantStatsData) {
        this.lipidQuantStatsData = [];
      }
      // console.debug("parents length: "+emptyParents.length);
      this.lipidQuantStatsData.push({
        labels: stats.x,
        values: stats.y,
        parents: emptyParents,
        name: stats.quantificationUnitParamName,
        // marker: { colors: defaultColorScales.lipidShorthandLevels },
        // textinfo: "label",
        // hoverinfo: "label+value+percent+name",
        // hole: 0.5,
        // type: "pie",
        type: "treemap",
        branchvalues: "total",
        count: "branches+leaves",
        tiling: {
          packing: "squarify",
          squarifyratio: 1.618034,
        },
        sort: true,
      });
      this.categoryAndClassPlotLayout.annotations = [];
    }
  }

  onSelect(event: any) {
    var seen: any = [];
    console.debug(
      JSON.stringify(event.points, function(key, val) {
        if (val != null && typeof val == "object") {
          if (seen.indexOf(val) >= 0) {
            return;
          }
          seen.push(val);
        }
        return val;
      })
    );
  }

  onLipidSelect(event: any) {
    var seen: any = [];
    console.debug(
      JSON.stringify(event, function(key, val) {
        if (val != null && typeof val == "object") {
          if (seen.indexOf(val) >= 0) {
            return;
          }
          seen.push(val);
        }
        return val;
      })
    );
    var ctrlDown: boolean = event.event.ctrlKey;
    seen = [];
    console.debug(
      "Received event: " +
        JSON.stringify(event.points, function(key, val) {
          if (val != null && typeof val == "object") {
            if (seen.indexOf(val) >= 0) {
              return;
            }
            seen.push(val);
          }
          return val;
        })
    );
    var pointNumber = event.points[0].pointNumber;
    var label = event.points[0].data.labels[pointNumber];
    var level = this.mapLabelToLevel(event.points[0].data.parents[pointNumber]);
    console.debug(
      "Selected point number " +
        pointNumber +
        " with label: " +
        label +
        " on level: " +
        level
    );
    if (ctrlDown) {
      var query = <LipidCompassQuery>{
        names: [label],
        matchMode: LipidCompassQuery.MatchModeEnum.EXACT,
        lipidLevel: level,
        normalizeName: false,
        facets: [],
        selectedFacets: [],
        unit: "",
        mzTabResults: [],
      };
      this.lipidService
        .findByLipidQuery(
          query,
          this.pageable.page,
          this.pageable.size,
          this.pageable.sort
        )
        .subscribe((response) => {
          console.debug("Response: " + JSON.stringify(response));
          var lipid = <Lipid>response._embedded.lipids[0];
          this.router.navigate(["lipid", lipid.id]);
        });
    }
  }

  onTreemapSelect(event: any) {
    if (event.points) {
      var label = event.points[0].label;
      var lipidLevel = this.mapLabelToLevel(label);
      this.loadingComposition = true;
      this.lipidStatsData = null;
      this.lipidQuantStatsData = null;
      this.plottableLipidQuantityService
        .findByLipidLevel(lipidLevel)
        .subscribe((statsList) => {
          this.prepareTreeMapData(statsList, label);
        });
    }
    console.debug(event.points[0].label);
  }

  notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
    return value !== null && value !== undefined;
  }

  private mapLabelToLevel(label: any) {
    console.debug("Mapping label: " + label);
    var lipidLevel = LipidLevel.CATEGORY;
    switch (label) {
      case "Category":
        lipidLevel = LipidLevel.CATEGORY;
        break;
      case "Class":
        lipidLevel = LipidLevel.CLASS;
        break;
      case "Species":
        lipidLevel = LipidLevel.SPECIES;
        break;
      case "Molecular Species":
        lipidLevel = LipidLevel.MOLECULAR_SPECIES;
        break;
      case "SN Position":
        lipidLevel = LipidLevel.SN_POSITION;
        break;
      case "Structure Defined":
        lipidLevel = LipidLevel.STRUCTURE_DEFINED;
        break;
      case "Full Structure":
        lipidLevel = LipidLevel.FULL_STRUCTURE;
        break;
      case "Complete Structure":
        lipidLevel = LipidLevel.COMPLETE_STRUCTURE;
        break;
    }
    console.debug("Mapped to level: " + lipidLevel);
    return lipidLevel;
  }
}
