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
import { CvParameter } from './cvParameter';
import { Submission } from './submission';
import { MzTabSummary } from './mzTabSummary';


export interface MzTabResult { 
    id?: string;
    arangoId?: string;
    transactionUuid: string;
    visibility?: MzTabResult.VisibilityEnum;
    mzTabSummary?: MzTabSummary;
    nativeId?: string;
    submissionStatus?: MzTabResult.SubmissionStatusEnum;
    cvParameters?: Array<CvParameter>;
    rating?: MzTabResult.RatingEnum;
    completeness?: MzTabResult.CompletenessEnum;
    submission?: Submission;
    revision?: string;
    dateCreated?: string;
    dateLastModified?: string;
    createdBy?: string;
    updatedBy?: string;
}
export namespace MzTabResult {
    export type VisibilityEnum = 'PRIVATE' | 'RESTRICTED' | 'PUBLIC' | '11184809';
    export const VisibilityEnum = {
        PRIVATE: 'PRIVATE' as VisibilityEnum,
        RESTRICTED: 'RESTRICTED' as VisibilityEnum,
        PUBLIC: 'PUBLIC' as VisibilityEnum,
        unknown_default_open_api: '11184809' as VisibilityEnum
    };
    export type SubmissionStatusEnum = 'IN_PROGRESS' | 'SUBMITTED' | 'IN_CURATION' | 'IN_REVIEW' | 'PUBLISHED' | '11184809';
    export const SubmissionStatusEnum = {
        IN_PROGRESS: 'IN_PROGRESS' as SubmissionStatusEnum,
        SUBMITTED: 'SUBMITTED' as SubmissionStatusEnum,
        IN_CURATION: 'IN_CURATION' as SubmissionStatusEnum,
        IN_REVIEW: 'IN_REVIEW' as SubmissionStatusEnum,
        PUBLISHED: 'PUBLISHED' as SubmissionStatusEnum,
        unknown_default_open_api: '11184809' as SubmissionStatusEnum
    };
    export type RatingEnum = 'UNRATED' | 'AUTOMATICALLY_CHECKED' | 'MANUALLY_CURATED' | '11184809';
    export const RatingEnum = {
        UNRATED: 'UNRATED' as RatingEnum,
        AUTOMATICALLY_CHECKED: 'AUTOMATICALLY_CHECKED' as RatingEnum,
        MANUALLY_CURATED: 'MANUALLY_CURATED' as RatingEnum,
        unknown_default_open_api: '11184809' as RatingEnum
    };
    export type CompletenessEnum = 'INCOMPLETE' | 'SUMMARY' | 'FULL' | '11184809';
    export const CompletenessEnum = {
        INCOMPLETE: 'INCOMPLETE' as CompletenessEnum,
        SUMMARY: 'SUMMARY' as CompletenessEnum,
        FULL: 'FULL' as CompletenessEnum,
        unknown_default_open_api: '11184809' as CompletenessEnum
    };
}

