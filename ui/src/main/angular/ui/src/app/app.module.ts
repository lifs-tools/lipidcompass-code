// import environment
// import angular apis
import {
  CommonModule,
  DecimalPipe,
  LocationStrategy,
  PathLocationStrategy,
} from "@angular/common";
import { HttpClient, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi, withXsrfConfiguration } from "@angular/common/http";
import { CUSTOM_ELEMENTS_SCHEMA, Injectable, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
// import ngx-translate for i18n support
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { OAuthModule } from "angular-oauth2-oidc";
import { PlotlyViaWindowModule } from "angular-plotly.js";
// primeng module imports
import { AccordionModule } from "primeng/accordion";
import { ConfirmationService, MessageService } from "primeng/api";
import { AutoCompleteModule } from "primeng/autocomplete";
import { BreadcrumbModule } from "primeng/breadcrumb";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { CheckboxModule } from "primeng/checkbox";
import { ChipModule } from "primeng/chip";
import { ChipsModule } from "primeng/chips";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { DataViewModule } from "primeng/dataview";
import { DropdownModule } from "primeng/dropdown";
import { FileUploadModule } from "primeng/fileupload";
import { InputNumberModule } from 'primeng/inputnumber';
import { InputGroupModule} from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { ListboxModule } from "primeng/listbox";
import { MenubarModule } from "primeng/menubar";
import { MessageModule } from "primeng/message";
import { MessagesModule } from "primeng/messages";
import { MultiSelectModule } from "primeng/multiselect";
import { PanelModule } from "primeng/panel";
import { PanelMenuModule } from "primeng/panelmenu";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { RatingModule } from "primeng/rating";
import { ScrollTopModule } from "primeng/scrolltop";
import { StepsModule } from "primeng/steps";
import { TableModule } from "primeng/table";
import { TabMenuModule } from "primeng/tabmenu";
import { TabViewModule } from "primeng/tabview";
import { ToastModule } from "primeng/toast";
import { TreeModule } from "primeng/tree";
import { VirtualScrollerModule } from "primeng/virtualscroller";
// import lipidcompass apis
import {
  LipidCompassBackendApiModule,
  LipidCompassBackendConfiguration,
} from "../../modules/lipidcompass-backend-client";
import {
  LipidCompassDataImporterApiModule,
  LipidCompassDataImporterConfiguration,
} from "../../modules/lipidcompass-data-importer-client";
import {
  LipidCompassSearchApiModule,
  LipidCompassSearchConfiguration,
} from "../../modules/lipidcompass-search-client";
import {
  MzTabValidationApiModule,
  MzTabValidationConfiguration,
} from "../../modules/mztabm-validator-client";
import { deployment, environment } from "../environments/environment";
import { AdminControlledVocabularyComponent } from "./admin/admin-controlledvocabularies/admin-controlledvocabularies.component";
import { AdminCrossReferencesComponent } from "./admin/admin-crossreferences/admin-crossreferences.component";
import { AdminCvParametersComponent } from "./admin/admin-cvparameters/admin-cvparameters.component";
import { AdminDataimportJobsComponent } from "./admin/admin-dataimport-jobs/admin-dataimport-jobs.component";
import { AdminDataimportSubmissionComponent } from "./admin/admin-dataimport-submission/admin-dataimport-submission.component";
import { AdminLipidComponent } from "./admin/admin-lipid/admin-lipid.component";
import { AdminLipidMapsComponent } from "./admin/admin-lipidmaps/admin-lipidmaps.component";
import { AdminLipidquantityComponent } from "./admin/admin-lipidquantity/admin-lipidquantity.component";
import { AdminMztabresultComponent } from "./admin/admin-mztabresult/admin-mztabresult.component";
import { AdminStudyComponent } from "./admin/admin-study/admin-study.component";
import { AdminSwissLipidsComponent } from "./admin/admin-swisslipids/admin-swisslipids.component";
import { AdminComponent } from "./admin/admin.component";
// custom project components
import { AppComponent } from "./app.component";
import { DataSubmitComponent } from "./data-submit/data-submit.component";
import { DefineStudyComponent } from "./data-submit/define-study/define-study.component";
import { SubmissionComponent } from "./data-submit/submission/submission.component";
import { UploadComponent } from "./data-submit/upload/upload.component";
import { ValidationComponent } from "./data-submit/validation/validation.component";
import { DatasetCardComponent } from "./dataset/dataset-card/dataset-card.component";
import { DatasetCompareComponent } from "./dataset/dataset-compare/dataset-compare.component";
import { DatasetPlotDonutComponent } from "./dataset/dataset-plot-donut-counts/dataset-plot-donut.component";
import { DatasetDetailComponent } from "./dataset/dataset-detail/dataset-detail.component";
import { DatasetComponent } from "./dataset/dataset.component";
import { DbStatsComponent } from "./layouts/db-stats/db-stats.component";
import { DbCompositionComponent } from "./layouts/db-composition/db-composition.component";
import { PageNotFoundComponent } from "./errors/pageNotFound/pageNotFound.component";
import { HelpComponent } from "./help/help.component";
import { HomeComponent } from "./home/home.component";
import { InfoComponent } from "./info/info.component";
import { ActivityIndicatorComponent } from "./layouts/activity-indicator/activity-indicator.component";
import { CrudTableComponent } from "./layouts/crud-table/crud-table.component";
import { FacetSearchComponent } from "./layouts/facet-search/facet-search.component";
import { FooterComponent } from "./layouts/footer/footer.component";
import { MainMenuSearchComponent } from "./layouts/main-menu-search/main-menu-search.component";
import { MainComponent } from "./layouts/main/main.component";
import { MoleculeViewComponent } from "./layouts/molecule-view/molecule-view.component";
import { SvgViewComponent } from "./layouts/svg-view/svg-view.component";
import { LipidCardComponent } from "./lipid/lipid-card/lipid-card.component";
import { LipidLiteratureSearchComponent } from "./lipid/lipid-literature-search/lipid-literature-search.component";
import { LipidMsSearchComponent } from "./lipid/lipid-ms-search/lipid-ms-search.component";
import { LipidQuantitySearchComponent } from "./lipid/lipid-quantity-search/lipid-quantity-search.component";
import { LipidSearchComponent } from "./lipid/lipid-search/lipid-search.component";
import { LipidSummaryComponent } from "./lipid/lipid-summary/lipid-summary.component";
// components
import { LipidTreeComponent } from "./lipid/lipid-tree/lipid-tree.component";
import { LipidOfTheDayComponent } from "./lipid/lipid-of-the-day/lipid-of-the-day.component";
import { LoginComponent } from "./login/login.component";
import { UserCardComponent } from "./login/user-card/user-card.component";
import { MztabComponent } from "./mztab/mztab.component";
import { StudyCardComponent } from "./study/study-card/study-card.component";
import { StudyDetailComponent } from "./study/study-detail/study-detail.component";
import { StudyComponent } from "./study/study.component";
// import { SubmitComponent } from './submit/submit.component';
import { AuthInterceptor } from "./_helpers/auth-interceptor";
import { HasRoleGuard } from "./_helpers/hasrole-guard";
import { EllipsisPipe } from "./_pipes/ellipsis.pipe";
// pipes
import { KeysPipe } from "./_pipes/keys.pipe";
import { OrderByPipe } from "./_pipes/orderBy.pipe";
import { HttperrorhandlerService } from "./_services/httperrorhandler.service";
// services
import { LipidNodeService } from "./_services/lipidnode.service";
import { LipidsearchService } from "./_services/lipidsearch.service";
import { NotificationService } from "./_services/notification.service";
import { SelectionService } from "./_services/selection.service";
import { SolrsearchService } from "./_services/solrsearch.service";
import { UserService } from "./_services/user.service";
import { MatomoConsentMode, MatomoModule, MatomoRouterModule } from "ngx-matomo-client";
import { TrackingConsentComponent } from "./tracking-consent/tracking-consent.component";
import { CardGridComponent } from "./layouts/card-grid/card-grid.component";
import { DatasetCorrectionsComponent } from "./dataset/dataset-corrections/dataset-corrections.component";
import { DatasetFiltersComponent } from "./dataset/dataset-filters/dataset-filters.component";
import { CvparameterClassificationComponent } from "./layouts/cvparameter-classification/cvparameter-classification.component";
// import { TrackingConsentComponent } from './tracking-consent/tracking-consent.component';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyPrimeNGModule } from '@ngx-formly/primeng';
import { RepeatTypeComponent } from './formly-components/repeat-type/repeat-type.component';
import { FormlyFormFieldModule } from "@ngx-formly/primeng/form-field";
import { FormlyInputModule } from "@ngx-formly/primeng/input";
import { FormlyTextAreaModule } from "@ngx-formly/primeng/textarea";
import { FormlyRadioModule } from "@ngx-formly/primeng/radio";
import { FormlyCheckboxModule } from "@ngx-formly/primeng/checkbox";
import { FormlySelectModule } from "@ngx-formly/core/select";
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';
import { OlslinkComponent } from './olslink/olslink.component';
import { StatsCardComponent } from "./layouts/stats-card/stats-card.component";
import { StatsCardGridComponent } from "./layouts/stats-card-grid/stats-card-grid.component";

// PlotlyViaCDNModule.setPlotlyVersion("1.58.3");
// PlotlyViaCDNModule.setPlotlyVersion("2.12.1");

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, "./assets/i18n/", ".json");
}

