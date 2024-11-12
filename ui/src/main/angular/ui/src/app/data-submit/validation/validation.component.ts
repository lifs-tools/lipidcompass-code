import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable, of } from "rxjs";
import {
  FileResource,
  Submission,
  SubmissionControllerService
} from "../../../../modules/lipidcompass-backend-client";
import { submissionWorkflowConfig } from "../../../environments/environment";

@Component({
  selector: "app-validation",
  templateUrl: "./validation.component.html",
  styleUrls: ["./validation.component.css"],
})
export class ValidationComponent implements OnInit {
  submissionId: string;
  activeIndex: number = 3;
  submission: Submission;
  public progress: boolean;
  validationMessages: any = {};
  level: any;
  maxErrors: 30;
  semanticValidation: true;
  mzTabFiles: Observable<FileResource[]>;
  hasMzTabMfiles: false;
  errorCount: number = 0;

  constructor(
    private submissionService: SubmissionControllerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.submissionId = this.route.snapshot.params.id;
    this.submissionService.getById(this.submissionId).subscribe((result) => {
      this.submission = <Submission>result;
      if (this.submission) {
        console.debug("Found the following files: "+JSON.stringify(this.submission.submittedFiles));
        let mzTabFileTmp = this.submission?.submittedFiles?.filter( (value) => value.fileType==="MZTAB_M");
        this.mzTabFiles = of(this.submission?.submittedFiles?.filter( (value) => value.fileType==="MZTAB_M"));
        console.debug("Found the following mzTab files: "+JSON.stringify(mzTabFileTmp));
      }
    });
  }

  validate(
    fileResource: FileResource,
  ) {
    console.debug("Validating file: " + JSON.stringify(fileResource));
    this.progress = true;
    this.submissionService.validateMzTabFile(this.submissionId, fileResource)
      .subscribe(
        (response) => {
          console.debug("Received response: " + JSON.stringify(response));
          if(response?._embedded?.validationMessages?.length>0) {
            this.validationMessages[fileResource.fileName] = response?._embedded?.validationMessages;
          } else {
            this.validationMessages[fileResource.fileName] = [];
          }
          // this.errorCount[fileResource.fileName] = this.validationMessages.map()
          this.progress = false;
        },
        (error) => {
          console.debug("Received error response: " + JSON.stringify(error));
          if(error?._embedded?.validationMessages?.length>0) {
            this.validationMessages[fileResource.fileName] = error?._embedded?.validationMessages;
          } else {
            this.validationMessages[fileResource.fileName] = [];
          }
          this.progress = false;
          // this.validationMessages[fileId] = error
        }
      );
  }

  nextPage(event: any) {
    this.router.navigate(
      [
        submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex].routerLink,
        this.submissionId,
      ],
      { relativeTo: this.route.parent }
    );
    return;
  }

  previousPage(event: any) {
    console.debug("previousPage: " + JSON.stringify(event));
    if (this.activeIndex > 1) {
      console.debug(
        "previousPage via activeIndex: " +
          JSON.stringify([
            submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex - 2].routerLink,
            this.submissionId,
          ])
      );
      console.debug(
        "Active index: " +
          (this.activeIndex - 2) +
          " is " +
          JSON.stringify(
            submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex - 2].routerLink
          )
      );
      this.router.navigate(
        [
          submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex - 2].routerLink,
          this.submissionId,
        ],
        { relativeTo: this.route.parent }
      );
    } else {
      console.debug(
        "previousPage to parent: " + JSON.stringify([this.route.parent])
      );
      this.router.navigate(submissionWorkflowConfig.parentRoute);
    }
    return;
  }

  cancel(event: any) {
    this.router.navigate(submissionWorkflowConfig.parentRoute);
    return;
  }
}
