import { CrossReference } from "./crossreference";

export interface Lipid {
  id: string;
  createdBy: string;
  updatedBy: string;
  nativeUrl: string;
  nativeId: string;
  commonName: string;
  normalizedShorthandName: string;
  lipidLevel: string;
  synonyms: Array<string>;
  systematicName: string;
  chemicalFormula: string;
  exactMass: Number;
  inchiKey: string;
  inchi: string;
  smiles: string;
  mdlModel: string;
  crossReferences: Array<CrossReference>;
  lipidCategory: string;
  lipidClass: string;
  lipidMapsEntry: Array<LipidMapsEntry>;
  swissLipidsEntry: Array<SwissLipidsEntry>;
  dateCreatedString: string;
  dateLastModified: string;
  _links: {
    self: {
      href: string;
    };
  };
}

export interface LipidMapsEntry {
  id: string;
  createdBy: string;
  updatedBy: string;
  lmClassificationCode: string;
  name: string;
  systematicName: string;
  normalizedName: string;
  abbreviation: string;
  level: string;
  nativeId: string;
  nativeUrl: string;
  dateCreatedString: string;
  dateLastModified: string;
  _links: {
    self: {
      href: string;
    };
  };
}

export interface SwissLipidsEntry {
  id: string;
  createdBy: string;
  updatedBy: string;
  normalizedName: string;
  abbreviation: string;
  description: string;
  level: string;
  nativeId: string;
  nativeUrl: string;
  dateCreatedString: string;
  dateLastModified: string;
  _links: {
    self: {
      href: string;
    };
  };
}

export interface LipidSvg {
  lipidId: string;
  svg: string;
}

export interface LipidQuantity {
  id: string;
  transactionUUid: string;
  visibility: string;
  lipid: Lipid;
  tissues: Array<string>;
  organisms: Array<string>;
  cellTypes: Array<string>;
  diseases: Array<string>;
  mzTabResultId: string;
  nativeId: string;
  assay: any;
  studyVariable: any;
  assayQuantity: Number;
  quantificationUnit: any;
  identificationReliability: string;
  bestIdentificationConfidenceMeasure: any;
  bestIdConfidenceValue: Number;
  studyVariableFactors: Array<string>;
  dateCreated: Date;
  dateLastModified: Date;
  createdBy: string;
  updatedBy: string;
  _links: {
    self: {
      href: string;
    };
  };
}

export interface LipidQuantitySolrDocument {
  id: string;
  collection: string;
  transactionUuid: string;
  normalizedName: string[];
  lipidLevel: string[];
  lipidCategory: string[];
  lipidClass: string[];
  lipidSpecies: string[];
  lipidMolecularSubSpecies: string[];
  lipidStructuralSubSpecies: string[];
  lipidIsomericSubSpecies: string[];
  organismNames: string[];
  organismAccessions: string[];
  tissueNames: string[];
  tissueAccessions: string[];
  cellTypeNames: string[];
  cellTypeAccessions: string[];
  diseaseNames: string[];
  diseaseAccessions: string[];
  sampleCustomNames: string[];
  sampleCustomAccessions: string[];
  sampleCustomValues: string[];
  assayCustomNames: string[];
  assayCustomAccessions: string[];
  assayCustomValues: string[];
  goMolecularFunctionNames: string[];
  goMolecularFunctionAccessions: string[];
  goCellComponentNames: string[];
  goCellComponentAccessions: string[];
  goBiologicalProcessNames: string[];
  goBiologicalProcessAccessions: string[];
  mzTabResultId: string;
  smlId: number;
  assayId: number;
  studyVariableId: number;
  assayQuantity: number;
  quantificationUnitAccession: string;
  quantificationUnitName: string;
  identificationReliability: string;
  bestIdentificationConfidenceMeasureAccession: string;
  bestIdentificationConfidenceMeasureName: string;
  bestIdConfidenceValue: number;
  studyVariableFactorNativeIds: string[];
  studyVariableFactorParameterAccessions: string[];
  studyVariableFactorParameterNames: string[];
  studyVariableFactorValues: string[];
  studyVariableFactorUnits: string[];
  studyVariableFactorUnitAccessions: string[];
  createdBy: string;
  updatedBy: string;
  dateCreated: Date;
  dateLastModified: Date;
  revision: string;
  visibility: string;
  owners: string[];
  score: number;
}
