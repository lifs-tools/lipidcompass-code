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


export interface PublicationItem { 
    type: PublicationItem.TypeEnum;
    accession: string;
}
export namespace PublicationItem {
    export type TypeEnum = 'doi' | 'pubmed' | 'uri' | '11184809';
    export const TypeEnum = {
        doi: 'doi' as TypeEnum,
        pubmed: 'pubmed' as TypeEnum,
        uri: 'uri' as TypeEnum,
        unknown_default_open_api: '11184809' as TypeEnum
    };
}


