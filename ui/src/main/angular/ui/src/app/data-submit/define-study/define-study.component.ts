import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MenuItem } from "primeng/api";
import {
  EntityModelSubmission,
  Study,
  StudyControllerService,
  Submission,
  SubmissionControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import { submissionWorkflowConfig } from "../../../environments/environment";

@Component({
  selector: "app-define-study",
  templateUrl: "./define-study.component.html",
  styleUrls: ["./define-study.component.css"],
})
export class DefineStudyComponent implements OnInit {
  submissionId: string;
  activeIndex: number = 1;
  submission: Submission = <Submission>{};
  submitted: boolean;
  studyName: string = "Please add a name";
  nameMinLength: number = 10;
  studyDescription: string = "Please add a description";
  descriptionMinLength: number = 20;

  constructor(
    private submissionService: SubmissionControllerService,
    private studyControllerService: StudyControllerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.submissionId = this.route.snapshot.params.id;
    this.submissionService.getById(this.submissionId).subscribe((result) => {
      this.submission = <Submission>result;
      this.studyName = this.submission?.study.name;
      this.studyDescription = this.submission?.study.description;
    }, (error) => {
      this.submission = <Submission>{};
    });
  }

  nextPage(event: any) {
    if (this.submission && this.submission.study) {
      this.submission.study.name = this.studyName;
      this.submission.study.description = this.studyDescription;
      this.studyControllerService.saveSingle(this.submission.study).subscribe((response) => {
        console.debug("NextPage saved study: "+JSON.stringify(response));
        this.router.navigate(
          [
            submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex].routerLink,
            this.submissionId,
          ],
          { relativeTo: this.route.parent }
        );
      })
      
    } else {
      this.router.navigate(
        [
          submissionWorkflowConfig.workflows.mzTab.steps[this.activeIndex].routerLink,
          this.submissionId,
        ],
        { relativeTo: this.route.parent }
      );
    }
    return;
  }

  previousPage(event: any) {
    if (this.submission) {
      this.submissionService.saveSingle(this.submission).subscribe((response) => {
        console.debug("PreviousPage saved submission: "+JSON.stringify(response));
        this.router.navigate(submissionWorkflowConfig.parentRoute);
      });
    } else {
      this.router.navigate(submissionWorkflowConfig.parentRoute);
    }
    return;
  }

  cancel(event: any) {
    this.router.navigate(submissionWorkflowConfig.parentRoute);
    return;
  }
}
