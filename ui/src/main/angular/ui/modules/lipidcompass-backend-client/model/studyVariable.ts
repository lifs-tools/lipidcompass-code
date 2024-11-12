/**
 * LipidCompass Backend API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Assay } from './assay';
import { Parameter } from './parameter';


export interface StudyVariable { 
    id: number;
    name: string;
    assay_refs?: Array<Assay>;
    average_function?: Parameter;
    variation_function?: Parameter;
    description?: string;
    factors?: Array<Parameter>;
}

