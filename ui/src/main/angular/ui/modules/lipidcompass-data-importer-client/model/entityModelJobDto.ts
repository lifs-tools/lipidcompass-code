/**
 * LipidCompass Data Importer API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { JobInstanceDto } from './jobInstanceDto';
import { Link } from './link';


export interface EntityModelJobDto { 
    name?: string;
    jobInstances?: Array<JobInstanceDto>;
    _links?: { [key: string]: Link; };
}

