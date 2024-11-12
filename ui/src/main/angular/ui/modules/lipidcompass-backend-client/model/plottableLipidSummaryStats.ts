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


export interface PlottableLipidSummaryStats { 
    dataset?: string;
    normalizedShorthandNames?: string;
    lipidIds?: Array<string>;
    quantificationUnit?: string;
    quantificationUnitAccession?: string;
    minAssayQuantity?: number;
    averageAssayQuantity?: number;
    maxAssayQuantity?: number;
    stddevAssayQuantity?: number;
    countAssayQuantity?: number;
    perc25?: number;
    perc50?: number;
    perc75?: number;
    iqr?: number;
    lowerWhisker?: number;
    upperWhisker?: number;
}

