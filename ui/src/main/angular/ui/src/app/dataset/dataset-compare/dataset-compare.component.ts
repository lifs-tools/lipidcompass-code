import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import { forkJoin, Observable } from "rxjs";
import {
  EntityModelMzTabResult,
  LipidCompassQuery,
  MzTabResultControllerService,
  LipidControllerService,
  LipidSpaceControllerService,
  PagePlottableLipidQuantity,
  PagePlottableLipidSummaryStats,
  PlottableLipidQuantity,
  PlottableLipidQuantityControllerService,
  PlottableLipidSummaryStats,
  LipidSpaceQueryDto,
} from "../../../../modules/lipidcompass-backend-client";
import { SummarizedLipidDataset } from "../../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../../modules/lipidcompass-backend-client/model/pageable";
import {
  colorScales,
  environment,
  lipidCategoryCompositionPlotLayout,
  lipidCategoryConcentrationPlotLayout,
  lipidClassRangesPlotLayout,
  lipidSpeciesCorrelationPlotLayout,
  paging,
  plotlyConfig,
} from "../../../environments/environment";
import { Correction } from "../../_models/correction";
import { LipidLevel } from "../../_models/lipidLevel";
import { PlotService } from "../../_services/plot.service";
import { LipidCompassSearchApiModule } from "../../../../modules/lipidcompass-search-client";

@Component({
  selector: "app-dataset-compare",
  templateUrl: "./dataset-compare.component.html",
  styleUrls: ["./dataset-compare.component.css"],
})
export class DatasetCompareComponent implements OnInit {
  pageable: Pageable = { ...paging.defaultPageable };
  navKey: string;
  datasetIds: string[];
  datasets: EntityModelMzTabResult[];
  selectedDatasets: EntityModelMzTabResult[];
  lipidLevel: LipidLevel = LipidLevel.CLASS;
  categoryLipidLevel: LipidLevel = LipidLevel.CATEGORY;
  chartData: Plotly.Data[];
  categoryCountsChartData: Plotly.Data[];
  categoryConcentrationsChartData: Plotly.Data[];
  boxPlotData: Plotly.Data[];
  boxPlotTableData: any[] = [];
  totalQuantBoxPlotData: Plotly.Data[] = [];
  totalQuantBoxPlotTableData: any[] = [];
  speciesCorrelationChartData: Plotly.Data[];
  lipidSpacePlotData: Plotly.Data[];
  lipidSpaceMatrixPlotData: Plotly.Data[];

  lipidCategoryColors = {
    "GL": "#1f77b4",
    "GP": "#ff7f0e",
    "SP": "#2ca02c",
    "ST": "#d62728",
    "FA": "#9467bd",
    "PR": "#8c564b",
    "SL": "#e377c2",
    "PK": "#bcbd22",
    "Metabolite": "#17becf",
    "Unknown": "#cccccc",
  };

  plotColors = [
    "#1f77b4",
    "#ff7f0e",
    "#2ca02c",
    "#d62728",
    "#9467bd",
    "#8c564b",
    "#e377c2",
    "#bcbd22",
    "#17becf"
  ];

  // defaultCorrections: Corrections[] = [
  //   new Corrections(
  //     "Cholester(ol/yl) Ester Correction",
  //     "Correct Cholesterol and Cholesteryl Esters according to the method described in Höring et al. 2019 (PMID: 30707563)."
  //   ),
  // ];

  sunburstData: Plotly.Data = [
    [
      {
        name: "Dataset I",

        type: "sunburst",

        labels: ["Dataset I", "ST", "Chol", "CholE", "GP", "PC", "PE", "PI"],

        parents: ["", "Dataset I", "ST", "ST", "Dataset I", "GP", "GP", "GP"],

        values: [28, 14, 12, 2, 14, 2, 6, 6],

        outsidetextfont: { size: 20, color: "#377eb8" },

        // leaf: {opacity: 0.4},

        marker: { line: { width: 2 } },

        branchvalues: "total",
      },
    ],
    [
      {
        name: "Dataset II",
        type: "sunburst",

        labels: ["Dataset II", "ST", "Chol", "CholE", "GP", "PC", "PE", "PI"],

        parents: ["", "Dataset II", "ST", "ST", "Dataset II", "GP", "GP", "GP"],

        values: [30, 14, 12, 2, 16, 3, 7, 6],

        outsidetextfont: { size: 20, color: "#377eb8" },

        // leaf: {opacity: 0.4},

        marker: { line: { width: 2 } },

        branchvalues: "total",
      },
    ],
  ];

