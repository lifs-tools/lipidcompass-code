import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Route, Router, RouterState, RouterStateSnapshot, UrlSegment, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { UserService } from "../_services/user.service";

@Injectable({
  providedIn: "root",
})
export class HasRoleGuard
  
{
  constructor(private authService: UserService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.checkUserLogin(next, state);
  }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.canActivate(next, state);
  }

  canDeactivate(
    component: unknown,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return true;
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]
  ): Observable<boolean> | Promise<boolean> | boolean {
    return true;
  }

  private navigateToOnFailure(route: string, state?: RouterStateSnapshot) {
    if (state) {
      this.router.navigate([route], { queryParams: { returnUrl: state.url } });
    }
    this.router.navigate([route]);
  }

  private checkUserLogin(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (!route.data.featureEnabled) {
      console.debug(
        "Feature not enabled for route: '" +
          state.url +
          "'! Navigating back to home!"
      );
      this.navigateToOnFailure("home");
      return false;
    }
    const requiresLogin = route.data.requiresLogin || false;
    if (requiresLogin) {
      if (this.authService.checkAuthenticated()) {
        if (route.data.role) {
          if (this.authService.hasRole(route.data.role)) {
            return true;
          }
          this.navigateToOnFailure("login", state);
          return false;
        } else {
          console.warn(
            "No role was defined for authenticated route " +
              route.pathFromRoot +
              ". This is most likely an error!"
          );
          this.navigateToOnFailure("login", state);
          return false;
        }
      }
      this.navigateToOnFailure("login", state);
      return false;
    }
    return true;
  }
}
