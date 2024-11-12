// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

import { AuthConfig } from "angular-oauth2-oidc";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import { LipidCompassQuery } from "../../modules/lipidcompass-backend-client";
import { LipidLevel } from "../app/_models/lipidLevel";
import { defaultColorScales } from "./common";

export const environment = {
  applicationName: "LipidCompass",
  production: false,
  trackingEnabled: true,
  globalLogin: true,
  debug: true,
  allowedUrls: [
    "http://localhost:4200/",
    "http://localhost:8081/",
    "http://localhost:8088/",
    "http://localhost:8090/",
    "http://localhost:28080/",
  ],
  contactUrl: "https://lifs-tools.org/support.html",
  privacyUrl: "https://lifs-tools.org/imprint-privacy-policy.html",

  features: {
    about: true,
    dataset: true,
    facetsearch: true,
    help: true,
    home: true,
    info: true,
    lipid: true,
    literature: true,
    login: true,
    mssearch: true,
    search: true,
    study: true,
    submit: true,
    mztab: true,
    quantities: true,
    admin: true,
    curate: false
  },
};

export const deployment = {
  path: "/",
  gatewayPath: "http://localhost:8090",
  backendPath: "http://localhost:8090/lipidcompass-backend",
  searchPath: "http://localhost:8090/lipidcompass-solr",
  dataImporterPath: "http://localhost:8090/lipidcompass-data-importer",
  validationPath: "https://apps.lifs-tools.org/mztabvalidator/rest/v2/"
};

export const paging = {
  defaultPageable: {
    page: 0,
    size: 25,
  },
};

export const colorScales = defaultColorScales;

export const queries = {
  defaultLipidCompassQuery: <LipidCompassQuery>{
    lipidLevel: LipidLevel.SPECIES,
    names: [], 
    matchMode: LipidCompassQuery.MatchModeEnum.PREFIX,
    normalizeName: false,
    unit: "",
    mzRange: [],
    quantityRange: [],
    mzTabResults: [],
    facets: [],
    selectedFacets: [],
  }
}

export const plotlyConfig: Plotly.Config = {
  displaylogo: false,
  toImageButtonOptions: {
    format: "svg", // one of png, svg, jpeg, webp
    filename: "plot",
    width: 800,
    height: 600,
    scale: 1, // Multiply title/legend/axis/canvas sizes by this factor
  },
};

export const lipidClassRangesPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Total Lipid Concentrations on Class Level",
  yaxis: {
    title: "Lipid Class",
    zeroline: true,
    // categoryorder: "category descending"
    showticklabels: false
  },
  xaxis: {
    title: "Summed Concentration",
    type: "log",
    zeroline: true,
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
};

export const lipidCategoryCompositionPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Lipid Counts on Category Level",
  // yaxis: {
  //   title: "# in DB",
  //   zeroline: true,
  // },
  yaxis: {
    title: "Lipid Class",
    zeroline: true,
    categoryorder: "array",
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
};

export const lipidCategoryConcentrationPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Lipid Concentrations on Category Level",
  // yaxis: {
  //   title: "# in DB",
  //   zeroline: true,
  // },
  yaxis: {
    title: "Lipid Class",
    zeroline: true,
    categoryorder: "array",
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
};

export const lipidSpeciesRangesPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Lipid Concentrations on Species Level",
  // yaxis: {
  //   title: "# in DB",
  //   zeroline: true,
  // },
  yaxis: {
    title: "Lipid Species",
    zeroline: true,
    categoryorder: "array",
    showticklabels: false
  },
  xaxis: {
    title: "Summed Concentration",
    type: 'log',
    autorange: true
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
};

export const lipidSpeciesCorrelationPlotLayout: Plotly.Layout = {
  autosize: false,
  title: "Log10 Lipid Concentrations on Species Level (Q25 - Q50 - Q75)",
  // yaxis: {
  //   title: "# in DB",
  //   zeroline: true,
  // },
  xaxis: {
    title: "Log10 Concentration in",
    type: 'log',
    autorange: true
  },
  yaxis: {
    title: "Log10 Concentration in",
    zeroline: true,
    type: 'log',
    autorange: true,
    matches: 'x'
  },
  height: 650,
  width: 850,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
}

export const authCodeFlowConfig: AuthConfig = {
  // Url of the Identity Provider
  issuer: "http://localhost:28080/auth/realms/lifs",

  // URL of the SPA to redirect the user to after login
  //redirectUri: window.location.origin + 'index.html',
  // /#/index.html
  redirectUri: window.location.origin, //window.location.origin, // + '/home',

  // The SPA's id. The SPA is registered with this id at the auth-server
  // clientId: 'server.code',
  clientId: "lipidcompass-ui",

  // Just needed if your auth server demands a secret. In general, this
  // is a sign that the auth server is not configured with SPAs in mind
  // and it might not enforce further best practices vital for security
  // such applications.
  // dummyClientSecret: 'secret',

  responseType: "code",

  useSilentRefresh: false,

  // set the scope for the permissions the client should request
  // The first four are defined by OIDC.
  // Important: Request offline_access to get a refresh token
  // The api scope is a usecase specific one
  scope: "openid profile email roles offline_access",

  oidc: true,

  // use this for testing and development only
  requireHttps: false,

  clearHashAfterLogin: false,

  sessionChecksEnabled: true,

  showDebugInformation: false,
};

export const submissionWorkflowConfig = {
  parentRoute: ['/submit'],
  workflows: {
    manual: {
      name: "Manual Workflow",
      description: "Enter study and result information manually.",
      enabled: true,
      steps: [
        {label: 'Study', routerLink: 'study'},
        {label: 'Upload', routerLink: 'upload'},
        {label: 'Validation', routerLink: 'validation'},
        {label: 'Submission', routerLink: 'submission'}
      ]
    },
    mzTab: {
      name: "mzTab-M Workflow",
      description: "Upload or provide URIs to mzTab-M files for submission.",
      enabled: true,
      steps: [
        {label: 'Study', routerLink: 'study'},
        {label: 'Upload', routerLink: 'upload'},
        {label: 'Validation', routerLink: 'validation'},
        {label: 'Submission', routerLink: 'submission'}
      ]
    }
  }
};
