import { Component, Input, OnInit } from "@angular/core";
import { BreadcrumbModule } from "primeng/breadcrumb";
import { CvParameter } from "../../../../modules/lipidcompass-backend-client";

@Component({
    selector: "app-cvparameter-classification",
    templateUrl: "./cvparameter-classification.component.html",
    styleUrls: ["./cvparameter-classification.component.css"],
})
export class CvparameterClassificationComponent implements OnInit {
    @Input() cvParameters: CvParameter[] = [];

    cvReferenceType = CvParameter.ReferenceTypeEnum;

    instrumentParams: CvParameter[] = [];
    msRunParams: CvParameter[] = [];
    sampleParams: CvParameter[] = [];
    assayParams: CvParameter[] = [];
    studyVariableParams: CvParameter[] = [];
    smallMoleculeParams: CvParameter[] = [];
    unspecifiedParams: CvParameter[] = [];

    constructor() {}

    ngOnInit() {
        var sortedCvParameters = this.cvParameters.sort((a, b) => (a.name.toLocaleLowerCase() < b.name.toLocaleLowerCase() ? -1 : 1));
        sortedCvParameters.map((cvParam) => {
          switch(cvParam.referenceType) {
            case "INSTRUMENT_ANALYZER":
            case "INSTRUMENT_DETECTOR":
            case "INSTRUMENT_NAME":
            case "INSTRUMENT_SOURCE":
              this.instrumentParams.push(cvParam);
              break;
            case "SAMPLE_DISEASE":
            case "SAMPLE_CELLTYPE":
            case "SAMPLE_ORGANISM":
            case "SAMPLE_TISSUE":
            case "SAMPLE_CUSTOM":
              this.sampleParams.push(cvParam);
              break;
            case "MS_RUN_FORMAT":
            case "MS_RUN_FRAGMENTATION_METHOD":
            case "MS_RUN_HASH_METHOD":
            case "MS_RUN_ID_FORMAT":
            case "MS_RUN_SCAN_POLARITY":
              this.msRunParams.push(cvParam);
              break;
            case "ASSAY_CUSTOM":
              this.assayParams.push(cvParam);
              break;
            case "STUDY_VARIABLE_FACTOR":
              this.studyVariableParams.push(cvParam);
              break;
            case "BEST_ID_CONFIDENCE_MEASURE":
            case "SMALL_MOLECULE_QUANTIFICATION_UNIT":
              this.smallMoleculeParams.push(cvParam);
              break;
            case "UNSPECIFIED":
              this.unspecifiedParams.push(cvParam);
              break;
            
          }
            
        });
    }
}
