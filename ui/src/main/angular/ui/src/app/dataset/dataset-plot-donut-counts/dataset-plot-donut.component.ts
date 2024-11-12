import { Component, Input, OnInit } from '@angular/core';
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import { lipidCategoryCompositionPlotLayout, plotlyConfig } from '../../../environments/environment';
import { LipidLevel } from '../../_models/lipidLevel';
import { PlotService } from '../../_services/plot.service';

@Component({
  selector: 'app-dataset-plot-donut',
  templateUrl: './dataset-plot-donut.component.html',
  styleUrls: ['./dataset-plot-donut.component.css']
})
export class DatasetPlotDonutComponent implements OnInit {

  @Input() datasetIds: string[];

  @Input() lipidLevel: LipidLevel = LipidLevel.CATEGORY;

  @Input() public plotLayout: Plotly.Layout = lipidCategoryCompositionPlotLayout;

  @Input() public plotConfig: Plotly.Config = plotlyConfig;

  @Input() counts: boolean = true;

  public plotData: Plotly.Data[] = [];

  constructor(public plotService: PlotService) {
  }

  ngOnInit() {
    this.datasetIds.forEach((value, datasetIdIndex, array) => {
      if(this.counts) {
        this.plotService
          .loadLipidLevelCompositionPieChartPlotData(
            value,
            this.lipidLevel,
            this.plotLayout
          )
          .subscribe((data) => {
            data.forEach((value, index, array) => {
              value.domain = {
                row: 0,
                column: datasetIdIndex,
              };
              this.plotData.push(<Plotly.Data>value);
            });
          });
        } else {
          this.plotService
          .loadLipidLevelConcentrationPieChartPlotData(
            value,
            this.lipidLevel,
            this.plotLayout
          )
          .subscribe((data) => {
            data.forEach((value, index, array) => {
              value.domain = {
                row: 0,
                column: datasetIdIndex,
              };
              this.plotData.push(<Plotly.Data>value);
            });
          });
        }
      });
  }

}
