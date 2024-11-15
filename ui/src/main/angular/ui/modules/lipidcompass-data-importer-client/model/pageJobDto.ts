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
import { JobDto } from './jobDto';
import { Pageable } from './pageable';
import { Sort } from './sort';


export interface PageJobDto { 
    totalPages?: number;
    totalElements?: number;
    size?: number;
    content?: Array<JobDto>;
    number?: number;
    sort?: Sort;
    pageable?: Pageable;
    first?: boolean;
    last?: boolean;
    numberOfElements?: number;
    empty?: boolean;
}

