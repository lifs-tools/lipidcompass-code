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
import { Link } from './link';


export interface EntityModelBatchStatus { 
    content?: EntityModelBatchStatus.ContentEnum;
    _links?: { [key: string]: Link; };
}
export namespace EntityModelBatchStatus {
    export type ContentEnum = 'COMPLETED' | 'STARTING' | 'STARTED' | 'STOPPING' | 'STOPPED' | 'FAILED' | 'ABANDONED' | 'UNKNOWN' | '11184809';
    export const ContentEnum = {
        COMPLETED: 'COMPLETED' as ContentEnum,
        STARTING: 'STARTING' as ContentEnum,
        STARTED: 'STARTED' as ContentEnum,
        STOPPING: 'STOPPING' as ContentEnum,
        STOPPED: 'STOPPED' as ContentEnum,
        FAILED: 'FAILED' as ContentEnum,
        ABANDONED: 'ABANDONED' as ContentEnum,
        UNKNOWN: 'UNKNOWN' as ContentEnum,
        unknown_default_open_api: '11184809' as ContentEnum
    };
}

