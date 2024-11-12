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
import { PageableObject } from './pageableObject';
import { LipidQuantityDatasetAssayTableRow } from './lipidQuantityDatasetAssayTableRow';
import { SortObject } from './sortObject';


export interface PageLipidQuantityDatasetAssayTableRow { 
    totalElements?: number;
    totalPages?: number;
    size?: number;
    content?: Array<LipidQuantityDatasetAssayTableRow>;
    number?: number;
    sort?: SortObject;
    pageable?: PageableObject;
    first?: boolean;
    last?: boolean;
    numberOfElements?: number;
    empty?: boolean;
}

