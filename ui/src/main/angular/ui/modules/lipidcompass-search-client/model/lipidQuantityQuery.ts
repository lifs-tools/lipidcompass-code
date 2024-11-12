/**
 * LipidCompass Search API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { FilterOn } from './filterOn';


export interface LipidQuantityQuery { 
    lipidLevel: LipidQuantityQuery.LipidLevelEnum;
    names: Array<string>;
    matchMode: LipidQuantityQuery.MatchModeEnum;
    normalizeName: boolean;
    addDefaultFacets: boolean;
    unit: string;
    minValue?: number;
    maxValue?: number;
    facetFields: Array<string>;
    facetPivotFields: Array<string>;
    filterQueries: Array<FilterOn>;
}
export namespace LipidQuantityQuery {
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
    export type MatchModeEnum = 'EXACT' | 'FUZZY' | 'PREFIX' | 'SUFFIX' | '11184809';
    export const MatchModeEnum = {
        EXACT: 'EXACT' as MatchModeEnum,
        FUZZY: 'FUZZY' as MatchModeEnum,
        PREFIX: 'PREFIX' as MatchModeEnum,
        SUFFIX: 'SUFFIX' as MatchModeEnum,
        unknown_default_open_api: '11184809' as MatchModeEnum
    };
}


