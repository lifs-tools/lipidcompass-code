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
import { ValidationMessage } from './validationMessage';


export interface FileResource { 
    fileName?: string;
    filePath?: string;
    sha256Hash?: string;
    fileSize?: number;
    fileType?: FileResource.FileTypeEnum;
    validationMessages?: Array<ValidationMessage>;
}
export namespace FileResource {
    export type FileTypeEnum = 'MZTAB_M' | 'MZML' | 'NATIVE_MS_FORMAT' | 'SPREADSHEET' | 'TEXT' | 'IMAGE' | 'BINARY' | '11184809';
    export const FileTypeEnum = {
        MZTAB_M: 'MZTAB_M' as FileTypeEnum,
        MZML: 'MZML' as FileTypeEnum,
        NATIVE_MS_FORMAT: 'NATIVE_MS_FORMAT' as FileTypeEnum,
        SPREADSHEET: 'SPREADSHEET' as FileTypeEnum,
        TEXT: 'TEXT' as FileTypeEnum,
        IMAGE: 'IMAGE' as FileTypeEnum,
        BINARY: 'BINARY' as FileTypeEnum,
        unknown_default_open_api: '11184809' as FileTypeEnum
    };
}


