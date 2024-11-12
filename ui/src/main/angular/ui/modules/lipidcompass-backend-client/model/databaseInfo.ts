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
import { DatabaseInfoAllOf } from './databaseInfoAllOf';
import { ArangoBaseEntity } from './arangoBaseEntity';


export interface DatabaseInfo { 
    id?: string;
    transactionUuid: string;
    visibility?: DatabaseInfo.VisibilityEnum;
    revision?: string;
    dateCreated?: string;
    dateLastModified?: string;
    createdBy?: string;
    updatedBy?: string;
    releaseVersion?: string;
    releaseDate?: string;
}
export namespace DatabaseInfo {
    export type VisibilityEnum = 'PRIVATE' | 'RESTRICTED' | 'PUBLIC';
    export const VisibilityEnum = {
        Private: 'PRIVATE' as VisibilityEnum,
        Restricted: 'RESTRICTED' as VisibilityEnum,
        Public: 'PUBLIC' as VisibilityEnum
    };
}

