import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Submission } from "../../../../modules/lipidcompass-backend-client";

@Component({
  selector: "app-submission-card",
  templateUrl: "./submission-card.component.html",
  styleUrls: ["./submission-card.component.css"],
})
export class SubmissionCardComponent implements OnChanges {
  @Input() submission: Submission;
  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnChanges(changes: SimpleChanges) {
    console.debug("Received onChanges: " + JSON.stringify(changes));
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case "submission": {
            if (changes.submission.currentValue) {
              console.debug(
                "Received change for submission: " +
                  changes.submission.currentValue.id
              );
            }
          }
        }
      }
    }
  }
}
