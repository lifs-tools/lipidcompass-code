import { Injectable } from "@angular/core";
import { PlotlyComponent } from "angular-plotly.js";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import { forkJoin, from, Observable, pipe } from "rxjs";
import { map, mergeMap } from "rxjs/operators";
import {
  PlottableLipidQuantityControllerService,
  SummarizedLipidDataset,
} from "../../../modules/lipidcompass-backend-client";
import { colorScales } from "../../environments/environment";
import { LipidLevel } from "../_models/lipidLevel";

@Injectable({
  providedIn: "root",
})
export class PlotService {
  constructor(
    private plottableLipidQuantityControllerService: PlottableLipidQuantityControllerService
  ) {}

  // public loadClassLevelSummaryConcentrationPlotDataM(
  //   mzTabResultIds: string[],
  //   lipidLevel: LipidLevel
  // ): Observable<Plotly.Data[]> {
  //   return from(mzTabResultIds).pipe(
  //     mergeMap((mzTabResultId) => {
  //       this.plottableLipidQuantityControllerService.findyByMzTabResultIdAndByLipidLevel(
  //         mzTabResultId,
  //         lipidLevel
  //       );
  //       }, 4
  //     )
  //   );
  //   // return forkJoin(
  //   //   mzTabResultIds.mergeMap((id) => {
  //   //     return this.plottableLipidQuantityControllerService.findyByMzTabResultIdAndByLipidLevelbResultIdAndByLipidLevel(
  //   //       id,
  //   //       lipidLevel
  //   //     );
  //   //   })
  //   // ).subscribe((results: SummarizedLipidDataset) => {

  //   // });
  // }

  public loadLipidLevelConcentrationScatterChartPlotData(
    mzTabResultId: string,
    lipidLevel: LipidLevel
  ): Observable<Plotly.Data[]> {
    return this.plottableLipidQuantityControllerService
      .findByMzTabResultIdAndByLipidLevel(mzTabResultId, lipidLevel)
      .pipe(
        map((responseEntitySummarizedLipidDataset: SummarizedLipidDataset) => {
          var result: SummarizedLipidDataset =
            responseEntitySummarizedLipidDataset;
          // lipidCompositionData = new Plotly.Data()
          var plotlyDataSetData: Map<string, Plotly.Data[]> = new Map();
          var scatterData: Plotly.Data = <Plotly.Data>{};
          var nentries: number = result.x.length;
          // this.lipidClassRangesPlotLayout.height = nentries * 40;
          var uniqueCategories = [...new Set(result.lipidCategory)];
          var markerStyles: Array<any> = [];
          var categoryColors: Array<any> = [];
          uniqueCategories.forEach((v, idx) => {
            var color = colorScales.colorbrewer.Set2[8][idx % 8];
            markerStyles.push({
              target: v,
              value: {
                marker: {
                  color: color,
                  opacity: 0.7,
                  symbol: "line-ns",
                  size: 16,
                  line: {
                    color: color,
                    width: 3,
                  },
                },
              },
            });
            categoryColors.push(color);
          });
          if (plotlyDataSetData.has(<string>result.mzTabResultId)) {
            scatterData = plotlyDataSetData.get(<string>result.mzTabResultId);
          } else {
            scatterData["x"] = result.y;
            scatterData["y"] = result.x;
            scatterData["type"] = "scatter";
            scatterData["mode"] = "markers";
            scatterData["transforms"] = [
              {
                type: "groupby",
                // groups: result.lipidCategory.map((item) => item + " " + result.mzTabResultId),
                groups: result.lipidCategory,
                styles: markerStyles,
              },
            ];
            scatterData["name"] = result.mzTabResultId;
            plotlyDataSetData.set(<string>result.mzTabResultId, scatterData);
          }
          var plotData: Plotly.Data[] = [];
          // quantUnit = <string>plsd.quantificationUnit;
          for (let value of plotlyDataSetData.values()) {
            plotData.push(value);
          }
          return plotData;
        })
      );
  }

  public loadLipidLevelConcentrationPieChartPlotData(
    mzTabResultId: string,
    lipidLevel: LipidLevel,
    plotLayout: Plotly.Layout
  ): Observable<Plotly.Data[]> {
    return this.plottableLipidQuantityControllerService
      .findByMzTabResultIdAndByLipidLevel(mzTabResultId, lipidLevel)
      .pipe(
        map((responseEntitySummarizedLipidDataset: SummarizedLipidDataset) => {
          var result: SummarizedLipidDataset =
            responseEntitySummarizedLipidDataset;

          var concentrationPlotData: Plotly.Data = <Plotly.Data>{};
          var plotlyConcDataSetData: Map<string, Plotly.Data[]> = new Map();
          if (plotlyConcDataSetData.has(<string>result.mzTabResultId)) {
            concentrationPlotData = plotlyConcDataSetData.get(
              <string>result.mzTabResultId
            );
          } else {
            concentrationPlotData["labels"] = result.x;
            concentrationPlotData["values"] = result.y;
            concentrationPlotData["type"] = "pie";
            concentrationPlotData["name"] = result.mzTabResultId;
            concentrationPlotData["text"] = result.mzTabResultId;
            (concentrationPlotData["textinfo"] = "label+percent"),
              // concentrationPlotData["textposition"] = "inside",
              (concentrationPlotData["hole"] = 0.5),
              (concentrationPlotData["sort"] = false),
              (plotLayout.yaxis.categoryarray = result.x);
            plotlyConcDataSetData.set(
              <string>result.mzTabResultId,
              concentrationPlotData
            );
          }

          var concPlotData: Plotly.Data[] = [];
          for (let value of plotlyConcDataSetData.values()) {
            concPlotData.push(value);
          }
          return concPlotData;
        })
      );
  }

  public loadLipidLevelCompositionPieChartPlotData(
    mzTabResultId: string,
    lipidLevel: LipidLevel,
    plotLayout: Plotly.Layout
  ): Observable<Plotly.Data[]> {
    return this.plottableLipidQuantityControllerService
      .findByMzTabResultIdAndByLipidLevel(mzTabResultId, lipidLevel)
      .pipe(
        map((responseEntitySummarizedLipidDataset: SummarizedLipidDataset) => {
          var result: SummarizedLipidDataset =
            responseEntitySummarizedLipidDataset;
          // lipidCompositionData = new Plotly.Data()
          var plotlyCountDataSetData: Map<string, Plotly.Data[]> = new Map();
          var pieData: Plotly.Data = <Plotly.Data>{};
          if (plotlyCountDataSetData.has(<string>result.mzTabResultId)) {
            pieData = plotlyCountDataSetData.get(<string>result.mzTabResultId);
          } else {
            pieData["labels"] = result.x;
            pieData["values"] = result.groupCount;
            pieData["type"] = "pie";
            pieData["name"] = result.mzTabResultId;
            pieData["text"] = result.mzTabResultId;
            (pieData["textinfo"] = "label+percent"),
              // pieData["textposition"] = "inside",
              (pieData["hole"] = 0.5),
              (pieData["sort"] = false),
              (plotLayout.yaxis.categoryarray = result.x);
            plotlyCountDataSetData.set(<string>result.mzTabResultId, pieData);
          }

          // quantUnit = <string>plsd.quantificationUnit;
          var plotData: Plotly.Data[] = [];
          for (let value of plotlyCountDataSetData.values()) {
            plotData.push(value);
          }

          return plotData;
        })
      );
  }
}
