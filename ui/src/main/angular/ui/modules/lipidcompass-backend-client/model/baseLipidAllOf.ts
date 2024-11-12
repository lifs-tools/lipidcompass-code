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


export interface BaseLipidAllOf { 
    id?: string;
    arangoId?: string;
    transactionUuid?: string;
    visibility?: BaseLipidAllOf.VisibilityEnum;
    nativeUrl?: string;
    commonName?: string;
    normalizedShorthandName?: string;
    lipidLevel?: BaseLipidAllOf.LipidLevelEnum;
    synonyms?: Array<string>;
    systematicName?: string;
    chemicalFormula?: string;
    exactMass?: number;
    inchiKey?: string;
    inchi?: string;
    smiles?: string;
    mdlModel?: string;
    crossReferences?: Array<CrossReference>;
    revision?: string;
    dateCreated?: string;
    dateLastModified?: string;
    createdBy?: string;
    updatedBy?: string;
}
export namespace BaseLipidAllOf {
    export type VisibilityEnum = 'PRIVATE' | 'RESTRICTED' | 'PUBLIC' | '11184809';
    export const VisibilityEnum = {
        PRIVATE: 'PRIVATE' as VisibilityEnum,
        RESTRICTED: 'RESTRICTED' as VisibilityEnum,
        PUBLIC: 'PUBLIC' as VisibilityEnum,
        unknown_default_open_api: '11184809' as VisibilityEnum
    };
    export type LipidLevelEnum = 'NO_LEVEL' | 'UNDEFINED_LEVEL' | 'CATEGORY' | 'CLASS' | 'SPECIES' | 'MOLECULAR_SPECIES' | 'SN_POSITION' | 'STRUCTURE_DEFINED' | 'FULL_STRUCTURE' | 'COMPLETE_STRUCTURE' | '11184809';
    export const LipidLevelEnum = {
        NO_LEVEL: 'NO_LEVEL' as LipidLevelEnum,
        UNDEFINED_LEVEL: 'UNDEFINED_LEVEL' as LipidLevelEnum,
        CATEGORY: 'CATEGORY' as LipidLevelEnum,
        CLASS: 'CLASS' as LipidLevelEnum,
        SPECIES: 'SPECIES' as LipidLevelEnum,
        MOLECULAR_SPECIES: 'MOLECULAR_SPECIES' as LipidLevelEnum,
        SN_POSITION: 'SN_POSITION' as LipidLevelEnum,
        STRUCTURE_DEFINED: 'STRUCTURE_DEFINED' as LipidLevelEnum,
        FULL_STRUCTURE: 'FULL_STRUCTURE' as LipidLevelEnum,
        COMPLETE_STRUCTURE: 'COMPLETE_STRUCTURE' as LipidLevelEnum,
        unknown_default_open_api: '11184809' as LipidLevelEnum
    };
}

