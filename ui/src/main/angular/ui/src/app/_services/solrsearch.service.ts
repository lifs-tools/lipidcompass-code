import { Component, Injectable, OnDestroy } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";

import { Observable, Subscription, Subject } from "rxjs";
import {
  NotificationService,
  BackendMessage,
  ServiceMessage,
} from "./notification.service";
import { UserService } from "./user.service";
import { deployment } from "../../environments/environment";

export class SolrQueryParams {
  constructor(
    readonly q: string,
    readonly field: string,
    readonly highlight: boolean,
    readonly highlight_field: string
  ) {}
}

export class SolrResponseHeader {
  constructor(
    readonly zkConnected: boolean,
    readonly status: number,
    readonly QTime: number,
    readonly params: object
  ) {}
}

export class SolrResponseBody {
  constructor(
    readonly numFound: number,
    readonly start: number,
    readonly docs: Array<object>
  ) {}
}

export class SolrResponseHighlight {
  constructor(readonly highlights: object) {}
}

export class SolrSearchResponse {
  constructor(
    readonly responseHeader: SolrResponseHeader,
    readonly response: SolrResponseBody,
    readonly highlighting: SolrResponseHighlight
  ) {}
}

export class SolrResultItem {
  constructor(
    readonly itemId: number,
    readonly name: string,
    readonly type: string,
    readonly identified: boolean
  ) {}
}

@Injectable()
export class SolrsearchService {
  private solrUrl = deployment.searchPath;

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService,
    private userService: UserService
  ) {}

  health() {
    // this.userService.isAuthenticated$.subscribe((observer) => {
    //   if (observer) {
    //     console.debug("Checking backend health status!");
    //     this.http
    //       .get<any>(
    //         this.solrUrl +
    //           "/literature/admin/ping?wt=json&distrib=true&indent=true"
    //       )
    //       .toPromise()
    //       .then((health) => {
    //         if (health["status"] !== "OK") {
    //           this.notificationService.publish(
    //             "messages",
    //             "error",
    //             'SOLR collection "literature" unavailable!',
    //             "Response status: " + health["status"]
    //           );
    //         } else {
    //           // this.notificationService.publish('messages',
    //           //   'success',
    //           //   'SOLR collection "literature" available!',
    //           //   'Response status: ' + health['status']
    //           // );
    //         }
    //       })
    //       .catch((error) => {
    //         this.notificationService.publish(
    //           "messages",
    //           "error",
    //           "SOLR unreachable!",
    //           "Response status: " + error["status"]
    //         );
    //       });
    //   } else {
    //     console.debug("Skipping backend health check for unauthenticated user!");
    //   }
    // });
  }

  search(query: string): Promise<SolrSearchResponse> {
    const queryParams = new SolrQueryParams(
      query,
      "title,author,doi,keywords,subject,date,created,creation_date",
      false,
      "text"
    );
    console.debug("QueryParams: " + JSON.stringify(queryParams));
    return this._search(this.solrUrl + "/literature/select", queryParams);
  }

  private _search(
    path: string,
    queryParams: SolrQueryParams
  ): Promise<SolrSearchResponse> {
    return this.http
      .get<SolrSearchResponse>(path, {
        params: this.buildHttpParams(queryParams),
      })
      .toPromise()
      .then((data) => data)
      .catch((error) => {
        this.notificationService.publishMessage("messages", <ServiceMessage>{
          level: "ALERT",
          title: "Problem while running search",
          content: this.formatError(error),
        });
        return error;
      });
  }

  private buildHttpParams(queryParams: SolrQueryParams): HttpParams {
    const httpParams = new HttpParams()
      .append("q", queryParams.q)
      .append("fl", queryParams.field)
      .append("hl", queryParams.highlight + "")
      .append("hl.fl", queryParams.highlight_field);
    console.debug("HTTP params: " + JSON.stringify(httpParams));
    return httpParams;
  }

  private formatError(error: any): string {
    return (
      "Response status: " +
      error["status"] +
      "\nMessage: " +
      error["statusText"]
    );
  }
}
