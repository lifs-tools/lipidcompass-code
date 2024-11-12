import { Component, OnInit } from "@angular/core";
import {
  EntityModelStudy,
  StudyControllerService,
  Study,
} from "../../../modules/lipidcompass-backend-client";
import { Pageable } from "../../../modules/lipidcompass-backend-client/model/pageable";
import { paging } from "../../environments/environment";
import { UserService } from "../_services/user.service";

@Component({
  selector: "app-study",
  templateUrl: "./study.component.html",
  styleUrls: ["./study.component.css"],
})
export class StudyComponent implements OnInit {
  // organismData: Array<Organism>;
  // tissueData: Object[];
  studyData: EntityModelStudy[];
  // selectedOrganisms: Array<Organism>;
  // organismsPage: Page<Organism>;
  // selectedTissues: Array<Tissue>;
  selectedStudies: Array<Study>;
  pageable: Pageable = { ...paging.defaultPageable };

  constructor(
    private studyControllerService: StudyControllerService,
    private userService: UserService
  ) {
    // console.debug("User has roles: " + userService.userRoles());
  }

  ngOnInit() {
    this.studyControllerService
      .get(this.pageable.page, this.pageable.size, this.pageable.sort)
      .subscribe((studies) => {
        console.debug("Receiving dataset data!");
        this.studyData = studies?._embedded?.studies;
      });
    // console.debug("User has roles: " + this.userService.userRoles());
  }
}
