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
import { CrossReference } from './crossReference';
import { SwissLipidsEntry } from './swissLipidsEntry';


export interface SwissLipidsEntryAllOf { 
    id?: string;
    arangoId?: string;
    transactionUuid?: string;
    visibility?: SwissLipidsEntryAllOf.VisibilityEnum;
    normalizedName?: string;
    abbreviation?: string;
    description?: string;
    level?: SwissLipidsEntryAllOf.LevelEnum;
    goslinLevel?: SwissLipidsEntryAllOf.GoslinLevelEnum;
    synonyms?: Array<string>;
    smiles?: string;
    nativeId?: string;
    nativeUrl?: string;
    parent?: SwissLipidsEntry;
    children?: Array<SwissLipidsEntry>;
    crossReferences?: Array<CrossReference>;
    revision?: string;
    dateCreated?: string;
    dateLastModified?: string;
    createdBy?: string;
    updatedBy?: string;
}
export namespace SwissLipidsEntryAllOf {
    export type VisibilityEnum = 'PRIVATE' | 'RESTRICTED' | 'PUBLIC' | '11184809';
    export const VisibilityEnum = {
        PRIVATE: 'PRIVATE' as VisibilityEnum,
        RESTRICTED: 'RESTRICTED' as VisibilityEnum,
        PUBLIC: 'PUBLIC' as VisibilityEnum,
        unknown_default_open_api: '11184809' as VisibilityEnum
    };
    export type LevelEnum = 'CATEGORY' | 'CLASS' | 'SPECIES' | 'MOLECULARSUBSPECIES' | 'STRUCTURALSUBSPECIES' | 'ISOMERICSUBSPECIES' | '11184809';
    export const LevelEnum = {
        CATEGORY: 'CATEGORY' as LevelEnum,
        CLASS: 'CLASS' as LevelEnum,
        SPECIES: 'SPECIES' as LevelEnum,
        MOLECULARSUBSPECIES: 'MOLECULARSUBSPECIES' as LevelEnum,
        STRUCTURALSUBSPECIES: 'STRUCTURALSUBSPECIES' as LevelEnum,
        ISOMERICSUBSPECIES: 'ISOMERICSUBSPECIES' as LevelEnum,
        unknown_default_open_api: '11184809' as LevelEnum
    };
    export type GoslinLevelEnum = 'NO_LEVEL' | 'UNDEFINED_LEVEL' | 'CATEGORY' | 'CLASS' | 'SPECIES' | 'MOLECULAR_SPECIES' | 'SN_POSITION' | 'STRUCTURE_DEFINED' | 'FULL_STRUCTURE' | 'COMPLETE_STRUCTURE' | '11184809';
    export const GoslinLevelEnum = {
        NO_LEVEL: 'NO_LEVEL' as GoslinLevelEnum,
        UNDEFINED_LEVEL: 'UNDEFINED_LEVEL' as GoslinLevelEnum,
        CATEGORY: 'CATEGORY' as GoslinLevelEnum,
        CLASS: 'CLASS' as GoslinLevelEnum,
        SPECIES: 'SPECIES' as GoslinLevelEnum,
        MOLECULAR_SPECIES: 'MOLECULAR_SPECIES' as GoslinLevelEnum,
        SN_POSITION: 'SN_POSITION' as GoslinLevelEnum,
        STRUCTURE_DEFINED: 'STRUCTURE_DEFINED' as GoslinLevelEnum,
        FULL_STRUCTURE: 'FULL_STRUCTURE' as GoslinLevelEnum,
        COMPLETE_STRUCTURE: 'COMPLETE_STRUCTURE' as GoslinLevelEnum,
        unknown_default_open_api: '11184809' as GoslinLevelEnum
    };
}

