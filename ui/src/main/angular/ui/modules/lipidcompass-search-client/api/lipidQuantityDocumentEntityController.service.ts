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
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent, HttpParameterCodec, HttpContext }       from '@angular/common/http';
import { CustomHttpParameterCodec }                          from '../encoder';
import { Observable }                                        from 'rxjs';

// @ts-ignore
import { EntityModelLipidQuantityDocument } from '../model/entityModelLipidQuantityDocument';
// @ts-ignore
import { LipidQuantityDocumentRequestBody } from '../model/lipidQuantityDocumentRequestBody';
// @ts-ignore
import { PagedModelEntityModelLipidQuantityDocument } from '../model/pagedModelEntityModelLipidQuantityDocument';

// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { LipidCompassSearchConfiguration }                                     from '../configuration';



@Injectable({
  providedIn: 'root'
})
export class LipidQuantityDocumentEntityControllerService {

    protected basePath = 'http://localhost:8091';
    public defaultHeaders = new HttpHeaders();
    public configuration = new LipidCompassSearchConfiguration();
    public encoder: HttpParameterCodec;

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: LipidCompassSearchConfiguration) {
        if (configuration) {
            this.configuration = configuration;
        }
        if (typeof this.configuration.basePath !== 'string') {
            if (typeof basePath !== 'string') {
                basePath = this.basePath;
            }
            this.configuration.basePath = basePath;
        }
        this.encoder = this.configuration.encoder || new CustomHttpParameterCodec();
    }


    private addToHttpParams(httpParams: HttpParams, value: any, key?: string): HttpParams {
        if (typeof value === "object" && value instanceof Date === false) {
            httpParams = this.addToHttpParamsRecursive(httpParams, value);
        } else {
            httpParams = this.addToHttpParamsRecursive(httpParams, value, key);
        }
        return httpParams;
    }

    private addToHttpParamsRecursive(httpParams: HttpParams, value?: any, key?: string): HttpParams {
        if (value == null) {
            return httpParams;
        }

        if (typeof value === "object") {
            if (Array.isArray(value)) {
                (value as any[]).forEach( elem => httpParams = this.addToHttpParamsRecursive(httpParams, elem, key));
            } else if (value instanceof Date) {
                if (key != null) {
                    httpParams = httpParams.append(key, (value as Date).toISOString().substr(0, 10));
                } else {
                   throw Error("key may not be null if value is Date");
                }
            } else {
                Object.keys(value).forEach( k => httpParams = this.addToHttpParamsRecursive(
                    httpParams, value[k], key != null ? `${key}.${k}` : k));
            }
        } else if (key != null) {
            httpParams = httpParams.append(key, value);
        } else {
            throw Error("key may not be null if value is not object or array");
        }
        return httpParams;
    }

    /**
     * delete-lipidquantitydocument
     * @param id 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteItemResourceLipidquantitydocumentDelete(id: string, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: undefined, context?: HttpContext}): Observable<any>;
    public deleteItemResourceLipidquantitydocumentDelete(id: string, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: undefined, context?: HttpContext}): Observable<HttpResponse<any>>;
    public deleteItemResourceLipidquantitydocumentDelete(id: string, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: undefined, context?: HttpContext}): Observable<HttpEvent<any>>;
    public deleteItemResourceLipidquantitydocumentDelete(id: string, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: undefined, context?: HttpContext}): Observable<any> {
        if (id === null || id === undefined) {
            throw new Error('Required parameter id was null or undefined when calling deleteItemResourceLipidquantitydocumentDelete.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.delete<any>(`${this.configuration.basePath}/lipidQuantityDocuments/${encodeURIComponent(String(id))}`,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get-lipidquantitydocument
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getCollectionResourceLipidquantitydocumentGet1(page?: number, size?: number, sort?: Array<string>, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json' | 'application/x-spring-data-compact+json' | 'text/uri-list', context?: HttpContext}): Observable<PagedModelEntityModelLipidQuantityDocument>;
    public getCollectionResourceLipidquantitydocumentGet1(page?: number, size?: number, sort?: Array<string>, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json' | 'application/x-spring-data-compact+json' | 'text/uri-list', context?: HttpContext}): Observable<HttpResponse<PagedModelEntityModelLipidQuantityDocument>>;
    public getCollectionResourceLipidquantitydocumentGet1(page?: number, size?: number, sort?: Array<string>, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json' | 'application/x-spring-data-compact+json' | 'text/uri-list', context?: HttpContext}): Observable<HttpEvent<PagedModelEntityModelLipidQuantityDocument>>;
    public getCollectionResourceLipidquantitydocumentGet1(page?: number, size?: number, sort?: Array<string>, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: 'application/hal+json' | 'application/x-spring-data-compact+json' | 'text/uri-list', context?: HttpContext}): Observable<any> {

        let localVarQueryParameters = new HttpParams({encoder: this.encoder});
        if (page !== undefined && page !== null) {
          localVarQueryParameters = this.addToHttpParams(localVarQueryParameters,
            <any>page, 'page');
        }
        if (size !== undefined && size !== null) {
          localVarQueryParameters = this.addToHttpParams(localVarQueryParameters,
            <any>size, 'size');
        }
        if (sort) {
            sort.forEach((element) => {
                localVarQueryParameters = this.addToHttpParams(localVarQueryParameters,
                  <any>element, 'sort');
            })
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/hal+json',
                'application/x-spring-data-compact+json',
                'text/uri-list'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.get<PagedModelEntityModelLipidQuantityDocument>(`${this.configuration.basePath}/lipidQuantityDocuments`,
            {
                context: localVarHttpContext,
                params: localVarQueryParameters,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * get-lipidquantitydocument
     * @param id 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getItemResourceLipidquantitydocumentGet(id: string, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<EntityModelLipidQuantityDocument>;
    public getItemResourceLipidquantitydocumentGet(id: string, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpResponse<EntityModelLipidQuantityDocument>>;
    public getItemResourceLipidquantitydocumentGet(id: string, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpEvent<EntityModelLipidQuantityDocument>>;
    public getItemResourceLipidquantitydocumentGet(id: string, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<any> {
        if (id === null || id === undefined) {
            throw new Error('Required parameter id was null or undefined when calling getItemResourceLipidquantitydocumentGet.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/hal+json'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.get<EntityModelLipidQuantityDocument>(`${this.configuration.basePath}/lipidQuantityDocuments/${encodeURIComponent(String(id))}`,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * patch-lipidquantitydocument
     * @param id 
     * @param lipidQuantityDocumentRequestBody 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public patchItemResourceLipidquantitydocumentPatch(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<EntityModelLipidQuantityDocument>;
    public patchItemResourceLipidquantitydocumentPatch(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpResponse<EntityModelLipidQuantityDocument>>;
    public patchItemResourceLipidquantitydocumentPatch(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpEvent<EntityModelLipidQuantityDocument>>;
    public patchItemResourceLipidquantitydocumentPatch(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<any> {
        if (id === null || id === undefined) {
            throw new Error('Required parameter id was null or undefined when calling patchItemResourceLipidquantitydocumentPatch.');
        }
        if (lipidQuantityDocumentRequestBody === null || lipidQuantityDocumentRequestBody === undefined) {
            throw new Error('Required parameter lipidQuantityDocumentRequestBody was null or undefined when calling patchItemResourceLipidquantitydocumentPatch.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/hal+json'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Content-Type', httpContentTypeSelected);
        }

        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.patch<EntityModelLipidQuantityDocument>(`${this.configuration.basePath}/lipidQuantityDocuments/${encodeURIComponent(String(id))}`,
            lipidQuantityDocumentRequestBody,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * create-lipidquantitydocument
     * @param lipidQuantityDocumentRequestBody 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public postCollectionResourceLipidquantitydocumentPost(lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<EntityModelLipidQuantityDocument>;
    public postCollectionResourceLipidquantitydocumentPost(lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpResponse<EntityModelLipidQuantityDocument>>;
    public postCollectionResourceLipidquantitydocumentPost(lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpEvent<EntityModelLipidQuantityDocument>>;
    public postCollectionResourceLipidquantitydocumentPost(lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<any> {
        if (lipidQuantityDocumentRequestBody === null || lipidQuantityDocumentRequestBody === undefined) {
            throw new Error('Required parameter lipidQuantityDocumentRequestBody was null or undefined when calling postCollectionResourceLipidquantitydocumentPost.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/hal+json'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Content-Type', httpContentTypeSelected);
        }

        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.post<EntityModelLipidQuantityDocument>(`${this.configuration.basePath}/lipidQuantityDocuments`,
            lipidQuantityDocumentRequestBody,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * update-lipidquantitydocument
     * @param id 
     * @param lipidQuantityDocumentRequestBody 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public putItemResourceLipidquantitydocumentPut(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<EntityModelLipidQuantityDocument>;
    public putItemResourceLipidquantitydocumentPut(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpResponse<EntityModelLipidQuantityDocument>>;
    public putItemResourceLipidquantitydocumentPut(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<HttpEvent<EntityModelLipidQuantityDocument>>;
    public putItemResourceLipidquantitydocumentPut(id: string, lipidQuantityDocumentRequestBody: LipidQuantityDocumentRequestBody, observe: any = 'body', reportProgress: boolean = false, options?: {httpHeaderAccept?: 'application/hal+json', context?: HttpContext}): Observable<any> {
        if (id === null || id === undefined) {
            throw new Error('Required parameter id was null or undefined when calling putItemResourceLipidquantitydocumentPut.');
        }
        if (lipidQuantityDocumentRequestBody === null || lipidQuantityDocumentRequestBody === undefined) {
            throw new Error('Required parameter lipidQuantityDocumentRequestBody was null or undefined when calling putItemResourceLipidquantitydocumentPut.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (BearerAuth) required
        localVarCredential = this.configuration.lookupCredential('BearerAuth');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        // authentication (OAuth2) required
        localVarCredential = this.configuration.lookupCredential('OAuth2');
        if (localVarCredential) {
            localVarHeaders = localVarHeaders.set('Authorization', 'Bearer ' + localVarCredential);
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/hal+json'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Content-Type', httpContentTypeSelected);
        }

        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        return this.httpClient.put<EntityModelLipidQuantityDocument>(`${this.configuration.basePath}/lipidQuantityDocuments/${encodeURIComponent(String(id))}`,
            lipidQuantityDocumentRequestBody,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
