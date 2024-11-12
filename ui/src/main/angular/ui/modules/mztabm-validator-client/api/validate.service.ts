/**
 * mzTab-M reference implementation and validation API.
 * This is the mzTab-M reference implementation and validation API service.
 *
 * OpenAPI spec version: 2.0.0
 * Contact: nils.hoffmann@isas.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs';

import { MzTab } from '../model/mzTab';
import { ValidationMessage } from '../model/validationMessage';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { MzTabValidationConfiguration }                                     from '../configuration';


@Injectable()
export class ValidateService {

    protected basePath = 'https://apps.lifs-tools.org/mztabvalidator/rest/v2/';
    public defaultHeaders = new HttpHeaders();
    public configuration = new MzTabValidationConfiguration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: MzTabValidationConfiguration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * 
     * Validates an mzTab file in XML or JSON representation and reports syntactic, structural, and semantic errors. 
     * @param mztabfile mzTab file that should be validated.
     * @param level The level of errors that should be reported, one of ERROR, WARN, INFO.
     * @param maxErrors The maximum number of errors to return.
     * @param semanticValidation Whether a semantic validation against the default rule set should be performed.
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public validateMzTabFile(mztabfile: MzTab, level?: 'info' | 'warn' | 'error', maxErrors?: number, semanticValidation?: boolean, observe?: 'body', reportProgress?: boolean): Observable<Array<ValidationMessage>>;
    public validateMzTabFile(mztabfile: MzTab, level?: 'info' | 'warn' | 'error', maxErrors?: number, semanticValidation?: boolean, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<ValidationMessage>>>;
    public validateMzTabFile(mztabfile: MzTab, level?: 'info' | 'warn' | 'error', maxErrors?: number, semanticValidation?: boolean, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<ValidationMessage>>>;
    public validateMzTabFile(mztabfile: MzTab, level?: 'info' | 'warn' | 'error', maxErrors?: number, semanticValidation?: boolean, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (mztabfile === null || mztabfile === undefined) {
            throw new Error('Required parameter mztabfile was null or undefined when calling validateMzTabFile.');
        }




        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (level !== undefined && level !== null) {
            queryParameters = queryParameters.set('level', <any>level);
        }
        if (maxErrors !== undefined && maxErrors !== null) {
            queryParameters = queryParameters.set('maxErrors', <any>maxErrors);
        }
        if (semanticValidation !== undefined && semanticValidation !== null) {
            queryParameters = queryParameters.set('semanticValidation', <any>semanticValidation);
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json',
            'application/xml'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.post<Array<ValidationMessage>>(`${this.basePath}/validate`,
            mztabfile,
            {
                params: queryParameters,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}