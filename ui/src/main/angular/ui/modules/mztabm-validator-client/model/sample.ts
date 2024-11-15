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
import { Parameter } from './parameter';


/**
 * Specification of sample. (empty) name: A name for each sample to serve as a list of the samples that MUST be reported in the following tables. Samples MUST be reported if a statistical design is being captured (i.e. bio or tech replicates). If the type of replicates are not known, samples SHOULD NOT be reported.  species: The respective species of the samples analysed. For more complex cases, such as metagenomics, optional columns and userParams should be used.  tissue: The respective tissue(s) of the sample.  cell_type: The respective cell type(s) of the sample.  disease: The respective disease(s) of the sample.  description: A human readable description of the sample.  custom: Custom parameters describing the sample's additional properties. Dates MUST be provided in ISO-8601 format. 
 */
export interface Sample { 
    id?: number;
    /**
     * The sample's name.
     */
    name?: string;
    /**
     * Additional user or cv parameters.
     */
    custom?: Array<Parameter>;
    /**
     * Biological species information on the sample.
     */
    species?: Array<Parameter>;
    /**
     * Biological tissue information on the sample.
     */
    tissue?: Array<Parameter>;
    /**
     * Biological cell type information on the sample.
     */
    cellType?: Array<Parameter>;
    /**
     * Disease information on the sample.
     */
    disease?: Array<Parameter>;
    /**
     * A free form description of the sample.
     */
    description?: string;
}
