import { Component, OnInit } from "@angular/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter } from "rxjs/operators";
import { environment } from "../../../environments/environment";

@Component({
  selector: "app-pageNotFound",
  templateUrl: "./pageNotFound.component.html",
  styleUrls: ["./pageNotFound.component.css"],
})
export class PageNotFoundComponent implements OnInit {
  previousUrl: string;
  constructor(router: Router) {
    router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.previousUrl = event.url;
      });
  }

  ngOnInit() {}

  requestHelp(event: any) {
    window.open(environment.contactUrl, "_blank");
  }
}
