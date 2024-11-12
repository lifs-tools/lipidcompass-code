import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import {
  EntityModelSubmission,
  FileResource,
  Submission,
  SubmissionControllerService,
} from "../../../../modules/lipidcompass-backend-client";
import { submissionWorkflowConfig } from "../../../environments/environment";
import { NotificationService } from "../../_services/notification.service";

@Component({
  selector: "app-upload",
  templateUrl: "./upload.component.html",
  styleUrls: ["./upload.component.scss"],
})
export class UploadComponent implements OnInit {
  submissionId: string;
  activeIndex: number = 2;
  submission: Submission;
  // uploadedFiles: File[];
  // fileName = "";
  file: File = null;
  public progress: boolean;

  constructor(
    private submissionService: SubmissionControllerService,
    private route: ActivatedRoute,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.submissionId = this.route.snapshot.params.id;
    this.submissionService.getById(this.submissionId).subscribe((result) => {
      this.submission = <Submission>result;
    });
  }

  clear() {
    this.file = null;
  }

  onSelectFile(event: any) {
    console.debug("onSelectFile");
    console.debug(JSON.stringify(event));
    const file: File = event.target.files[0];
    if (file) {
      console.debug("Setting file: " + JSON.stringify(file.name));
      this.file = file;
    }
  }

  deleteFile(file: FileResource) {
    console.debug("deleteFile: "+JSON.stringify(file));
    this.submissionService
      .deleteFile(this.submissionId, file)
      .subscribe((result) => {
        console.debug("Received response: " + JSON.stringify(result));
        this.submission = result;
      });
  }

  myCustomUpload(event: any) {
    console.debug("myCustomUpload: " + JSON.stringify(event));
    if (this.file) {
      console.debug("File is populated");
      const formData = new FormData();
      formData.append("file", this.file);
      this.progress = true;
      this.submissionService.upload(this.submissionId, this.file).subscribe(
        (response) => {
          console.debug("Received response: " + JSON.stringify(response));
          this.submission = response;
          this.file = null;
          this.progress = false;
        },
        (error) => {
          console.debug("Received error response: " + JSON.stringify(error));
          this.progress = false;
          this.notificationService.publish(
            "messages",
            "error",
            "Error",
            JSON.stringify(error)
          );
        }
      );
    } else {
      console.debug("FileForm is not populated");
    }
    return;
  }

  nextPage(event: any) {
    console.debug("nextPage: " + JSON.stringify(event));
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
