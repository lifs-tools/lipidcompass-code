import { FormGroup, FormControl, Validators } from '@angular/forms';

export const mzTabForm = new FormGroup({
  metadata: new FormControl(null, [Validators.required]),
  smallMoleculeSummary: new FormControl(null, [Validators.required]),
  smallMoleculeFeature: new FormControl(null, [Validators.required]),
  smallMoleculeEvidence: new FormControl(null, [Validators.required]),
  comment: new FormControl(null, [])
});

export const commentForm = new FormGroup({
  prefix: new FormControl(null, [Validators.required]),
  msg: new FormControl(null, [Validators.required]),
  line_number: new FormControl(null, [])
});

export const metadataForm = new FormGroup({
  prefix: new FormControl(null, [Validators.required]),
  'mzTab-version': new FormControl(null, [
    Validators.required,
    Validators.pattern(/^\d{1}\.\d{1}\.\d{1}-[A-Z]{1}$/)
  ]),
  'mzTab-ID': new FormControl(null, [Validators.required]),
  title: new FormControl(null, [Validators.required, Validators.minLength(20)]),
  description: new FormControl(null, [Validators.required, Validators.minLength(20)]),
  contact: new FormControl(null, [Validators.required]),
  publication: new FormControl(null, []),
  uri: new FormControl(null, []),
  external_study_uri: new FormControl(null, []),
  instrument: new FormControl(null, [Validators.required]),
  quantification_method: new FormControl(null, [Validators.required]),
  sample: new FormControl(null, []),
  sample_processing: new FormControl(null, []),
  software: new FormControl(null, [Validators.required]),
  derivatization_agent: new FormControl(null, []),
  ms_run: new FormControl(null, [Validators.required]),
  assay: new FormControl(null, [Validators.required]),
  study_variable: new FormControl(null, [Validators.required]),
  custom: new FormControl(null, []),
  cv: new FormControl(null, [Validators.required]),
  'small_molecule-quantification_unit': new FormControl(null, [
    Validators.required
  ]),
  'small_molecule_feature-quantification_unit': new FormControl(null, [
    Validators.required
  ]),
  'small_molecule-identification_reliability': new FormControl(null, []),
  database: new FormControl(null, [Validators.required]),
  id_confidence_measure: new FormControl(null, [Validators.required]),
  'colunit-small_molecule': new FormControl(null, []),
  'colunit-small_molecule_feature': new FormControl(null, []),
  'colunit-small_molecule_evidence': new FormControl(null, [])
});

export const smallMoleculeSummaryForm = new FormGroup({
  prefix: new FormControl(null, []),
  header_prefix: new FormControl(null, []),
  sml_id: new FormControl(null, [Validators.required]),
  smf_id_refs: new FormControl(null, []),
  database_identifier: new FormControl(null, []),
  chemical_formula: new FormControl(null, []),
  smiles: new FormControl(null, []),
  inchi: new FormControl(null, []),
  chemical_name: new FormControl(null, []),
  uri: new FormControl(null, []),
  theoretical_neutral_mass: new FormControl(null, []),
  adduct_ions: new FormControl(null, [
    Validators.pattern(/^\[\d*M([+-][\w]*)\]\d*[+-]$/)
  ]),
  reliability: new FormControl(null, []),
  best_id_confidence_measure: new FormControl(null, []),
  best_id_confidence_value: new FormControl(null, []),
  abundance_assay: new FormControl(null, []),
  abundance_study_variable: new FormControl(null, []),
  abundance_variation_study_variable: new FormControl(null, []),
  opt: new FormControl(null, []),
  comment: new FormControl(null, [])
});

export const smallMoleculeFeatureForm = new FormGroup({
  prefix: new FormControl(null, []),
  header_prefix: new FormControl(null, []),
  smf_id: new FormControl(null, [Validators.required]),
  sme_id_refs: new FormControl(null, []),
  sme_id_ref_ambiguity_code: new FormControl(null, []),
  adduct_ion: new FormControl(null, [
    Validators.pattern(/^\[\d*M([+-][\w]*)\]\d*[+-]$/)
  ]),
  isotopomer: new FormControl(null, []),
  exp_mass_to_charge: new FormControl(null, [Validators.required]),
  charge: new FormControl(null, [Validators.required]),
  retention_time_in_seconds: new FormControl(null, []),
  retention_time_in_seconds_start: new FormControl(null, []),
  retention_time_in_seconds_end: new FormControl(null, []),
  abundance_assay: new FormControl(null, []),
  opt: new FormControl(null, []),
  comment: new FormControl(null, [])
});

