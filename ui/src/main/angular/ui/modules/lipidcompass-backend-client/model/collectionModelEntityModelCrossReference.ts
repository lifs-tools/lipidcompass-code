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
import { Link } from './link';
import { CollectionModelEntityModelCrossReferenceEmbedded } from './collectionModelEntityModelCrossReferenceEmbedded';


export interface CollectionModelEntityModelCrossReference { 
    _embedded?: CollectionModelEntityModelCrossReferenceEmbedded;
    _links?: { [key: string]: Link; };
}

