import { Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import {
  EntityModelLipidQuantity,
  EntityModelMzTabResult,
  LipidCompassQuery,
  LipidQuantityControllerService,
  LipidQuantityDatasetAssayTableRow,
  MzTabResult,
  PlottableLipidQuantityControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import {
  lipidCategoryCompositionPlotLayout,
  lipidCategoryConcentrationPlotLayout,
  lipidClassRangesPlotLayout,
  lipidSpeciesRangesPlotLayout,
  plotlyConfig,
} from "../../../environments/environment";
import { LipidLevel } from "../../_models/lipidLevel";
import { PlotService } from "../../_services/plot.service";

@Component({
  selector: "app-dataset-card",
  templateUrl: "./dataset-card.component.html",
  styleUrls: ["./dataset-card.component.css"],
})
export class DatasetCardComponent implements OnChanges {
  @Input() mzTabResult: MzTabResult;

  @Input() detailView: boolean = false;

  @Input() showClassifications: boolean = true;

  public rating = {
    value: 1,
    label: MzTabResult.RatingEnum.UNRATED,
    ncases: Object.keys(MzTabResult.RatingEnum).length - 1,
  };

  lipidCategoryCompositionPlotData: Plotly.Data[] = [];
  lipidCategoryConcentrationPlotData: Plotly.Data[] = [];
  lipidClassCompositionPlotData: Plotly.Data[] = [];
  lipidSpeciesCompositionPlotData: Plotly.Data[] = [];

  lipidQuantities: EntityModelLipidQuantity[];
  lipidQuantityTable: LipidQuantityDatasetAssayTableRow[];

  lipidLevel: LipidLevel = LipidLevel.CLASS;

  categoryLipidLevel: LipidLevel = LipidLevel.CATEGORY;

  public lipidClassRangesPlotLayout: Plotly.Layout = lipidClassRangesPlotLayout;
  public lipidCategoryCompositionPlotLayout: Plotly.Layout = lipidCategoryCompositionPlotLayout;
  public lipidCategoryConcentrationPlotLayout: Plotly.Layout = lipidCategoryConcentrationPlotLayout;
  public lipidSpeciesRangesPlotLayout: Plotly.Layout = lipidSpeciesRangesPlotLayout;

  public homePlotlyConfig: Plotly.Config = plotlyConfig;

  constructor(
    private translate: TranslateService,
    private plottableLipidQuantityControllerService: PlottableLipidQuantityControllerService,
    private lipidQuantityControllerService: LipidQuantityControllerService,
    private plotService: PlotService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    console.debug("mzTabResult: " + JSON.stringify(this.mzTabResult));
    if (changes?.mzTabResult?.currentValue) {
      var mzTabResultId = changes.mzTabResult.currentValue.id;
      this.rating.label = this.mzTabResult?.rating;
      switch (this.mzTabResult?.rating) {
        case MzTabResult.RatingEnum.UNRATED:
          this.rating.value = 1;
          break;
        case MzTabResult.RatingEnum.AUTOMATICALLY_CHECKED:
          this.rating.value = 2;
          break;
        case MzTabResult.RatingEnum.MANUALLY_CURATED:
          this.rating.value = 3;
          break;
        default:
          this.rating.value = 0;
          console.error("Unknown rating: " + this.mzTabResult?.rating);
          break;
      }
      if (this.detailView) {
        console.debug("Loading summary data for " + mzTabResultId);
        // load category level concentration summary
        this.plotService
          .loadLipidLevelConcentrationPieChartPlotData(
            mzTabResultId,
            LipidLevel.CATEGORY,
            lipidCategoryConcentrationPlotLayout
          )
          .subscribe((plotData: Plotly.Data[]) => {
            this.lipidCategoryConcentrationPlotData = plotData;
          });
        // load category level composition summary
        this.plotService
          .loadLipidLevelCompositionPieChartPlotData(
            mzTabResultId,
            LipidLevel.CATEGORY,
            lipidCategoryCompositionPlotLayout
          )
          .subscribe((plotData: Plotly.Data[]) => {
            this.lipidCategoryCompositionPlotData = plotData;
          });
        // load class level concentration summary
        this.plotService
          .loadLipidLevelConcentrationScatterChartPlotData(
            mzTabResultId,
            LipidLevel.CLASS
          )
          .subscribe((plotData: Plotly.Data[]) => {
            this.lipidClassCompositionPlotData = plotData;
          });
        // load species level concentration summary
        this.plotService
          .loadLipidLevelConcentrationScatterChartPlotData(
            mzTabResultId,
            LipidLevel.SPECIES
          )
          .subscribe((plotData: Plotly.Data[]) => {
            this.lipidSpeciesCompositionPlotData = plotData;
          });

        var query = <LipidCompassQuery>{
          lipidLevel: LipidLevel.SPECIES,
          names: [],
          matchMode: LipidCompassQuery.MatchModeEnum.PREFIX,
          normalizeName: false,
          unit: "",
          mzRange: [],
          quantityRange: [],
          mzTabResults: [
            <EntityModelMzTabResult>{
              id: this.mzTabResult.id,
            },
          ],
          facets: [],
          selectedFacets: [],
        };
        this.plottableLipidQuantityControllerService
          .findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName(
            query,
            0,
            20,
            ["normalizedShorthandNames,asc", "assay,asc"]
          )
          .subscribe((result) => {
            this.lipidQuantityTable = result?.content;
          });
      } else {
        console.debug(
          "Not loading plot data. Set 'detailView=true' to enable plot data loading!"
        );
      }
    }
  }
}
