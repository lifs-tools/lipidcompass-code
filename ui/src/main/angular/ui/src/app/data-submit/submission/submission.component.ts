import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MenuItem } from "primeng/api";
import {
  EntityModelSubmission,
  SubmissionControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import { JobControllerService } from "../../../../modules/lipidcompass-data-importer-client";
import { submissionWorkflowConfig } from "../../../environments/environment";

@Component({
  selector: "app-submission",
  templateUrl: "./submission.component.html",
  styleUrls: ["./submission.component.css"],
})
export class SubmissionComponent implements OnInit {
  submissionId: string;
  activeIndex: number = 4;
  submission: EntityModelSubmission;

  constructor(
    private submissionService: SubmissionControllerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.submissionId = this.route.snapshot.params.id;
    this.submissionService.getById(this.submissionId).subscribe((result) => {
      this.submission = <EntityModelSubmission>result;
    });
  }

  nextPage(event: any) {
    if (this.submission) {
      this.submission.status = "SUBMITTED";
      this.submissionService.saveSingle(this.submission).subscribe((response) => {
        console.debug("NextPage saved submission: "+JSON.stringify(response));
        this.router.navigate(submissionWorkflowConfig.parentRoute);
      });
    } else {
      this.router.navigate(submissionWorkflowConfig.parentRoute);
    }
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