// @Injectable()
// export class XhrInterceptor implements HttpInterceptor {
//   intercept(req: HttpRequest<any>, next: HttpHandler) {
//     const xhr = req.clone({
//       headers: req.headers.set("X-Requested-With", "XMLHttpRequest"),
//     });
//     return next.handle(xhr);
//   }
// }

// Configure application routes, responsible components, activation guards and login requirements
const appRoutes: Routes = [
  {
    path: "home",
    component: HomeComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: false,
      role: "user",
      featureEnabled: environment.features.home,
    },
  },
  // {
  //   path: 'lipid',
  //   component: LipidTreeComponent,
  //   canActivate: [IsAuthenticatedGuard],
  //   data: { requiresLogin: true },
  // },
  {
    path: "lipid/:id",
    component: LipidSummaryComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.lipid,
    },
  },
  {
    path: "search",
    component: LipidSearchComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.search,
    },
  },
  {
    path: "facetsearch",
    component: FacetSearchComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.facetsearch,
    },
  },
  {
    path: "mssearch",
    component: LipidMsSearchComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.mssearch,
    },
  },
  {
    path: "study",
    component: StudyComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.study,
    },
  },
  {
    path: "study/:id",
    component: StudyDetailComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.study,
    },
  },
  {
    path: "dataset",
    component: DatasetComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.dataset,
    },
  },
  {
    // must appear before dataset/:id
    path: "dataset/compare",
    component: DatasetCompareComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.dataset,
    },
  },
  {
    path: "dataset/:id",
    component: DatasetDetailComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.dataset,
    },
  },
  {
    path: "dataset/pid/:nativeId",
    component: DatasetDetailComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.dataset,
    },
  },
  {
    path: "quantities",
    component: LipidQuantitySearchComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.quantities,
    },
  },
  {
    path: "submit",
    component: DataSubmitComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.submit,
    },
    children: [
      {
        path: "submit",
        component: DataSubmitComponent,
        // redirectTo: "submit",
        pathMatch: "full",
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "user",
          featureEnabled: environment.features.submit,
        },
      },
      {
        path: "upload/:id",
        component: UploadComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "user",
          featureEnabled: environment.features.submit,
        },
      },
      {
        path: "validation/:id",
        component: ValidationComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "user",
          featureEnabled: environment.features.submit,
        },
      },
      {
        path: "study/:id",
        component: DefineStudyComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "user",
          featureEnabled: environment.features.submit,
        },
      },
      {
        path: "submission/:id",
        component: SubmissionComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "user",
          featureEnabled: environment.features.submit,
        },
      },
    ],
  },
  {
    path: "mztab",
    component: MztabComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.mztab,
    },
  },
  {
    path: "literature",
    component: LipidLiteratureSearchComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.literature,
    },
  },
  {
    path: "help",
    component: HelpComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.help,
    },
  },
  {
    path: "info",
    component: InfoComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "user",
      featureEnabled: environment.features.info,
    },
  },
  {
    path: "admin",
    component: AdminComponent,
    canActivate: [HasRoleGuard],
    data: {
      requiresLogin: true,
      role: "admin",
      featureEnabled: environment.features.admin,
    },
    children: [
      {
        path: "controlledVocabularies",
        component: AdminControlledVocabularyComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "crossReferences",
        component: AdminCrossReferencesComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "cvParameters",
        component: AdminCvParametersComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "lipids",
        component: AdminLipidComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "lipidQuantities",
        component: AdminLipidquantityComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "mzTabResults",
        component: AdminMztabresultComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "studies",
        component: AdminStudyComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "lipidMapsEntries",
        component: AdminLipidMapsComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "swissLipidsEntries",
        component: AdminSwissLipidsComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "dataImport/submissions",
        component: AdminDataimportSubmissionComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
      {
        path: "dataImport/jobs",
        component: AdminDataimportJobsComponent,
        canActivate: [HasRoleGuard],
        data: {
          requiresLogin: true,
          role: "admin",
          featureEnabled: environment.features.admin,
        },
      },
    ],
  },
  {
    path: "login",
    component: LoginComponent,
    data: {
      requiresLogin: false,
      featureEnabled: environment.features.login,
    },
  },
  { path: "", redirectTo: "home", pathMatch: "full" },
  { path: "**", component: PageNotFoundComponent },
];

