import { Injectable } from "@angular/core";
import {
  OAuthEvent,
  OAuthService,
  UserInfo,
} from "angular-oauth2-oidc";
import { BehaviorSubject } from "rxjs";
import { UserControllerService } from "../../../modules/lipidcompass-backend-client";
import { authCodeFlowConfig } from "../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class UserService {
  userProfile: any;
  applicationRoles: string[];
  accountManagementUrl: String;
  isAuthenticated$ = new BehaviorSubject<boolean>(false);

  constructor(private oauthService: OAuthService, private userControllerService: UserControllerService) {
    this.oauthService.events.subscribe(({ type }: OAuthEvent) => {
      switch (type) {
        case "token_received":
          // console.debug("Received token!");
          const idToken = this.oauthService.getIdToken();
          const accessToken = this.oauthService.getAccessToken();
          if (accessToken && idToken) {
            // console.debug("access_token: " + accessToken);
            // console.debug("id_token: " + idToken);
          }
        default:
          // console.debug("oauth service event: " + type);
      }
      this.checkAuthenticated();
    });
    this.oauthService.configure(authCodeFlowConfig);
    this.oauthService.setStorage(sessionStorage);
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
    this.accountManagementUrl = authCodeFlowConfig.issuer + "/account";
    this.isAuthenticated$.subscribe((isAuthenticated) => {
      if (isAuthenticated) {
        sessionStorage.setItem(
          "lipidcompass_roles",
          JSON.stringify(this.userRoles())
        );
      } else {
        sessionStorage.removeItem("lipidcompass_roles");
      }
    });
  }

  checkAuthenticated(): boolean {
    var hasIdToken = this.oauthService.hasValidIdToken();
    var hasAccessToken = this.oauthService.hasValidAccessToken();
    var isAuthenticated = hasIdToken && hasAccessToken;
    this.isAuthenticated$.next(isAuthenticated);
    // console.debug("isAuthenticated=" + isAuthenticated);
    // if (isAuthenticated) {
    //   console.debug(
    //     "jwt token: " +
    //       JSON.stringify(this.parseJwt(this.oauthService.getAccessToken()))
    //   );
    // }
    return isAuthenticated;
  }

  userName() {
    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;
    // console.debug("claims: " + JSON.stringify(claims));
    return claims["name"];
  }

  userEmail() {
    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;
    // console.debug("claims: " + JSON.stringify(claims));
    return claims["email"];
  }

  userId() {
    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;
    // console.debug("claims: " + JSON.stringify(claims));
    return claims["sub"];
  }

  hasRole(role: string) {
    var roleMatched = this.userRoles().find((x: string) => x == role);
    if (roleMatched === undefined || !roleMatched) {
      return false;
    }
    return true;
  }

  userRoles(): string[] {
    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;
    var jwt = this.parseJwt(this.oauthService.getAccessToken());
    // console.debug("application roles: " + JSON.stringify(jwt["resource_access"]["lipidcompass-ui"]["roles"]));
    this.applicationRoles = <string[]>(
      jwt["resource_access"]["lipidcompass-ui"]["roles"]
    );
    return this.applicationRoles;
  }

  getUserClaims(): Promise<object | UserInfo> {
    return this.oauthService
      .loadUserProfile()
      .then((up) => (this.userProfile = up));
  }

  parseJwt(token: any) {
    var base64Url = token.split(".")[1];
    var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    var jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map(function (c) {
          return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join("")
    );

    return JSON.parse(jsonPayload);
  }

  getToken(): string {
    return sessionStorage.getItem("access_token");
  }

  getAccountManagementUrl() {
    return this.accountManagementUrl;
  }

  login() {
    // console.debug("Login");
    this.oauthService.initCodeFlow();
    this.oauthService.setupAutomaticSilentRefresh();
  }

  logout() {
    // console.debug("Logout");
    this.oauthService.logOut();
  }

  updateDatabaseUser() {
    if (this.checkAuthenticated()) {
      this.userControllerService.user().subscribe((response) => {
        console.debug("Received updated user from database");
      });
    }
  }

}