  sunburstLayout: Plotly.Layout = {
    autosize: true,

    // margin: {l: 0, r: 0, b: 0, t:0},

    sunburstcolorway: colorScales.colorbrewer.Set2[3],
    width: 450,
    height: 450,
    hoverlabel: { namelength: -1 },
  };

  speciesCorrelationReferenceData: PlottableLipidQuantity[];

  public lipidSpeciesCorrelationPlotLayout: Plotly.Layout = lipidSpeciesCorrelationPlotLayout;
  public lipidClassRangesPlotLayout: Plotly.Layout = lipidClassRangesPlotLayout;
  public lipidCategoryCompositionPlotLayout: Plotly.Layout = lipidCategoryCompositionPlotLayout;
  public lipidCategoryConcentrationPlotLayout: Plotly.Layout = lipidCategoryConcentrationPlotLayout;
  public boxPlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Box Plot",
    yaxis: {
      title: "quantity",
      zeroline: false,
    },
    boxmode: "group",
  };

  public totalQuantBoxPlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Lipid Concentrations Distribution",
    yaxis: {
      title: "quantity",
      zeroline: false,
    },
    boxmode: "group",
  };

  public lipidSpacePlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Global Lipid Space",
    yaxis: {
      title: "PC 2",
      zeroline: false,
    },
    xaxis: {
      title: "PC 1",
      zeroline: false,
    },
  };

  public lipidSpaceMatrixPlotLayout: Plotly.Layout = {
    autosize: true,
    title: "Global Lipidome Correlation Matrix",
    yaxis: {
      title: "Sample",
      zeroline: false,
    },
    xaxis: {
      title: "",
      zeroline: false,
    },
  };

  public homePlotlyConfig: Plotly.Config = plotlyConfig;

  constructor(
    private router: Router,
    private mzTabResultService: MzTabResultControllerService,
    private plottableLipidQuantityControllerService: PlottableLipidQuantityControllerService,
    private plotService: PlotService,
    private lipidService: LipidControllerService,
    private lipidSpaceService: LipidSpaceControllerService
  ) {
    this.navKey = "jklhalkhds";
    if (this.router.getCurrentNavigation()) {
      if (this.router.getCurrentNavigation()?.extras?.state) {
        this.datasetIds = this.router.getCurrentNavigation().extras.state.datasets;
        console.debug("Storing dataset ids in localStorage");
        localStorage.setItem(this.navKey, JSON.stringify(this.datasetIds));
        // this.selectedDatasets = this.datasets;
        console.debug(JSON.stringify(this.datasets));
      }
    } else {
      if (localStorage.getItem(this.navKey)) {
        console.debug("Retrieved dataset ids from localStorage");
        this.datasetIds = JSON.parse(localStorage.getItem(this.navKey));
      }
    }
  }

  public getMzTabResults(
    datasetIds: string[]
  ): Observable<EntityModelMzTabResult[]> {
    var mzTabResultService = this.mzTabResultService;
    return forkJoin(
      this.datasetIds.map(function(
        value,
        index
      ): Observable<EntityModelMzTabResult> {
        return mzTabResultService.getById(value);
      })
    );
  }

  ngOnInit() {
    this.getMzTabResults(this.datasetIds).subscribe((data) => {
      this.datasets = data;
      this.chartData = [];
      this.categoryConcentrationsChartData = [];
      this.categoryCountsChartData = [];
      this.boxPlotData = [];
      this.boxPlotTableData = [];
      this.speciesCorrelationChartData = [];
      // this.lipidCategoryCompositionPlotLayout.annotations = [];
      this.lipidCategoryCompositionPlotLayout.grid = {
        rows: 1,
        columns: this.datasetIds.length,
        pattern: "coupled",
      };
      // this.lipidCategoryConcentrationPlotLayout.annotations = [];
      this.lipidCategoryConcentrationPlotLayout.grid = {
        rows: 1,
        columns: this.datasetIds.length,
        pattern: "coupled",
      };
      this.datasetIds.forEach((value, datasetIdIndex, array) => {
        this.plotService
          .loadLipidLevelConcentrationScatterChartPlotData(
            value,
            this.lipidLevel
          )
          .subscribe((data) => {
            // console.debug(
            //   "Received plot data for " + value + ": " + JSON.stringify(data)
            // );
            data.forEach((value, index, array) => {
              this.chartData.push(<Plotly.Data>value);
            });
            // this.chartData.push(<Plotly.Data[]>data);
          });
        this.plotService
          .loadLipidLevelCompositionPieChartPlotData(
            value,
            LipidLevel.CATEGORY,
            lipidCategoryCompositionPlotLayout
          )
          .subscribe((data) => {
            // console.debug(
            //   "Received category composition plot data for " + value + ": " + JSON.stringify(data)
            // );
            data.forEach((value, index, array) => {
              value.domain = {
                row: 0,
                column: datasetIdIndex,
              };
              this.categoryCountsChartData.push(<Plotly.Data>value);
            });
          });
        this.plotService
          .loadLipidLevelConcentrationPieChartPlotData(
            value,
            LipidLevel.CATEGORY,
            lipidCategoryConcentrationPlotLayout
          )
          .subscribe((data) => {
            // console.debug(
            //   "Received category concentration data for " + value + ": " + JSON.stringify(data)
            // );
            data.forEach((value, index, array) => {
              value.domain = {
                row: 0,
                column: datasetIdIndex,
              };

              this.categoryConcentrationsChartData.push(<Plotly.Data>value);
            });
          });
        this.plottableLipidQuantityControllerService
          .findByMzTabResultIdAndByLipidLevel(value, LipidLevel.CLASS)
          .subscribe((data) => {
            var boxData: Plotly.Data = {};
            // boxData["x"].push(data?.x);
            // boxData["x"].push(data?.x);
            boxData["x"] = data?.x;
            boxData["y"] = data?.y;
            data?.x.forEach((value, index) => {
              var tableData: any = {};
              tableData["x"] = value;
              tableData["y"] = data?.y[index];
              tableData["name"] = data?.mzTabResultId;
              tableData["unit"] = data?.quantificationUnitParamName;
              this.totalQuantBoxPlotTableData.push(tableData);
            });
            // boxData["q1"].push(plsd?.perc25);
            // boxData["median"].push(plsd?.perc50);
            // // boxData["mean"].push(plsd.averageAssayQuantity);
            // boxData["q3"].push(plsd?.perc75);
            // // boxData["sd"].push(plsd.stddevAssayQuantity);
            // boxData["lowerfence"].push(plsd?.lowerWhisker);
            // boxData["upperfence"].push(plsd?.upperWhisker);
            boxData["name"] = data?.mzTabResultId;
            boxData["type"] = "box";
            boxData["customdata"] = Array(data?.x?.length).fill(
              data.quantificationUnitParamName
            );
            this.totalQuantBoxPlotData.push(boxData);
            this.totalQuantBoxPlotLayout.yaxis.title =
              data.quantificationUnitParamName;
          });
      });
    });
    if (this.datasetIds.length > 1) {
      this.prepareSpeciesLevelCorrelationPlotData(
        this.datasetIds[0],
        this.datasetIds[1],
        LipidLevel.SPECIES
      );
      this.prepareSpeciesLevelBoxPlotData(this.datasetIds, LipidLevel.SPECIES);
      this.prepareLipidSpacePlotData(this.datasetIds, LipidLevel.SPECIES);
    }
  }

  prepareLipidSpacePlotData(
    mzTabResultIds: Array<string>,
    lipidLevel: LipidLevel
  ) {
    var mzTabResultArray: Array<EntityModelMzTabResult> = mzTabResultIds.map(
      (value) =>
        <EntityModelMzTabResult>{
          id: value,
        }
    );

    var lipidSpaceQueryDto: LipidSpaceQueryDto = <LipidSpaceQueryDto>(<unknown>{
      mzTabResultIds: mzTabResultIds,
      lipidNames: [],
      sampleCvTerms: [],
      assayNames: []
    });

    this.lipidSpaceService.compare(lipidSpaceQueryDto).subscribe((lipidSpaceResponse) => {
        console.debug(
          "Received lipidSpaceResponse: " +
            JSON.stringify(lipidSpaceResponse)
        );
        var globalLipidome = lipidSpaceResponse.LipidSpaces.filter((value, index, array) => {
          return value?.LipidomeName === "global_lipidome";
        });
        var lipidNameAtCategoryRequest = <LipidCompassQuery>
        {
          lipidLevel: LipidCompassQuery.LipidLevelEnum.CATEGORY,
          names: globalLipidome[0].LipidNames
        };
        var lipidCategories = this.lipidService.getLipidNameAtLevel(lipidNameAtCategoryRequest);
        lipidCategories.subscribe((data) => {
          console.debug(
            "Received lipidCategories: " +
              JSON.stringify(data)
          );
          
          var lipidColors = data._embedded.strings.map((value, index, array) => {
            if(value && value.length > 0) {
              // log lipid category and color from lipidCategoryColors
              var color = this.lipidCategoryColors[value];
              console.debug("Lipid category: " + value + " color: " + color);
              return color;
            }
            console.debug("Returning color for Unknown: " + color);
            return this.lipidCategoryColors["Unknown"];
          });
          console.debug("Lipid colors: " + JSON.stringify(lipidColors));
          var minSize = 10, maxSize = 50;
          var min = Math.min(...globalLipidome[0].Intensities);
          var max = Math.max(...globalLipidome[0].Intensities);
          var pointSizes = globalLipidome[0].Intensities.map((value) => (value-min)/(max-min)*(maxSize-minSize)+minSize);
          var scatterData: Plotly.Data = <Plotly.Data>{};
          var lipidSpacePlotData: Plotly.Data[] = [];
          scatterData["x"] = globalLipidome[0].X;
          scatterData["y"] = globalLipidome[0].Y;
          scatterData["text"] = globalLipidome[0].LipidNames;
          scatterData["name"] = "Global Lipidome";
          scatterData["mode"] = "markers";
          scatterData["type"] = "scatter";
          console.debug("Using marker colors: " + JSON.stringify(lipidColors));
          scatterData["marker"] = { 
            size: pointSizes,
            color: lipidColors
          };
          var distanceMatrixPlotData: Plotly.Data[] = [];
          var distanceMatrix: Plotly.Data = <Plotly.Data>{};
          var z: number[][] = [];
          var x: string[] = globalLipidome[0].LipidNames;
          var y: string[] = [];
          lipidSpaceResponse.LipidomeDistanceMatrix.forEach((value, index, array) => {
            z.push(value);
          });
          distanceMatrix["x"] = x;
          distanceMatrix["y"] = y;
          distanceMatrix["z"] = z;
          distanceMatrix["type"] = "heatmap";
          distanceMatrix["hoverongaps"] = false;
          distanceMatrixPlotData.push(distanceMatrix);

          lipidSpacePlotData.push(scatterData);
          this.lipidSpacePlotData = lipidSpacePlotData;
          this.lipidSpaceMatrixPlotData = distanceMatrixPlotData;
        });
      });
  }

  prepareSpeciesLevelBoxPlotData(
    mzTabResultIds: Array<string>,
    lipidLevel: LipidLevel
  ) {
    var mzTabResultArray: Array<EntityModelMzTabResult> = mzTabResultIds.map(
      (value) =>
        <EntityModelMzTabResult>{
          id: value,
        }
    );
    var query: LipidCompassQuery = <LipidCompassQuery>(<unknown>{
      lipidLevel: lipidLevel,
      names: [],
      matchMode: LipidCompassQuery.MatchModeEnum.PREFIX,
      normalizeName: false,
      unit: "",
      mzRange: [],
      quantityRange: [],
      mzTabResults: mzTabResultArray,
      facets: [],
      selectedFacets: [],
    });

    this.plottableLipidQuantityControllerService
      .findLipidQuantitiesGroupedByLipid(
        query,
        this.pageable.page,
        -1,
        this.pageable.sort
      )
      .subscribe((pagePlottableLipidQuantity) => {
        console.debug(
          "Received plottableLipidSummaryStats: " +
            JSON.stringify(pagePlottableLipidQuantity)
        );
        var page: PagePlottableLipidQuantity = pagePlottableLipidQuantity;
        if (!page.empty) {
          var plotlyBoxData: Plotly.Data[] = [];
          var boxPlotTableData: any[] = [];
          var plotlyDataSetData: Map<string, Plotly.Data[]> = new Map();
          var quantUnit: string = "quantity [Arbitrary Units]";
          // var xData = [];
          var plottableLipidSummaryData = page?.content;
          plottableLipidSummaryData.map((plsd) => {
            // plotlyBoxData.push();
            var boxData: Plotly.Data = <Plotly.Data>{};
            if (plotlyDataSetData.has(<string>plsd?.dataset)) {
              boxData = plotlyDataSetData.get(<string>plsd?.dataset);
            } else {
              boxData["x"] = [];
              boxData["q1"] = [];
              boxData["median"] = [];
              // boxData["mean"] = [];
              boxData["q3"] = [];
              // boxData["sd"] = [];
              boxData["lowerfence"] = [];
              boxData["upperfence"] = [];
              boxData["name"] = "";
              boxData["type"] = "box";
              plotlyDataSetData.set(<string>plsd?.dataset, boxData);
            }
            console.debug(
              "Handling data for " +
                plsd?.dataset +
                " for lipid: " +
                plsd?.normalizedShorthandNames
            );
            boxData["x"].push(plsd?.normalizedShorthandNames);
            boxData["q1"].push(plsd?.perc25);
            boxData["median"].push(plsd?.perc50);
            // boxData["mean"].push(plsd.averageAssayQuantity);
            boxData["q3"].push(plsd?.perc75);
            // boxData["sd"].push(plsd.stddevAssayQuantity);
            boxData["lowerfence"].push(plsd?.lowerWhisker);
            boxData["upperfence"].push(plsd?.upperWhisker);
            boxData["name"] = plsd?.dataset;
            boxData["type"] = "box";
            quantUnit = <string>plsd?.quantificationUnit;
            plotlyDataSetData.set(<string>plsd?.dataset, boxData);
          });
          for (let value of plotlyDataSetData.values()) {
            plotlyBoxData.push(value);
            console.debug(
              "Adding " + plotlyDataSetData.values.length + " rows to table!"
            );
            boxPlotTableData.push({
              name: value["name"],
              shorthandNames: value["x"],
              q1: value["q1"],
              median: value["median"],
              q3: value["q3"],
              lowerfence: value["lowerfence"],
              upperfence: value["upperfence"],
              unit: quantUnit,
            });
          }
          console.debug(plotlyBoxData);
          this.boxPlotData = plotlyBoxData;
          this.boxPlotLayout.yaxis.title = quantUnit;
        }
      });
  }

  prepareSpeciesLevelCorrelationPlotData(
    mzTabResultId1: string,
    mzTabResultId2: string,
    lipidLevel: LipidLevel
  ) {
    var mzTabResult1 = <EntityModelMzTabResult>{
      id: mzTabResultId1,
    };
    var mzTabResult2 = <EntityModelMzTabResult>{
      id: mzTabResultId2,
    };
    var referenceQuantities: Map<string, PlottableLipidQuantity> = new Map<
      string,
      PlottableLipidQuantity
    >();

    var refQuery: LipidCompassQuery = <LipidCompassQuery>(<unknown>{
      lipidLevel: lipidLevel,
      names: [],
      matchMode: LipidCompassQuery.MatchModeEnum.PREFIX,
      normalizeName: false,
      unit: "",
      mzRange: [],
      quantityRange: [],
      mzTabResults: [mzTabResult1],
      facets: [],
      selectedFacets: [],
    });

    this.plottableLipidQuantityControllerService
      .findLipidQuantitiesGroupedByLipid(
        refQuery,
        this.pageable.page,
        -1,
        this.pageable.sort
      )
      .subscribe((pagePlottableLipidQuantity) => {
        console.debug(
          "Received plottableLipidSummaryStats for species level correlation: " +
            JSON.stringify(pagePlottableLipidQuantity)
        );
        var allRef: number[] = [];
        var allComp: number[] = [];
        var page: PagePlottableLipidQuantity = pagePlottableLipidQuantity;
        if (!page.empty) {
          page.content.forEach((value, index, array) => {
            referenceQuantities.set(value.normalizedShorthandNames, value);
          });
          var compQuery: LipidCompassQuery = <LipidCompassQuery>(<unknown>{
            lipidLevel: lipidLevel,
            names: [...referenceQuantities.keys()],
            matchMode: LipidCompassQuery.MatchModeEnum.PREFIX,
            normalizeName: false,
            unit: "",
            mzRange: [],
            quantityRange: [],
            mzTabResults: [mzTabResult2],
            facets: [],
            selectedFacets: [],
          });

          this.plottableLipidQuantityControllerService
            .findLipidQuantitiesGroupedByLipid(
              compQuery,
              this.pageable.page,
              -1,
              this.pageable.sort
            )
            .subscribe((pagePlottableLipidQuantity) => {
              console.debug(
                "Received plottableLipidSummaryStats for species level correlation: " +
                  JSON.stringify(pagePlottableLipidQuantity)
              );
              var page: PagePlottableLipidQuantity = pagePlottableLipidQuantity;
              var comparisonData: PlottableLipidQuantity[][] = [];
              if (!page.empty) {
                page.content.forEach((value, index, array) => {
                  var dataset = value;
                  if (dataset) {
                    if (
                      referenceQuantities.has(dataset.normalizedShorthandNames)
                    ) {
                      comparisonData.push([
                        referenceQuantities.get(
                          dataset.normalizedShorthandNames
                        ),
                        dataset,
                      ]);
                    }
                  }
                });
              }
              console.info(
                "Plotting correlation for " +
                  comparisonData.length +
                  " common lipids!"
              );
              comparisonData.forEach((value, index, array) => {
                var reference: PlottableLipidQuantity =
                  comparisonData[index][0];
                var comp: PlottableLipidQuantity = comparisonData[index][1];
                var quantUnit: string = "quantity [Arbitrary Units]";
                var scatterData: Plotly.Data = <Plotly.Data>{};
                this.lipidSpeciesCorrelationPlotLayout.xaxis.title =
                  reference.dataset;
                this.lipidSpeciesCorrelationPlotLayout.yaxis.title =
                  comp.dataset;

                scatterData["x"] = [];
                scatterData["y"] = [];
                scatterData["name"] = "";
                // scatterData["mode"] = "markers";
                scatterData["type"] = "scatter";
                // scatterData["marker"] = { size: 12 };

                // console.debug("Handling data for " + dataset.dataset);
                scatterData["x"] = [reference.perc50];
                scatterData["error_x"] = {
                  type: "data",
                  symmetric: false,
                  array: [reference.perc75 - reference.perc50],
                  arrayminus: [reference.perc50 - reference.perc25],
                  // array: [Math.log10(reference.maxAssayQuantity - reference.perc50)],
                  // arrayminus: [Math.log10(reference.perc50 - reference.minAssayQuantity)],
                  visible: true,
                };
                scatterData["y"] = [comp.perc50];
                scatterData["error_y"] = {
                  type: "data",
                  symmetric: false,
                  array: [comp.perc75 - comp.perc50],
                  arrayminus: [comp.perc50 - comp.perc25],
                  visible: true,
                };
                scatterData["name"] = reference.normalizedShorthandNames;
                // scatterData["mode"] = "markers";
                scatterData["type"] = "scatter";
                // scatterData["marker"] = { size: 12 };
                quantUnit = <string>reference.quantificationUnit;
                console.info("Adding species correlation chart data");
                this.speciesCorrelationChartData.push(scatterData);

                reference.assayQuantities.forEach((element) => {
                  allRef.push(element);
                });
                comp.assayQuantities.forEach((element) => {
                  allComp.push(element);
                });
              });
              var lr = this.linearRegression2(allRef, allComp);
              var fit_from = Math.min(...allRef);
              var fit_to = Math.max(...allComp);

              var fit = {
                x: [fit_from, fit_to],
                y: [
                  fit_from * lr.sl + Math.log(lr.off),
                  fit_to * lr.sl + Math.log(lr.off),
                ],
                mode: "lines",
                type: "scatter",
                name: "R2="
                  .concat((Math.round(lr.r2 * 10000) / 10000).toString())
                  .concat(
                    " y=" +
                      Math.round(lr.sl * 10000) / 10000 +
                      "x+" +
                      Math.round(Math.log(lr.off) * 10000) / 10000
                  ),
              };
              this.speciesCorrelationChartData.push(fit);
            });
        }
      });
  }

  linearRegression2(x: number[], y: number[]): any {
    let sum_x = 0,
      sum_y = 0,
      sum_xx = 0,
      sum_xy = 0,
      sum_yy = 0;
    let count = x.length;
    for (let i = 0, len = count; i < count; i++) {
      sum_x += x[i];
      sum_y += y[i];
      sum_xx += x[i] * x[i];
      sum_xy += x[i] * y[i];
      sum_yy += y[i] * y[i];
    }
    let slope =
      (count * sum_xy - sum_x * sum_y) / (count * sum_xx - sum_x * sum_x);
    let intercept = sum_y / count - (slope * sum_x) / count;
    // // Generate values
    // const xValues = [];
    // const yValues = [];
    // for (let x = 50; x <= 150; x += 1) {
    //   xValues.push(x);
    //   yValues.push(x * slope + intercept);
    // }
    var lr = {};
    let n = count;
    lr["sl"] = (n * sum_xy - sum_x * sum_y) / (n * sum_xx - sum_x * sum_x);
    lr["off"] = sum_y / n - (lr["sl"] * sum_x) / n;
    lr["r2"] = Math.pow(
      (n * sum_xy - sum_x * sum_y) /
        Math.sqrt((n * sum_xx - sum_x * sum_x) * (n * sum_yy - sum_y * sum_y)),
      2
    );
    return lr;
  }

  linearRegression(x: number[], y: number[]): any {
    var lr = {};
    var n = y.length;
    var sum_x = 0;
    var sum_y = 0;
    var sum_xy = 0;
    var sum_xx = 0;
    var sum_yy = 0;

    for (var i = 0; i < y.length; i++) {
      sum_x += x[i];
      sum_y += y[i];
      sum_xy += x[i] * y[i];
      sum_xx += x[i] * x[i];
      sum_yy += y[i] * y[i];
    }

    lr["sl"] = (n * sum_xy - sum_x * sum_y) / (n * sum_xx - sum_x * sum_x);
    lr["off"] = (sum_y - lr["sl"] * sum_x) / n;
    lr["r2"] = Math.pow(
      (n * sum_xy - sum_x * sum_y) /
        Math.sqrt((n * sum_xx - sum_x * sum_x) * (n * sum_yy - sum_y * sum_y)),
      2
    );

    return lr;
  }
}
