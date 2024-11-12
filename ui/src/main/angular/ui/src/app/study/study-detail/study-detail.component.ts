import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { Study, StudyControllerService } from '../../../../modules/lipidcompass-backend-client';

@Component({
  selector: 'app-study-detail',
  templateUrl: './study-detail.component.html',
  styleUrls: ['./study-detail.component.css']
})
export class StudyDetailComponent implements OnInit {
  study: Observable<Study>;
  private studyId: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studyControllerService: StudyControllerService,
    translate: TranslateService
  ) { 
    translate.setDefaultLang('en');
    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
    this.studyId = route.snapshot.params.id;
  }

  ngOnInit(): void {
    this.study = this.studyControllerService.getById(this.studyId);
  }

}
