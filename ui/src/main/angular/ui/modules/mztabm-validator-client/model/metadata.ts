/**
 * mzTab-M reference implementation and validation API.
 * This is the mzTab-M reference implementation and validation API service.
 *
 * OpenAPI spec version: 2.0.0
 * Contact: nils.hoffmann@isas.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { Assay } from './assay';
import { CV } from './cV';
import { ColumnParameterMapping } from './columnParameterMapping';
import { Contact } from './contact';
import { Database } from './database';
import { Instrument } from './instrument';
import { MsRun } from './msRun';
import { Parameter } from './parameter';
import { Publication } from './publication';
import { Sample } from './sample';
import { SampleProcessing } from './sampleProcessing';
import { Software } from './software';
import { StudyVariable } from './studyVariable';
import { Uri } from './uri';


/**
 * The metadata section provides additional information about the dataset(s) reported in the mzTab file. All fields in the metadata section are optional apart from those noted as mandatory. The fields in the metadata section MUST be reported in order of the various fields listed here. The field’s name and value MUST be separated by a tab character.  
 */
export interface Metadata { 
    /**
     * The metadata section prefix. MUST always be MTD. 
     */
    prefix: Metadata.PrefixEnum;
    /**
     * The version of the mzTab file. The suffix MUST be \"-M\" for mzTab for metabolomics (mzTab-M). 
     */
    mzTabVersion: string;
    /**
     * The ID of the mzTab file, this could be supplied by the repository from which it is downloaded or a local identifier from the lab producing the file. It is not intended to be a globally unique ID but carry some locally useful meaning. 
     */
    mzTabID: string;
    /**
     * The file’s human readable title. 
     */
    title?: string;
    /**
     * The file’s human readable description. 
     */
    description?: string;
    /**
     * The contact’s name, affiliation and e-mail. Several contacts can be given by indicating the number in the square brackets after \"contact\". A contact has to be supplied in the format [first name] [initials] [last name].
     */
    contact?: Array<Contact>;
    /**
     * A publication associated with this file. Several publications can be given by indicating the number in the square brackets after “publication”. PubMed ids must be prefixed by “pubmed:”, DOIs by “doi:”. Multiple identifiers MUST be separated by “|”.
     */
    publication?: Array<Publication>;
    /**
     * A URI pointing to the file’s source data (e.g., a MetaboLights records).
     */
    uri?: Array<Uri>;
    /**
     * A URI pointing to an external file with more details about the study design (e.g., an ISA-TAB file).
     */
    externalStudyUri?: Array<Uri>;
    /**
     * The name, source, analyzer and detector of the instruments used in the experiment. Multiple instruments are numbered [1-n].
     */
    instrument?: Array<Instrument>;
    /**
     * The quantification method used in the experiment reported in the file.
     */
    quantificationMethod: Parameter;
    /**
     * Specification of sample. (empty) name: A name for each sample to serve as a list of the samples that MUST be reported in the following tables. Samples MUST be reported if a statistical design is being captured (i.e. bio or tech replicates). If the type of replicates are not known, samples SHOULD NOT be reported.  species: The respective species of the samples analysed. For more complex cases, such as metagenomics, optional columns and userParams should be used.  tissue: The respective tissue(s) of the sample.  cell_type: The respective cell type(s) of the sample.  disease: The respective disease(s) of the sample.  description: A human readable description of the sample.  custom: Custom parameters describing the sample’s additional properties. Dates MUST be provided in ISO-8601 format. 
     */
    sample?: Array<Sample>;
    /**
     * A list of parameters describing a sample processing, preparation or handling step similar to a biological or analytical methods report. The order of the sample_processing items should reflect the order these processing steps were performed in. If multiple parameters are given for a step these MUST be separated by a “|”. If derivatization was performed, it MUST be reported here as a general step, e.g. 'silylation' and the actual derivatization agens MUST be specified in the Section 6.2.54 part. 
     */
    sampleProcessing?: Array<SampleProcessing>;
    /**
     * Software used to analyze the data and obtain the reported results. The parameter’s value SHOULD contain the software’s version. The order (numbering) should reflect the order in which the tools were used. A software setting used. This field MAY occur multiple times for a single software. The value of this field is deliberately set as a String, since there currently do not exist CV terms for every possible setting.
     */
    software: Array<Software>;
    /**
     * A description of derivatization agents applied to small molecules, using userParams or CV terms where possible.
     */
    derivatizationAgent?: Array<Parameter>;
    /**
     * Specification of ms_run.  location: Location of the external data file e.g. raw files on which analysis has been performed. If the actual location of the MS run is unknown, a “null” MUST be used as a place holder value, since the [1-n] cardinality is referenced elsewhere. If pre-fractionation has been performed, then [1-n] ms_runs SHOULD be created per assay.  instrument_ref: If different instruments are used in different runs, instrument_ref can be used to link a specific instrument to a specific run.  format: Parameter specifying the data format of the external MS data file. If ms_run[1-n]-format is present, ms_run[1-n]-id_format SHOULD also be present, following the parameters specified in Table 1.  id_format: Parameter specifying the id format used in the external data file. If ms_run[1-n]-id_format is present, ms_run[1-n]-format SHOULD also be present. fragmentation_method: The type(s) of fragmentation used in a given ms run. scan_polarity: The polarity mode of a given run. Usually only one value SHOULD be given here except for the case of mixed polarity runs. hash: Hash value of the corresponding external MS data file defined in ms_run[1-n]-location. If ms_run[1-n]-hash is present, ms_run[1-n]-hash_method SHOULD also be present. hash_method: A parameter specifying the hash methods used to generate the String in ms_run[1-n]-hash. Specifics of the hash method used MAY follow the definitions of the mzML format. If ms_run[1-n]-hash is present, ms_run[1-n]-hash_method SHOULD also be present. 
     */
    msRun: Array<MsRun>;
    /**
     * Specification of assay. (empty) name: A name for each assay, to serve as a list of the assays that MUST be reported in the following tables.  custom: Additional custom parameters or values for a given assay.  external_uri: An external reference uri to further information about the assay, for example via a reference to an object within an ISA-TAB file.  sample_ref: An association from a given assay to the sample analysed.  ms_run_ref: An association from a given assay to the source MS run. All assays MUST reference exactly one ms_run unless a workflow with pre-fractionation is being encoded, in which case each assay MUST reference n ms_runs where n fractions have been collected. Multiple assays SHOULD reference the same ms_run to capture multiplexed experimental designs. 
     */
    assay: Array<Assay>;
    /**
     * Specification of study_variable. (empty) name: A name for each study variable (experimental condition or factor), to serve as a list of the study variables that MUST be reported in the following tables. For software that does not capture study variables, a single study variable MUST be reported, linking to all assays. This single study variable MUST have the identifier “undefined“. assay_refs: Bar-separated references to the IDs of assays grouped in the study variable. average_function: The function used to calculate the study variable quantification value and the operation used is not arithmetic mean (default) e.g. “geometric mean”, “median”. The 1-n refers to different study variables. variation_function: The function used to calculate the study variable quantification variation value if it is reported and the operation used is not coefficient of variation (default) e.g. “standard error”. description: A textual description of the study variable. factors: Additional parameters or factors, separated by bars, that are known about study variables allowing the capture of more complex, such as nested designs. 
     */
    studyVariable: Array<StudyVariable>;
    /**
     * Any additional parameters describing the analysis reported.
     */
    custom?: Array<Parameter>;
    /**
     * Specification of controlled vocabularies. label: A string describing the labels of the controlled vocabularies/ontologies used in the mzTab file as a short-hand e.g. \"MS\" for PSI-MS. full_name: A string describing the full names of the controlled vocabularies/ontologies used in the mzTab file. version: A string describing the version of the controlled vocabularies/ontologies used in the mzTab file. uri: A string containing the URIs of the controlled vocabularies/ontologies used in the mzTab file. 
     */
    cv: Array<CV>;
    /**
     * Defines what type of units are reported in the small molecule summary quantification / abundance fields.
     */
    smallMoleculeQuantificationUnit: Parameter;
    /**
     * Defines what type of units are reported in the small molecule feature quantification / abundance fields.
     */
    smallMoleculeFeatureQuantificationUnit: Parameter;
    /**
     * The system used for giving reliability / confidence codes to small molecule identifications MUST be specified if not using the default codes.
     */
    smallMoleculeIdentificationReliability?: Parameter;
    /**
     * Specification of databases. (empty): The description of databases used. For cases, where a known database has not been used for identification, a userParam SHOULD be inserted to describe any identification performed e.g. de novo. If no identification has been performed at all then \"no database\" should be inserted followed by null. prefix: The prefix used in the “identifier” column of data tables. For the “no database” case \"null\" must be used. version: The database version is mandatory where identification has been performed. This may be a formal version number e.g. “1.4.1”, a date of access “2016-10-27” (ISO-8601 format) or “Unknown” if there is no suitable version that can be annotated. uri: The URI to the database. For the “no database” case, \"null\" must be reported. 
     */
    database: Array<Database>;
    /**
     * The type of small molecule confidence measures or scores MUST be reported as a CV parameter [1-n]. The CV parameter definition should formally state whether the ordering is high to low or vice versa. The order of the scores SHOULD reflect their importance for the identification and be used to determine the identification’s rank.
     */
    idConfidenceMeasure: Array<Parameter>;
    /**
     * Defines the used unit for a column in the small molecule section. The format of the value has to be \\{column name}=\\{Parameter defining the unit}. This field MUST NOT be used to define a unit for quantification columns. The unit used for small molecule quantification values MUST be set in small_molecule-quantification_unit.
     */
    colunitSmallMolecule?: Array<ColumnParameterMapping>;
    /**
     * Defines the used unit for a column in the small molecule feature section. The format of the value has to be \\{column name}=\\{Parameter defining the unit}. This field MUST NOT be used to define a unit for quantification columns. The unit used for small molecule quantification values MUST be set in small_molecule_feature-quantification_unit.
     */
    colunitSmallMoleculeFeature?: Array<ColumnParameterMapping>;
    /**
     * Defines the used unit for a column in the small molecule evidence section. The format of the value has to be \\{column name}=\\{Parameter defining the unit}.
     */
    colunitSmallMoleculeEvidence?: Array<ColumnParameterMapping>;
}
export namespace Metadata {
    export type PrefixEnum = 'MTD';
    export const PrefixEnum = {
        MTD: 'MTD' as PrefixEnum
    };
}