export const smallMoleculeEvidenceForm = new FormGroup({
  prefix: new FormControl(null, []),
  header_prefix: new FormControl(null, []),
  sme_id: new FormControl(null, [Validators.required]),
  evidence_input_id: new FormControl(null, [Validators.required]),
  database_identifier: new FormControl(null, [Validators.required]),
  chemical_formula: new FormControl(null, []),
  smiles: new FormControl(null, []),
  inchi: new FormControl(null, []),
  chemical_name: new FormControl(null, []),
  uri: new FormControl(null, []),
  derivatized_form: new FormControl(null, []),
  adduct_ion: new FormControl(null, [
    Validators.pattern(/^\[\d*M([-][\w]*)\]\d*[+-]$/)
  ]),
  exp_mass_to_charge: new FormControl(null, [Validators.required]),
  charge: new FormControl(null, [Validators.required]),
  theoretical_mass_to_charge: new FormControl(null, [Validators.required]),
  spectra_ref: new FormControl(null, [Validators.required]),
  identification_method: new FormControl(null, [Validators.required]),
  ms_level: new FormControl(null, [Validators.required]),
  id_confidence_measure: new FormControl(null, []),
  rank: new FormControl(null, [Validators.required, Validators.min(1)]),
  opt: new FormControl(null, []),
  comment: new FormControl(null, [])
});

export const parameterForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  cv_label: new FormControl(null, []),
  cv_accession: new FormControl(null, []),
  name: new FormControl(null, [Validators.required]),
  value: new FormControl(null, [Validators.required])
});

export const instrumentForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  name: new FormControl(null, []),
  source: new FormControl(null, []),
  analyzer: new FormControl(null, []),
  detector: new FormControl(null, [])
});

export const sampleProcessingForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  sampleProcessing: new FormControl(null, [])
});

export const softwareForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  parameter: new FormControl(null, []),
  setting: new FormControl(null, [])
});

export const publicationForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  publicationItems: new FormControl(null, [Validators.required])
});

export const publicationItemForm = new FormGroup({
  type: new FormControl(null, [Validators.required]),
  accession: new FormControl(null, [Validators.required])
});

export const spectraRefForm = new FormGroup({
  ms_run: new FormControl(null, [Validators.required]),
  reference: new FormControl(null, [Validators.required])
});

export const contactForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  name: new FormControl(null, []),
  affiliation: new FormControl(null, []),
  email: new FormControl(null, [
    Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
  ])
});

export const uriForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  value: new FormControl(null, [])
});

export const sampleForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  name: new FormControl(null, []),
  custom: new FormControl(null, []),
  species: new FormControl(null, []),
  tissue: new FormControl(null, []),
  cell_type: new FormControl(null, []),
  disease: new FormControl(null, []),
  description: new FormControl(null, [])
});

export const msRunForm = new FormGroup({
  id: new FormControl(null, [Validators.required, Validators.min(1)]),
  name: new FormControl(null, []),
  location: new FormControl(null, [Validators.required]),
  instrument_ref: new FormControl(null, []),
  format: new FormControl(null, []),
  id_format: new FormControl(null, []),
  fragmentation_method: new FormControl(null, []),
  scan_polarity: new FormControl(null, []),
  hash: new FormControl(null, []),
  hash_method: new FormControl(null, [])
});

export const studyVariableForm = new FormGroup({
  id: new FormControl(null, [Validators.required, Validators.min(1)]),
  name: new FormControl(null, [Validators.required]),
  assay_refs: new FormControl(null, []),
  average_function: new FormControl(null, []),
  variation_function: new FormControl(null, []),
  description: new FormControl(null, []),
  factors: new FormControl(null, [])
});

export const assayForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  name: new FormControl(null, [Validators.required]),
  custom: new FormControl(null, []),
  external_uri: new FormControl(null, []),
  sample_ref: new FormControl(null, []),
  ms_run_ref: new FormControl(null, [Validators.required])
});

export const cvForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  label: new FormControl(null, [Validators.required]),
  full_name: new FormControl(null, [Validators.required]),
  version: new FormControl(null, [Validators.required]),
  uri: new FormControl(null, [Validators.required])
});

export const databaseForm = new FormGroup({
  id: new FormControl(null, [Validators.min(1)]),
  param: new FormControl(null, [Validators.required]),
  prefix: new FormControl(null, [Validators.required]),
  version: new FormControl(null, [Validators.required]),
  uri: new FormControl(null, [Validators.required])
});

export const columnParameterMappingForm = new FormGroup({
  column_name: new FormControl(null, [Validators.required]),
  param: new FormControl(null, [Validators.required])
});

export const optColumnMappingForm = new FormGroup({
  identifier: new FormControl(null, [Validators.required]),
  param: new FormControl(null, []),
  value: new FormControl(null, [])
});

export const errorForm = new FormGroup({
  code: new FormControl(null, [Validators.required]),
  message: new FormControl(null, [Validators.required])
});

export const validationMessageForm = new FormGroup({
  code: new FormControl(null, [Validators.required]),
  category: new FormControl(null, [Validators.required]),
  message_type: new FormControl(null, []),
  message: new FormControl(null, [Validators.required]),
  line_number: new FormControl(null, [])
});
