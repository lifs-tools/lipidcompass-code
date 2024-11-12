export interface CvParameter {
  accession: string;
  name: string;
  value: string;
  cv: ControlledVocabulary;
  referenceType: string;
}

export interface ControlledVocabulary {
  label: string;
  name: string;
  uri: string;
  version: string;
}
