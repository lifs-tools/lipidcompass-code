import {
  BreakpointObserver,
  Breakpoints,
  BreakpointState,
} from "@angular/cdk/layout";
import { Component, OnInit } from "@angular/core";
import { MenuItem } from "primeng/api";
import { environment } from "../../../environments/environment";
import { UserService } from "../../_services/user.service";

@Component({
  selector: "app-user-card",
  templateUrl: "./user-card.component.html",
  styleUrls: ["./user-card.component.css"],
})
export class UserCardComponent implements OnInit {
  items: MenuItem[];
  realmUrl: string;
  isAuthenticated: boolean;
  applicationName: string = environment.applicationName;
  cardCssClass: string = "user-card";
  // private routerEvents: Subscription;

  constructor(
    public userService: UserService,
    private breakpointObserver: BreakpointObserver
  ) {}

  ngOnInit() {
    this.items = [
      {
          label: "Home",
          icon: "fa fa-compass",
          url: "/"
      }      
    ];
    this.userService.isAuthenticated$.subscribe((isSubscribed) => {
      this.isAuthenticated = isSubscribed;
    });
    this.breakpointObserver
      .observe([Breakpoints.Large, Breakpoints.XLarge])
      .subscribe((result: BreakpointState) => {
        if (result.matches) {
          console.debug("Large breakpoint matched: Not wrapping search!");
          this.cardCssClass = "user-card";
        }
      });
    this.breakpointObserver
      .observe([Breakpoints.Medium])
      .subscribe((result: BreakpointState) => {
        if (result.matches) {
          console.debug("Medium breakpoint matched: Not wrapping search!");
          this.cardCssClass = "user-card-tablet";
        }
      });
    this.breakpointObserver
      .observe([Breakpoints.XSmall, Breakpoints.Small])
      .subscribe((result: BreakpointState) => {
        if (result.matches) {
          console.debug("Small breakpoint matched: Wrapping search!");
          this.cardCssClass = "user-card-phone";
        }
      });
  }

  login() {
    this.userService.login();
  }

  logout() {
    this.userService.logout();
  }

  manageAccount(event: any) {
    console.debug("manageAccount");
    window.open(this.userService.getAccountManagementUrl().toString());
  }

  requestAccess(event: any) {
    window.open(environment.contactUrl, "_blank");
  }
}
