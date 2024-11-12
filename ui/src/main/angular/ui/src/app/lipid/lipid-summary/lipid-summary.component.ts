import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  ViewEncapsulation,
} from "@angular/core";
import {
  ActivatedRoute,
  NavigationEnd,
  NavigationStart,
  Router,
} from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import { Observable, Subscription } from "rxjs";
import { filter, map } from "rxjs/operators";
import { Lipid, LipidControllerService } from "../../../../modules/lipidcompass-backend-client";
import { SelectionService } from "../../_services/selection.service";

@Component({
  selector: "app-lipid-summary",
  templateUrl: "./lipid-summary.component.html",
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  styleUrls: ["./lipid-summary.component.css"],
})
export class LipidSummaryComponent implements OnInit, OnDestroy {
  selectedItem: Lipid;
  selectedItemSummary: Lipid;
  lipidId: Observable<String>;
  lipid: Observable<Lipid>;

  breadcrumbs = Array<Lipid>();

  private subscription: Subscription;

  chartData: any;

  constructor(
    translate: TranslateService,
    private lipidController: LipidControllerService,
    private selectionService: SelectionService<Lipid>,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang("en");
    // this.lipidId = route.snapshot.params.id;
    this.selectedItem = <Lipid>{};
    this.selectedItemSummary = <Lipid>{};
    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use("en");
    this.lipidId = route.params.pipe(map(params => params.id));
    this.lipidId.pipe(map((id: string) => this.lipidController.getById(id))).subscribe(res => {this.lipid = res;});
    // router.events
    //   .pipe(
    //     filter((event) => event instanceof NavigationEnd)
    //     // now query the activated route
    //     // map(() => this.route),
    //     // filter((route: ActivatedRoute) => route.outlet === "primary")
    //   )
    //   .subscribe((route: ActivatedRoute) => {
    //     if (route.snapshot) {
    //       this.lipidId = route.snapshot.params.id;
    //       console.debug("Activated Route: " + JSON.stringify(route.component));
    //     }
    //   });
  }

  // private rootRoute(route: ActivatedRoute): ActivatedRoute {
  //   // while (route.firstChild) {
  //   //   route = route.firstChild;
  //   // }
  //   console.debug("Returning route: ", JSON.stringify(route.pathFromRoot));
  //   return route;
  // }

  handleSelection(res: Lipid) {
    // if (res && res.data) {
    //   this.breadcrumbs = [];
    //   this.selectedItem = <Lipid>res;
    //   console.debug('Before: ' + JSON.stringify(this.breadcrumbs));
    //   this.breadcrumbs = this.fillBreadCrumbs(res);
    //   console.debug('After: ' + JSON.stringify(this.breadcrumbs));
    //   if (this.lipidNodeService) {
    //     this.lipidNodeService.summaryFor(res).subscribe(summaryNode => {
    //       if (summaryNode) {
    //         this.setSummary(summaryNode);
    //       }
    //     });
    //   }
    // } else {
    //   console.debug('Received an invalid TreeItem! ' + JSON.stringify(res));
    //   this.selectedItem = {};
    //   this.selectedItemSummary = {};
    //   this.chartData = {};
    //   this.breadcrumbs = [];
    // }
  }

  setSummary(node: Lipid) {
    // this.selectedItemSummary = node;
    // if (this.selectedItemSummary['data']['content']['isotopes']) {
    //   console.debug(
    //     JSON.stringify(this.selectedItemSummary['data']['content']['isotopes'])
    //   );
    //   this.chartData = this.createChartData(
    //     this.selectedItemSummary['data']['content']['isotopes']
    //   );
    // }
  }

  ngOnInit() {
    this.breadcrumbs = [];
    this.subscription = this.selectionService.selection$.subscribe(
      (next) => {
        this.handleSelection(next);
      },
      (error) => {
        console.debug(
          "Caught error while receiving selection event: " +
            JSON.stringify(error)
        );
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  // getParent(child: Lipid): Lipid {
  //   if (child['parent']) {
  //     return child['parent'];
  //   }
  //   return undefined;
  // }

  fillBreadCrumbs(child: Lipid): Array<Lipid> {
    const parents = new Array<Lipid>();
    // let done = false;
    // var node = child;
    // parents.push({
    //   label: node.label,
    //   icon: node.icon,
    //   url: node.data._links.self.href
    // });
    // parents.push(this.getParent(node));
    // console.debug('start filling breadcrumbs');
    // while (!done) {
    //   console.debug('Node: ' + JSON.stringify(node.label));
    //   var parentNode = this.getParent(node);
    //   if (parentNode && parentNode['label'] !== 'Root') {
    //     // var link = null;
    //     // if (
    //     //   parentNode.data &&
    //     //   parentNode.data._links &&
    //     //   parentNode.data._links.self
    //     // ) {
    //     //   link = parentNode.data._links.self.href;
    //     // }
    //     // TODO add routerLink to navigate back to the parents summary view
    //     // parents.push({
    //     //   label: parentNode.label,
    //     //   icon: parentNode.icon,
    //     //   url: link
    //     // });
    //     parents.push(parentNode);
    //     node = parentNode;
    //   } else {
    //     done = true;
    //   }
    //   // if (node['parent']) {
    //   //   const snode = node['parent'];
    //   //   if (snode) {
    //   //     var link = null;
    //   //     if (snode.data && snode.data._links) {
    //   //       link = snode.data._links.self.href;
    //   //     }
    //   //     // TODO add routerLink to navigate back to the parents summary view
    //   //     parents.push({
    //   //       label: snode.label,
    //   //       icon: snode.icon,
    //   //       url: link
    //   //     });
    //   //   }
    //   // } else {
    //   //   done = true;
    //   // }
    // }
    // console.debug('breadcrumbs: ' + JSON.stringify(parents));
    // console.debug('end filling breadcrumbs');
    return parents.reverse();
  }

  createChartData(data: Array<any>) {
    // selectedItemSummary['data'][key]
    // mass, intensity
    const chartLabels = new Array<any>();
    const chartDatasetData = new Array<any>();
    data.forEach((element) => {
      chartLabels.push(element["mass"]);
      chartDatasetData.push(element["intensity"]);
    });
    const chartData = {
      labels: chartLabels,
      datasets: [
        {
          label: "Isotopes",
          data: chartDatasetData,
        },
      ],
    };
    console.debug(JSON.stringify(chartData));
    return chartData;
  }

  onSelect(event: any) {
    if (event) {
      console.debug(event);
    }
  }
}
