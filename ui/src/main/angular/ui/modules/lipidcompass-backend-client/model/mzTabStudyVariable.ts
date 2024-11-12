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
import { StudyVariable } from './studyVariable';
import { Submission } from './submission';


export interface MzTabStudyVariable { 
    id?: string;
    arangoId?: string;
    transactionUuid: string;
    visibility?: MzTabStudyVariable.VisibilityEnum;
    studyVariable?: StudyVariable;
    nativeId?: string;
    submission?: Submission;
    revision?: string;
    dateCreated?: string;
    dateLastModified?: string;
    createdBy?: string;
    updatedBy?: string;
}
export namespace MzTabStudyVariable {
    export type VisibilityEnum = 'PRIVATE' | 'RESTRICTED' | 'PUBLIC' | '11184809';
    export const VisibilityEnum = {
        PRIVATE: 'PRIVATE' as VisibilityEnum,
        RESTRICTED: 'RESTRICTED' as VisibilityEnum,
        PUBLIC: 'PUBLIC' as VisibilityEnum,
        unknown_default_open_api: '11184809' as VisibilityEnum
    };
}


