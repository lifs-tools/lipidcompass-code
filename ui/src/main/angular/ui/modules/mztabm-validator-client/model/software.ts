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
 * Software used to analyze the data and obtain the reported results. The parameter’s value SHOULD contain the software’s version. The order (numbering) should reflect the order in which the tools were used. A software setting used. This field MAY occur multiple times for a single software. The value of this field is deliberately set as a String, since there currently do not exist CV terms for every possible setting. 
 */
export interface Software { 
    id?: number;
    /**
     * Parameter defining the software being used.
     */
    parameter?: Parameter;
    /**
     * A software setting used. This field MAY occur multiple times for a single software. The value of this field is deliberately set as a String, since there currently do not exist cvParams for every possible setting. 
     */
    setting?: Array<string>;
}
