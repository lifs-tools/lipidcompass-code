import {
  timer as observableTimer,
  Observable,
  Subscription,
  Subject,
  of,
} from "rxjs";
import { Component, Injectable, OnDestroy } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { TreeNode } from "primeng/api";
import { catchError, map, tap } from "rxjs/operators";
import { NotificationService, BackendMessage } from "./notification.service";
import { UserService } from "./user.service";
import { deployment } from "../../environments/environment";

interface CategorySummary {
  code: string;
  model: string;
  mainClasses: number;
  subClasses: number;
  species: number;
  faScanSpecies: number;
  subSpecies: number;
  annotatedIsomers: number;
  itemId: number;
  name: string;
  parent: any;
}

@Injectable()
export class LipidNodeService implements OnDestroy {
  private timer: Observable<number>;
  // Subscription object
  private sub: Subscription;

  private backendAvailable = new Subject<boolean>();

  private backendUrl = deployment.backendPath;

  backendAvailable$ = this.backendAvailable.asObservable();

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService,
    private userService: UserService
  ) {
    this.timer = observableTimer(200, 30000);
    // subscribing to a observable returns a subscription object
    this.sub = this.timer.subscribe((t) => this.health());
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private asTreeNode(parent: TreeNode | undefined, data: any): TreeNode {
    const node: TreeNode = {};
    node["data"] = data;
    const myLevel = data.content["level"];
    const levels = [];
    if (parent) {
      node["parent"] = parent;
    }
    node["label"] = data.content.name;
    node["children"] = this.asTreeNodeArray(node, data["_links"]["children"]);
    node["expandedIcon"] = "fa-folder-open";
    node["collapsedIcon"] = "fa-folder";
    node["leaf"] = false;
    return node;
  }

  private asErrorTreeNode(parent: TreeNode, data: any): TreeNode {
    if (parent) {
      parent["data"]["error"] = data;
      parent["expandedIcon"] = "fa-exclamation-circle";
      parent["collapsedIcon"] = "fa-exclamation-circle";
      parent["leaf"] = true;
      return parent;
    } else {
      return <TreeNode>{
        label: "Error",
        data: { error: data },
        expandedIcon: "fa-exclamation-circle",
        collapsedIcon: "fa-exclamation-circle",
        leaf: true,
      };
    }
  }

  private asTreeNodeArray(parent: TreeNode, data: any): TreeNode[] {
    const nodes: TreeNode[] = [];
    if (data) {
      console.debug(JSON.stringify(data));
      if (data["_embedded"] && data["_embedded"]["children"]) {
        for (const node of data["_embedded"]["children"]) {
          nodes.push(this.asTreeNode(parent, node));
        }
      } else if (
        data["_embedded"] &&
        data["_embedded"]["swissLipidsEntryList"]
      ) {
        for (const node of data["_embedded"]["swissLipidsEntryList"]) {
          nodes.push(this.asTreeNode(parent, node));
        }
      }
    }
    return nodes;
  }

  health(): void {
    // this.userService.isAuthenticated$.subscribe((observer) => {
    //   if (observer) {
    //     console.debug("Checking backend health status!");
    //     this.http
    //       .get<any>(this.backendUrl + "/actuator/health")
    //       .toPromise()
    //       .then((health) => {
    //         console.debug(JSON.stringify(health));
    //         if (health["status"] !== "UP") {
    //           this.notificationService.publish(
    //             "messages",
    //             "error",
    //             "Backend resource unavailable!",
    //             "Response status: " +
    //               health["status"] +
    //               "\nMessage: " +
    //               health["statusText"]
    //           );
    //           this.backendAvailable.next(false);
    //         } else {
    //           // this.notificationService.publish('messages',
    //           //   'success',
    //           //   'Backend resource available!',
    //           //   'Response status: ' +
    //           //   health['status'] +
    //           //   '\nMessage: ' +
    //           //   health['statusText']
    //           // );
    //           // // this.notificationService.info('Backend resource available!',
    //           // //    'Response status: ' + health['status'] + '\nMessage: ' + health['statusText']);
    //           this.backendAvailable.next(true);
    //         }
    //       })
    //       .catch((error) => {
    //         this.notificationService.publish(
    //           "messages",
    //           "error",
    //           "Gateway unreachable!",
    //           "Response status: " +
    //             error["status"] +
    //             "\nMessage: " +
    //             error["statusText"]
    //         );
    //         this.backendAvailable.next(false);
    //       });
    //   } else {
    //     console.debug("Skipping backend health check for unauthenticated user!");
    //   }
    // });
  }

  stats(): Observable<any> {
    return this.http.get<any>(this.backendUrl + "/lipids/stats");
  }

  summaryFor(eventNode: TreeNode): Observable<TreeNode> {
    return this.summary(eventNode);
  }

  childrenFor(eventNode: TreeNode, level: string) {
    return this.children(eventNode, level);
  }

  categories() {
    const node = {
      label: "Categories",
      data: {
        _links: { children: { href: this.backendUrl + "/lipids/levels" } },
      },
    };
    return this.childrenFor(<TreeNode>node, "CATEGORY");
  }

  private summary(node: TreeNode): Observable<TreeNode> {
    return this.http
      .get<TreeNode>(node.data["_links"]["self"]["href"])
      .pipe(map((response) => this.asTreeNode(node, response)));
  }

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.error(error); // log to console instead
      this.notificationService.publish(
        "notification.service.lipidnode",
        "error",
        "Node retrieval failed!",
        "Error: " + this.formatError(error)
      );
      return of(result as T);
    };
  }

  private children(
    parentNode: TreeNode,
    level: string
  ): Observable<TreeNode[]> {
    //const href = parentNode.data['_links']['children']['href'];
    return this.http
      .post<TreeNode[]>(this.backendUrl + "/lipids/levels", level)
      .pipe(map((response) => this.asTreeNodeArray(parentNode, response)));
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
