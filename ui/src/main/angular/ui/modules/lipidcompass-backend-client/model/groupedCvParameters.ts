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


export interface GroupedCvParameters { 
    referenceType?: GroupedCvParameters.ReferenceTypeEnum;
    name?: Array<string>;
    accession?: Array<string>;
}
export namespace GroupedCvParameters {
    export type ReferenceTypeEnum = 'BEST_ID_CONFIDENCE_MEASURE' | 'SMALL_MOLECULE_QUANTIFICATION_UNIT' | 'SAMPLE_DISEASE' | 'SAMPLE_CELLTYPE' | 'SAMPLE_ORGANISM' | 'SAMPLE_TISSUE' | 'SAMPLE_CUSTOM' | 'ASSAY_CUSTOM' | 'MS_RUN_FORMAT' | 'MS_RUN_HASH_METHOD' | 'MS_RUN_ID_FORMAT' | 'MS_RUN_FRAGMENTATION_METHOD' | 'MS_RUN_SCAN_POLARITY' | 'STUDY_VARIABLE_FACTOR' | 'INSTRUMENT_ANALYZER' | 'INSTRUMENT_DETECTOR' | 'INSTRUMENT_NAME' | 'INSTRUMENT_SOURCE' | 'UNSPECIFIED' | '11184809';
    export const ReferenceTypeEnum = {
        BEST_ID_CONFIDENCE_MEASURE: 'BEST_ID_CONFIDENCE_MEASURE' as ReferenceTypeEnum,
        SMALL_MOLECULE_QUANTIFICATION_UNIT: 'SMALL_MOLECULE_QUANTIFICATION_UNIT' as ReferenceTypeEnum,
        SAMPLE_DISEASE: 'SAMPLE_DISEASE' as ReferenceTypeEnum,
        SAMPLE_CELLTYPE: 'SAMPLE_CELLTYPE' as ReferenceTypeEnum,
        SAMPLE_ORGANISM: 'SAMPLE_ORGANISM' as ReferenceTypeEnum,
        SAMPLE_TISSUE: 'SAMPLE_TISSUE' as ReferenceTypeEnum,
        SAMPLE_CUSTOM: 'SAMPLE_CUSTOM' as ReferenceTypeEnum,
        ASSAY_CUSTOM: 'ASSAY_CUSTOM' as ReferenceTypeEnum,
        MS_RUN_FORMAT: 'MS_RUN_FORMAT' as ReferenceTypeEnum,
        MS_RUN_HASH_METHOD: 'MS_RUN_HASH_METHOD' as ReferenceTypeEnum,
        MS_RUN_ID_FORMAT: 'MS_RUN_ID_FORMAT' as ReferenceTypeEnum,
        MS_RUN_FRAGMENTATION_METHOD: 'MS_RUN_FRAGMENTATION_METHOD' as ReferenceTypeEnum,
        MS_RUN_SCAN_POLARITY: 'MS_RUN_SCAN_POLARITY' as ReferenceTypeEnum,
        STUDY_VARIABLE_FACTOR: 'STUDY_VARIABLE_FACTOR' as ReferenceTypeEnum,
        INSTRUMENT_ANALYZER: 'INSTRUMENT_ANALYZER' as ReferenceTypeEnum,
        INSTRUMENT_DETECTOR: 'INSTRUMENT_DETECTOR' as ReferenceTypeEnum,
        INSTRUMENT_NAME: 'INSTRUMENT_NAME' as ReferenceTypeEnum,
        INSTRUMENT_SOURCE: 'INSTRUMENT_SOURCE' as ReferenceTypeEnum,
        UNSPECIFIED: 'UNSPECIFIED' as ReferenceTypeEnum,
        unknown_default_open_api: '11184809' as ReferenceTypeEnum
    };
}


