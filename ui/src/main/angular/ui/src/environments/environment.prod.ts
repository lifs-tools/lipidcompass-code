import { AuthConfig } from "angular-oauth2-oidc";
import { Plotly } from "angular-plotly.js/lib/plotly.interface";
import { LipidCompassQuery } from "../../modules/lipidcompass-backend-client";
import { LipidLevel } from "../app/_models/lipidLevel";
import { defaultColorScales } from "./common";
export const environment = {
  applicationName: "LipidCompass",
  production: true,
  trackingEnabled: true,
  globalLogin: true,
  debug: false,
  allowedUrls: [
    "http://127.0.0.1:8090",
    "http://localhost:8091",
    "http://lipidcompass-gateway:8090/",
    "https://lipidcompass.org/",
    "https://lifs-tools.org",
  ],
  contactUrl: "https://lifs-tools.org/support.html",
  privacyUrl: "https://lifs-tools.org/imprint-privacy-policy.html",
  features: {about: true,
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
    mztab: false,
    quantities: true,
    admin: true,
    curate: false
  },
};

export const deployment = {
  path: "https://lipidcompass.org/",
  gatewayPath: "https://lipidcompass.org/",
  backendPath: "https://lipidcompass.org/lipidcompass-backend",
  searchPath: "https://lipidcompass.org/lipidcompass-solr",
  dataImporterPath: "https://lipidcompass.org/lipidcompass-data-importer",
  validationPath: "https://apps.lifs-tools.org/mztabvalidator/rest/v2/"
};

export const paging = {
  defaultPageable: {
    page: 0,
    size: 20,
  },
};

export const plotlyConfig: Plotly.Config = {
  displaylogo: false,
  toImageButtonOptions: {
    format: "svg", // one of png, svg, jpeg, webp
    filename: "plot",
    height: 600,
    width: 800,
    scale: 1, // Multiply title/legend/axis/canvas sizes by this factor
  },
};

export const colorScales = defaultColorScales;

export const queries = {
  defaultLipidCompassQuery: <LipidCompassQuery>{
    lipidLevel: LipidLevel.SPECIES,
    names: [], 
    matchMode: LipidCompassQuery.MatchModeEnum.EXACT,
    normalizeName: false,
    unit: "",
    mzRange: [],
    quantityRange: [],
    mzTabResults: [],
    facets: [],
    selectedFacets: [],
  }
}

export const lipidClassRangesPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Total Lipid Concentrations on Class Level",
  yaxis: {
    title: "Lipid Class",
    zeroline: true,
    // categoryorder: "category descending"
  },
  xaxis: {
    title: "Summed Concentration",
    type: "log",
    zeroline: true,
  },
  height: 700,
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
    title: "Lipid Category",
    zeroline: true,
    categoryorder: "array",
  },
  xaxis: {
    type: 'log',
    autorange: true
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
};

export const lipidSpeciesCorrelationPlotLayout: Plotly.Layout = {
  autosize: true,
  title: "Lipid Concentrations on Species Level",
  // yaxis: {
  //   title: "# in DB",
  //   zeroline: true,
  // },
  yaxis: {
    title: "Summed Concentration in reference",
    zeroline: true,
    type: 'log',
    autorange: true
  },
  xaxis: {
    title: "Summed Concentration",
    type: 'log',
    autorange: true
  },
  height: 350,
  colorway: colorScales.colorbrewer.Set2[8],
  hoverlabel: { namelength :-1 },
}

export const authCodeFlowConfig: AuthConfig = {
  // Url of the Identity Provider
  issuer: "https://lifs-tools.org/auth/realms/lifs",

  // URL of the SPA to redirect the user to after login
  //redirectUri: window.location.origin + 'index.html',
  // /#/index.html
  redirectUri: window.location.origin, //window.location.origin, // + '/home',

  // The SPA's id. The SPA is registerd with this id at the auth-server
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
  requireHttps: true,

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
      enabled: false,
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
