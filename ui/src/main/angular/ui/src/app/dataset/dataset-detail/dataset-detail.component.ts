import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { MzTabResult, MzTabResultControllerService } from '../../../../modules/lipidcompass-backend-client';

enum IdTypeEnum { NATIVE_ID , ID , UNKNOWN };

@Component({
  selector: 'app-dataset-detail',
  templateUrl: './dataset-detail.component.html',
  styleUrls: ['./dataset-detail.component.css']
})
export class DatasetDetailComponent implements OnInit {

  mzTabResult: Observable<MzTabResult>;
  private mzTabResultId: string;
  private idType: IdTypeEnum= IdTypeEnum.UNKNOWN;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private mzTabResultControllerService: MzTabResultControllerService,
    translate: TranslateService
  ) { 
    translate.setDefaultLang('en');
    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
    console.log("Route snapshot parameters: " + JSON.stringify(route.snapshot.params));
    if(route.snapshot.params.id !== undefined) {
      this.mzTabResultId = route.snapshot.params.id;
      this.idType = IdTypeEnum.ID;
    } else if(route.snapshot.params.nativeId !== undefined) {
      this.mzTabResultId = route.snapshot.params.nativeId;
      this.idType = IdTypeEnum.NATIVE_ID;
    }
    console.log("mzTabResultId: " + this.mzTabResultId);
  }

  ngOnInit(): void {
    switch(this.idType) {
      case IdTypeEnum.ID:
        console.log("Retrieving mzTabResult by database id: " + this.mzTabResultId + " ...");
        this.mzTabResult = this.mzTabResultControllerService.getById(this.mzTabResultId);
        break;
      case IdTypeEnum.NATIVE_ID:
        console.log("Retrieving mzTabResult by native id: " + this.mzTabResultId + " ..."); 
        this.mzTabResult = this.mzTabResultControllerService.getByNativeId(this.mzTabResultId);
        break;
      default:
        throw new Error("Unknown id type: " + this.idType);
    }
  }

}
