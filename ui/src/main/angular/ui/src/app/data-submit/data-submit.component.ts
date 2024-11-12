import { Component, OnInit } from "@angular/core";
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  Event
} from "@angular/router";
import { MenuItem } from "primeng/api";
import {
  Study,
  StudyControllerService,
  Submission,
  SubmissionControllerService,
  User,
  UserControllerService,
  EntityModelSubmission,
} from "../../../modules/lipidcompass-backend-client";
import { submissionWorkflowConfig } from "../../environments/environment";
import { UserService } from "../_services/user.service";
import { paging } from "../../environments/environment";
import { MzTabResultDocumentSearchControllerService } from "../../../modules/lipidcompass-search-client";
import { filter, Subject, takeUntil } from "rxjs";
import { UniqueSelectionDispatcher } from "@angular/cdk/collections";
import { v4 as uuid } from "uuid";
import { Pageable } from "../../../modules/lipidcompass-backend-client/model/pageable";

@Component({
  selector: "app-data-submit",
  templateUrl: "./data-submit.component.html",
  styleUrls: ["./data-submit.component.css"],
})
export class DataSubmitComponent implements OnInit {
  private destroyed$ = new Subject();
  dataSubmitSteps: MenuItem[];
  submissionInProgress: Boolean = false;
  pageable: Pageable;
  submissions: EntityModelSubmission[];
  loading: boolean;

  constructor(
    public userService: UserService,
    public userControllerService: UserControllerService,
    public submissionService: SubmissionControllerService,
    public studyService: StudyControllerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.router.events
      .pipe(
        filter((event: Event) => event instanceof NavigationEnd),
        takeUntil(this.destroyed$)
      )
      .subscribe((event: NavigationEnd) => {
        console.debug("NavigationEnd: " + JSON.stringify(event));
        if (event.url === "/submit") {
          this.submissionInProgress = false;
          this.listSubmissions();
        }
      });
    this.dataSubmitSteps = submissionWorkflowConfig.workflows.mzTab.steps;
    this.submissionInProgress = false;
    this.pageable = paging.defaultPageable as Pageable;
    this.listSubmissions();
  }

  newSubmission() {
    this.submissionInProgress = true;
    this.userControllerService.user().subscribe((user) => {
      let loadedUser = <User>user;
      let transactionUuid =  uuid();
      if(loadedUser) {
        
          let submission: Submission = <Submission>{
            visibility: Submission.VisibilityEnum.PRIVATE,
            status: Submission.StatusEnum.IN_PROGRESS,
            study: null,
            submitter: loadedUser,
            transactionUuid: transactionUuid
          };
          this.submissionService
            .saveSingle(submission)
            .subscribe((response) => {
                console.debug(JSON.stringify(response));
                this.router.navigate(
                  [submissionWorkflowConfig.workflows.mzTab.steps[0].routerLink, response.id],
                  { relativeTo: this.route }
                )
              }
            );
      } 
    });
    return;
  }

  downloadFile(fileName: string) {
    
  }

  listSubmissions() {
    this.loading = true;
    this.submissionService
      .queryUserSubmissions(this.pageable.page, this.pageable.size, this.pageable.sort)
      .subscribe((res) => {
        console.debug(JSON.stringify(res));
        this.submissions = res?._embedded?.submissions;
        this.loading = false;
        this.submissionInProgress = false;
      });
    return;
  }

  deleteSubmission(submissionId: string) {
    // this.confirmationService.confirm({
    //   message: "Really delete this submission?",
    //   accept: () => {
    this.loading = true;
    this.submissionService.deleteById(submissionId).subscribe((res) => {
      this.loading = false;
    });
    this.submissionInProgress = false;
    this.listSubmissions();
    //   },
    // });
    return;
  }

  editSubmission(submissionId: string) {
    this.submissionInProgress = true;
    console.debug(
      "Navigating to: " +
        JSON.stringify(submissionWorkflowConfig.workflows.mzTab.steps[0].routerLink) +
        "/" +
        submissionId
    );
    this.router.navigate(
      [submissionWorkflowConfig.workflows.mzTab.steps[0].routerLink, submissionId],
      { relativeTo: this.route }
    );
    return;
  }
}