@NgModule({ declarations: [
        AppComponent,
        ActivityIndicatorComponent,
        AdminComponent,
        AdminControlledVocabularyComponent,
        AdminCrossReferencesComponent,
        AdminCvParametersComponent,
        AdminDataimportJobsComponent,
        AdminDataimportSubmissionComponent,
        AdminLipidComponent,
        AdminLipidMapsComponent,
        AdminLipidquantityComponent,
        AdminMztabresultComponent,
        AdminStudyComponent,
        AdminSwissLipidsComponent,
        CardGridComponent,
        CrudTableComponent,
        CvparameterClassificationComponent,
        DatasetCardComponent,
        DatasetCompareComponent,
        DatasetComponent,
        DatasetCorrectionsComponent,
        DatasetDetailComponent,
        DatasetFiltersComponent,
        DatasetPlotDonutComponent,
        DataSubmitComponent,
        DbCompositionComponent,
        DbStatsComponent,
        DefineStudyComponent,
        EllipsisPipe,
        FacetSearchComponent,
        FooterComponent,
        HelpComponent,
        HomeComponent,
        InfoComponent,
        KeysPipe,
        LipidCardComponent,
        LipidLiteratureSearchComponent,
        LipidMsSearchComponent,
        LipidQuantitySearchComponent,
        LipidSearchComponent,
        LipidSummaryComponent,
        LipidTreeComponent,
        LipidOfTheDayComponent,
        LoginComponent,
        MainComponent,
        MainMenuSearchComponent,
        MoleculeViewComponent,
        MztabComponent,
        OlslinkComponent,
        OrderByPipe,
        PageNotFoundComponent,
        RepeatTypeComponent,
        StatsCardComponent,
        StatsCardGridComponent,
        StudyCardComponent,
        StudyComponent,
        StudyDetailComponent,
        SubmissionComponent,
        SvgViewComponent,
        TrackingConsentComponent,
        UploadComponent,
        UserCardComponent,
        ValidationComponent
    ],
    exports: [KeysPipe, EllipsisPipe, OrderByPipe],
    bootstrap: [AppComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA], imports: [LipidCompassBackendApiModule.forRoot(() => {
            return new LipidCompassBackendConfiguration({
                basePath: deployment.backendPath,
            });
        }),
        LipidCompassDataImporterApiModule.forRoot(() => {
            return new LipidCompassDataImporterConfiguration({
                basePath: deployment.dataImporterPath,
            });
        }),
        LipidCompassSearchApiModule.forRoot(() => {
            return new LipidCompassSearchConfiguration({
                basePath: deployment.searchPath,
            });
        }),
        MzTabValidationApiModule.forRoot(() => {
            return new MzTabValidationConfiguration({
                basePath: deployment.validationPath,
            });
        }),
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        RouterModule.forRoot(appRoutes, { enableTracing: environment.debug } // <-- debugging purposes only
        ),
        ToastModule,
        FormsModule,
        ReactiveFormsModule,
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: environment.allowedUrls,
                sendAccessToken: true,
            },
        }),
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient],
            },
        }),
        AccordionModule,
        AutoCompleteModule,
        ConfirmDialogModule,
        FileUploadModule,
        TreeModule,
        TableModule,
        TabViewModule,
        TabMenuModule,
        BreadcrumbModule,
        MenubarModule,
        MessagesModule,
        MessageModule,
        InputGroupModule,
        InputGroupAddonModule,
        InputNumberModule,
        InputTextModule,
        InputTextareaModule,
        DropdownModule,
        ListboxModule,
        ChipModule,
        ChipsModule,
        ConfirmDialogModule,
        ButtonModule,
        MultiSelectModule,
        CardModule,
        PanelMenuModule,
        PanelModule,
        DataViewModule,
        CheckboxModule,
        RatingModule,
        StepsModule,
        PlotlyViaWindowModule,
        FontAwesomeModule,
        ScrollTopModule,
        ProgressSpinnerModule,
        VirtualScrollerModule,
        // MarkdownModule.forRoot({ loader: HttpClient }),
        MatomoModule.forRoot({
            trackerUrl: "https://lifs-tools.org/matomo",
            siteId: "13",
            acceptDoNotTrack: true,
            requireConsent: MatomoConsentMode.COOKIE,
            disabled: !environment.trackingEnabled,
        }),
        MatomoRouterModule,
        FormlyFormFieldModule,
        FormlyInputModule,
        FormlyTextAreaModule,
        FormlyRadioModule,
        FormlyCheckboxModule,
        FormlySelectModule,
        FormlyPrimeNGModule,
        FormlyModule.forRoot({
            types: [{ name: 'repeat', component: RepeatTypeComponent }],
            validationMessages: [{ name: 'required', message: 'This field is required' }],
        }),
        FormlyModule.forRoot(),
        FormlyBootstrapModule], providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: HttperrorhandlerService,
            multi: true,
        },
        HasRoleGuard,
        { provide: LocationStrategy, useClass: PathLocationStrategy },
        DecimalPipe,
        SelectionService,
        LipidNodeService,
        LipidsearchService,
        SolrsearchService,
        NotificationService,
        MessageService,
        ConfirmationService,
        UserService,
        provideHttpClient(withInterceptorsFromDi(), withXsrfConfiguration({
            cookieName: "XSRF-TOKEN",
            headerName: "X-CSRF-TOKEN",
        })),
    ] })
export class AppModule {}
