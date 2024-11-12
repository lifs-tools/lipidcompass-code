import { Component } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { submissionWorkflowConfig } from "../../../environments/environment";

@Component({
  selector: 'app-select-workflow',
  standalone: false,
  // imports: [],
  templateUrl: './select-workflow.component.html',
  styleUrl: './select-workflow.component.css'
})
export class SelectWorkflowComponent {

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  nextPage(event: any) {
    // if (this.submission && this.submission.study) {
    //   this.submission.study.name = this.studyName;
    //   this.submission.study.description = this.studyDescription;
    //   this.studyControllerService.saveSingle(this.submission.study).subscribe((response) => {
    //     console.debug("NextPage saved study: "+JSON.stringify(response));
    //     this.router.navigate(
    //       [
    //         submissionWorkflowConfig.steps[this.activeIndex].routerLink,
    //         this.submissionId,
    //       ],
    //       { relativeTo: this.route.parent }
    //     );
    //   })
      
    // } else {
    //   this.router.navigate(
    //     [
    //       submissionWorkflowConfig.steps[this.activeIndex].routerLink,
    //       this.submissionId,
    //     ],
    //     { relativeTo: this.route.parent }
    //   );
    // }
    return;
  }

  previousPage(event: any) {
    // if (this.submission) {
    //   this.submissionService.saveSingle(this.submission).subscribe((response) => {
    //     console.debug("PreviousPage saved submission: "+JSON.stringify(response));
    //     this.router.navigate(submissionWorkflowConfig.parentRoute);
    //   });
    // } else {
    //   this.router.navigate(submissionWorkflowConfig.parentRoute);
    // }
    return;
  }

  cancel(event: any) {
    this.router.navigate(submissionWorkflowConfig.parentRoute);
    return;
  }

}
