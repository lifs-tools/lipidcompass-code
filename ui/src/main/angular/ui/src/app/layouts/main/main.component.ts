import { Component, HostListener } from "@angular/core";
import {
    BreakpointObserver,
    Breakpoints,
    BreakpointState,
} from "@angular/cdk/layout";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { MenuItem } from "primeng/api";
import { filter } from "rxjs";
import { environment } from "../../../environments/environment";
import { Role } from "../../_models/role";
import { UserService } from "../../_services/user.service";

@Component({
    selector: "app-main",
    templateUrl: "./main.component.html",
    styleUrls: ["./main.component.scss"],
})
export class MainComponent {
    static readonly ROUTE_DATA_BREADCRUMB = "breadcrumb";
    readonly home = { icon: "fa fa-compass", url: "/home" };
    breadCrumbs: MenuItem[] = [this.home];
    items: MenuItem[];
    wrapSearch: Boolean = false;

    constructor(
        public userService: UserService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private breakpointObserver: BreakpointObserver
    ) {
        this.userService.isAuthenticated$.subscribe((observable) => {
            if (observable) {
                this.items = [
                    {
                        label: "Home",
                        icon: "fa fa-compass",
                        routerLink: ["/home"],
                        visible: environment.features.home,
                    },
                    {
                        label: "Search",
                        icon: "fa fa-search",
                        routerLink: ["/search"],
                        visible: environment.features.search,
                    },
                    {
                        label: "Studies",
                        icon: "fa fa-vials",
                        routerLink: ["/study"],
                        visible: environment.features.study,
                    },
                    {
                        label: "Datasets",
                        icon: "fa fa-archive",
                        routerLink: ["/dataset"],
                        visible: environment.features.dataset,
                    },
                    {
                        label: "Help",
                        icon: "fa fa-question",
                        routerLink: ["/help"],
                        visible: environment.features.help,
                    },
                    {
                        label: "About",
                        icon: "fa fa-info",
                        routerLink: ["/info"],
                        visible: environment.features.about,
                    },
                ];
                if (this.userService.hasRole(Role.Curator)) {
                    this.items.push({
                        label: "Curate",
                        icon: "fas fa-highlighter",
                        routerLink: ["/curate"],
                        visible: environment.features.curate,
                    });
                }
                if (this.userService.hasRole(Role.Admin)) {
                    this.items.push({
                        label: "Admin",
                        icon: "fas fa-tools",
                        routerLink: ["/admin"],
                        visible: environment.features.admin,
                    });
                }
                if (this.userService.hasRole(Role.User)) {
                    this.items.push({
                        label: "Submit",
                        icon: "fas fa-cloud-upload-alt",
                        routerLink: ["/submit"],
                        visible: environment.features.submit,
                    });
                }
                this.items.push({
                    label: "User",
                    icon: "fas fa-user",
                    routerLink: ["/login"],
                    visible: environment.features.login,
                });
            } else {
                this.items = [
                    {
                        label: "Home",
                        icon: "fa fa-compass",
                        routerLink: ["/"],
                        visible: environment.features.home,
                    },
                    {
                        label: "Login",
                        icon: "fa fa-sign-in-alt",
                        routerLink: ["/login"],
                        visible: environment.features.login,
                    }
                ];
            }
        });
        this.breakpointObserver
            .observe([
                Breakpoints.Medium,
                Breakpoints.Large,
                Breakpoints.XLarge,
            ])
            .subscribe((result: BreakpointState) => {
                if (result.matches) {
                    console.debug(
                        "Medium to large breakpoint matched: Not wrapping search!"
                    );
                    this.wrapSearch = false;
                }
            });
        this.breakpointObserver
            .observe([Breakpoints.XSmall, Breakpoints.Small])
            .subscribe((result: BreakpointState) => {
                if (result.matches) {
                    console.debug("Small breakpoint matched: Wrapping search!");
                    this.wrapSearch = true;
                }
            });
    }
}
