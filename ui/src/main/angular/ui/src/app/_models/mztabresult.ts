import { CrossReference } from './crossreference';

export interface MzTabResult {
  id: string;
  transactionUuid: string;
  mzTabSummary: MzTabSummary;
  createdBy: string;
  updatedBy: string;
  nativeId: string;
  submissionStatus: string;
  visibility: string;
  mzTab: Object;
  dateCreated: string;
  dateLastModified: string;
  revision: string;
  _links: {
    self: {
      href: string;
    };
  };
}

export interface MzTabSummary {
  id: string;
  version: string;
  title: string;
  description: string;
  contacts: Contact[];
  assayCount: number;
  msRunCount: number;
  studyVariableCount: number;
  sampleCount: number;
  smlCount: number;
  smfCount: number;
  smeCount: number;
  submissionStatus: string;
  publications: Publication[];
}

export interface Publication {
  publicationItems: PublicationItem[];
}

export interface PublicationItem {
  type: string;
  accession: string;
}

export interface Contact {
  id: number;
  name: string;
  affiliation: string;
  email: string;
}

export interface MzTabResultsForLipid {
  mzTabResultId: string;
  _links: {
    self: {
      href: string;
    };
  };
}

/*
{"id":"11726339","visibility":"PRIVATE",
"mzTab":{"id":"LCDS1","version":"2.0.0-M","title":null,"description":"Establishing multiple omics baselines for three Southeast Asian populations in the Singapore Integrative Omics Study","contact":[],"numberAssays":359,"numberMsRuns":359,"numberStudyVariable":3,"numberSample":359,"numberSml":281,"numberSmf":0,"numberSme":0,"contact":"Establishing multiple omics baselines for three Southeast Asian populations in the Singapore Integrative Omics Study"},
"nativeId":"LCDS1",
"submissionStatus":"IN_SUBMISSION",
"revision":"_bW5Ay66---",
"dateCreated":"2020-11-03T15:38:53.653+00:00",
"dateLastModified":"2020-11-03T15:38:53.653+00:00",
"createdBy":null,
"updatedBy":null}
*/
