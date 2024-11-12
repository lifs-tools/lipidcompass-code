import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpStatusCode } from "@angular/common/http";
import { Injectable, Optional } from "@angular/core";
import { Router } from "@angular/router";
import {
  OAuthModuleConfig,
  OAuthResourceServerErrorHandler,
  OAuthStorage,
} from "angular-oauth2-oidc";
import { Observable, throwError } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";
import { UserService } from "../_services/user.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authStorage: OAuthStorage,
    private errorHandler: OAuthResourceServerErrorHandler,
    @Optional() private moduleConfig: OAuthModuleConfig,
    private router: Router
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    let token = this.authStorage.getItem("access_token");
    if (token) {
      // console.debug("Using authenticated http request: ", JSON.stringify(token));
      let header = "Bearer " + token;
      let headers = req.headers.set("Authorization", header);
      req = req.clone({ headers });
    } else {
      // console.debug("Not using authenticated http request!")
    }
    return next.handle(req).pipe(
      map((event: HttpEvent<any>) => {
        return event;
      }),
      catchError(
        (
          httpErrorResponse: HttpErrorResponse,
          _: Observable<HttpEvent<any>>
        ) => {
          if (httpErrorResponse.status === HttpStatusCode.Unauthorized || httpErrorResponse.status === HttpStatusCode.Forbidden) {
            // this.userService.logout();
            this.router.navigate(["login"]);
          }
          return throwError(() => httpErrorResponse);
        }
      )
    );
  }
}
