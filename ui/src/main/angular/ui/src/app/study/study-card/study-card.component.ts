import { Component, Input } from "@angular/core";
import { Router } from "@angular/router";
import { Study } from "../../../../modules/lipidcompass-backend-client";
import { OrderByPipe } from "../../_pipes/orderBy.pipe";

@Component({
    selector: "app-study-card",
    templateUrl: "./study-card.component.html",
    styleUrls: ["./study-card.component.css"],
})
export class StudyCardComponent {
    @Input() study: Study;

    @Input() detailView: Boolean = true;

    constructor(private router: Router) {}

    openDatasetComparison(): void {
        if (
            this.study.mzTabResultReferences &&
            this.study.mzTabResultReferences.length > 0
        ) {
            this.router.navigate(["dataset/compare"], {
                state: {
                    datasets: this.study?.mzTabResultReferences?.map(
                        (mzTabResult) => {
                            return mzTabResult.id;
                        }
                    ),
                },
            });
        }
    }
}
