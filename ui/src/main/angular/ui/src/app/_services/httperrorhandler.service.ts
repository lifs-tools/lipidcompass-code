import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, EMPTY, Observable, throwError } from "rxjs";
import {
  NotificationService,
  Publisher,
  ServiceMessage,
} from "./notification.service";
// import { UserService } from './user.service';

@Injectable({
  providedIn: "root",
})
export class HttperrorhandlerService implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status == 401) {
          return EMPTY;
        }
        this.handleError("HttpErrorHandler", "messages", error, request);
        return throwError(error);
      })
    );
  }

  constructor(private notificationService: NotificationService) {}

  public handleError(
    sourceName: string,
    topic: string,
    error: HttpErrorResponse,
    request: HttpRequest<any>
  ) {
    var errorMessage = "";
    if (error.error) {
      // A client-side or network error occurred. Handle it accordingly.
      if (error.error.message) {
        console.error("An error occurred:", error.error.message);
        errorMessage = error.error.message + "\nURL:" + request.url;
      } else if (error.error.error_description) {
        console.error("An error occurred:", error.error.error_description);
        errorMessage = error.error.error_description + "\nURL:" + request.url;
      }
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Service ${sourceName} returned code ${error.status}, ` +
          `body was: ${JSON.stringify(error.error)}`
      );
      errorMessage = error["statusText"];
    }
    var level = "error";
    var title = "Error";
    if (errorMessage === "") {
      if (error["status"] === 200) {
        errorMessage =
          "Database is empty" +
          ".\nPlease import data or try again later!";
        level = "info";
        title = "Info"
      } else if (error["status"] === 404) {
        errorMessage =
          "Backend service or resource unavailable for " +
          request.url +
          ".\nPlease try again later!";
        level = "warn";
        title = "Warning"
      } else if (error["status"] === 504) {
        errorMessage =
          "Backend service or resource unavailable for " +
          request.url +
          ".\nPlease try again later!";
        level = "warn";
        title = "Warning"
      }
    } 

    // Return an observable with a user-facing error message.
    this.notificationService.publish(
      topic,
      level,
      title,
      errorMessage
    );
  }
}
