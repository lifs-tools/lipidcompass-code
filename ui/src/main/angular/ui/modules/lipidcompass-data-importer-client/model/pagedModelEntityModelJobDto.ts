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
import { PageMetadata } from './pageMetadata';
import { PagedModelEntityModelJobDtoEmbedded } from './pagedModelEntityModelJobDtoEmbedded';
import { Link } from './link';


export interface PagedModelEntityModelJobDto { 
    _embedded?: PagedModelEntityModelJobDtoEmbedded;
    _links?: { [key: string]: Link; };
    page?: PageMetadata;
}

