import { Component, Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { NotificationService, ServiceMessage } from './notification.service';
import { UserService } from './user.service';
import { deployment } from '../../environments/environment';
import { LipidQuery } from '../_models/lipidquery';
import { Page, PageAndSort } from '../_models/page';
import { Lipid, LipidQuantity } from '../_models/lipid';
import { LipidControllerService, LipidQuantityControllerService } from '../../../modules/lipidcompass-backend-client';
import { LipidDocumentEntityControllerService, LipidDocumentSearchControllerService, LipidQuantityDocumentEntityControllerService, LipidQuantityDocumentSearchControllerService, LipidQuantityQuery, MzTabResultDocumentEntityControllerService, MzTabResultDocumentSearchControllerService, StudyDocumentEntityControllerService, StudyDocumentSearchControllerService } from '../../../modules/lipidcompass-search-client';

export class Ms1QueryParams {
  constructor(
    readonly masses: string,
    readonly level: string,
    readonly tolerance: number,
    readonly identified: boolean,
    readonly adductions: Array<string>
  ) { }
}

export class Ms1SearchResponse {
  constructor(
    readonly list: Array<Ms1ResultItem>,
    readonly totalCount: number,
    readonly success: boolean,
    readonly error: string,
    readonly code: number
  ) { }
}

export class QueryParams {
  constructor(
    readonly query: string,
    readonly type: string,
    readonly start: number,
    readonly limit: number,
    readonly page: number,
    readonly callback: string
  ) { }
}

export class SearchResponse<T extends ResultItem> {
  constructor(
    readonly page: Page<T>,
    readonly success: boolean,
    readonly error: string,
    readonly code: number
  ) { }
}

export class ResultItem {
  constructor(
    readonly itemId: string,
    readonly name: string,
    readonly type: string,
    readonly href: string
  ) { }
}

export class LipidResultItem extends ResultItem {
  constructor(
    readonly lipid: Lipid
  ) {
    super(lipid.id, lipid.normalizedShorthandName, 'Lipid', lipid?._links?.self?.href);
  }
}

export class LipidQuantityResultItem extends ResultItem {
  constructor(
    readonly lipidQuantity: LipidQuantity
  ) {
    super(lipidQuantity.id, lipidQuantity.lipid.normalizedShorthandName, 'Lipid Quantity', lipidQuantity?._links?.self?.href);
  }
}

export class Ms1ResultItem extends ResultItem {
  constructor(
    readonly itemId: string,
    readonly name: string,
    readonly type: string,
    readonly identified: boolean,
    readonly mass: number,
    readonly faCarbons: number,
    readonly faDoubleBonds: number,
    readonly resMass: number,
    readonly adductIon: string,
    readonly code: string,
    readonly url: string
  ) {
    super(itemId, name, type, url);
  }
}

export class AdductIonResponse {
  constructor(
    readonly list: Array<AdductIon>,
    readonly success: boolean,
    readonly errorMessage: string
  ) { }
}

export class AdductIon {
  constructor(
    readonly itemId: number,
    readonly name: string,
    readonly mass: number
  ) { }
}

@Injectable()
export class LipidsearchService {
  private backendUrl = deployment.backendPath;

  constructor(
    private http: HttpClient,
    private notificationservice: NotificationService,
    private userService: UserService,
    private lipidQuantityDocumentEntityController: LipidQuantityDocumentEntityControllerService,
    private lipidQuantityDocumentSearchController: LipidQuantityDocumentSearchControllerService,
    private lipidDocumentEntityController: LipidDocumentEntityControllerService,
    private lipidDocumentSearchController: LipidDocumentSearchControllerService,
    private studyDocumentEntityController: StudyDocumentEntityControllerService,
    private studyDocumentSearchController: StudyDocumentSearchControllerService,
    private mzTabResultDocumentEntityController: MzTabResultDocumentEntityControllerService,
    private mzTabResultDocumentSearchController: MzTabResultDocumentSearchControllerService
  ) { }

  health() {
    // console.debug('Checking backend health status!');
    // this.http
    //   .get<any>(this.backendUrl + '/actuator/health')
    //   .toPromise()
    //   .then(health => {
    //     if (health['status'] !== 'UP') {
    //       this.notificationservice.publishMessage(
    //         'messages',
    //         <ServiceMessage>{
    //           level: 'ALERT',
    //           title: 'Backend resource unavailable!',
    //           content:
    //             'Response status: ' +
    //             health['status'] +
    //             '\nMessage: ' +
    //             health['statusText']
    //         }
    //       );
    //     } else {
    //     }
    //   })
    //   .catch(error => {
    //     this.notificationservice.publishMessage('messages', <
    //       ServiceMessage
    //       >{
    //         level: 'ALERT',
    //         title: 'Backend resource unavailable!',
    //         content:
    //           'Response status: ' +
    //           error['status'] +
    //           '\nMessage: ' +
    //           error['statusText']
    //       });
    //   });
  }

  // searchLipid(name: string, level: string, matchingMode: string, normalizeName: boolean, addDefaultFacets: boolean, pageAndSort: PageAndSort): Promise<SearchResponse<LipidResultItem>> {
  //   // const queryParams = new QueryParams(query, level, 0, 100, 0, '');
  //   // console.debug('QueryParams: ' + JSON.stringify(queryParams));
  //   return this._searchLipid(
  //     this.backendUrl,
  //     this.buildLipidQueryBody(
  //       level,
  //       name,
  //       matchingMode,
  //       normalizeName,
  //       addDefaultFacets,
  //       pageAndSort
  //     ),
  //     pageAndSort
  //   );
  // }

  // searchLipidQuantity(name: string, level: string, matchingMode: string, normalizeName: boolean, addDefaultFacets: boolean, pageAndSort: PageAndSort): Promise<SearchResponse<LipidQuantityResultItem>> {
  //   // const queryParams = new QueryParams(query, level, 0, 100, 0, '');
  //   // console.debug('QueryParams: ' + JSON.stringify(queryParams));
  //   return this._searchLipidQuantity(
  //     this.backendUrl,
  //     this.buildLipidQueryBody(
  //       level,
  //       name,
  //       matchingMode,
  //       normalizeName,
  //       addDefaultFacets,
  //       pageAndSort
  //     ),
  //     pageAndSort
  //   );
  // }

  // searchLimit(
  //   query: string,
  //   level: string,
  //   limit: number
  // ): Promise<SearchResponse> {
  //   const queryParams = new QueryParams(query, level, 0, limit, 0, '');
  //   // console.debug('QueryParams: ' + JSON.stringify(queryParams));
  //   return this._search(
  //     this.backendUrl,
  //     queryParams
  //   );
  // }
  /*
  searchMs(msQueryParams: MsQueryParams): Promise<MsSearchResponse>{

  }
*/
  /*   adductIons(): Promise<AdductIonResponse> {
      return this.http
        .get<AdductIonResponse>(
          this.backendUrl + '/solr/adductions'
        )
        .toPromise()
        .then(data => <AdductIonResponse>data)
        .catch(error => {
          this.notificationservice.publishMessage('notification.service.lipidsearch',
          <BackendMessage>{
            level: 'ALERT',
            title: 'Problem while running search',
            content: this.formatError(error)
          });
          return <AdductIonResponse>error;
        });
    } */

  // private _searchLipidsForMzTabFiles(

  // ): Promise<> {

  // }

  // private _searchLipid(
  //   path: string,
  //   queryParams: LipidQuery,
  //   pageAndSort: PageAndSort
  // ): Promise<SearchResponse<LipidResultItem>> {
  //   // const options = { body: queryParams };
  //   const options = {};
  //   if (pageAndSort) {
  //     options['params'] = new HttpParams().append(
  //       'page', pageAndSort.number.toString()).append(
  //         'size', pageAndSort.size.toString()
  //       );
  //   } else {
  //     options['params'] = new HttpParams().append(
  //       'page', "0").append(
  //         'size', "20"
  //       );
  //   }

  //   // this.lipidController.findByLipidQuery().toPromise().;

  //   return this.http
  //     .post<SearchResponse<LipidResultItem>>(
  //       path + '/lipids/findByLipidQuery',
  //       queryParams,
  //       options
  //     )
  //     .toPromise()
  //     .then(data => <SearchResponse<LipidResultItem>>this.asLipidListSearchResponse(data, 200))
  //     .catch(error => {
  //       this.notificationservice.publishMessage('messages',
  //         <ServiceMessage>{
  //           level: 'ALERT',
  //           title: 'Problem while running search',
  //           content: this.formatError(error)
  //         });
  //       return this.asLipidListSearchErrorResponse(error, error['status']);
  //     });
  // }

  // private _searchLipidQuantity(
  //   path: string,
  //   queryParams: LipidQuantityQuery,
  //   pageAndSort: PageAndSort
  // ): Promise<FacetPagedModelEntityModelLipidQuantityDocument> {
  //   // const options = { body: queryParams };
  //   const options = {};
  //   if (pageAndSort) {
  //     options['params'] = new HttpParams().append(
  //       'page', pageAndSort.number.toString()).append(
  //         'size', pageAndSort.size.toString()
  //       );
  //   } else {
  //     options['params'] = new HttpParams().append(
  //       'page', "0").append(
  //         'size', "20"
  //       );
  //   }

  //   // return this.lipidQuantityDocumentSearchController.findByLipidQuery(
  //   //   p: page
  //   // ).toPromise();

  //   return this.http
  //     .post<SearchResponse<LipidQuantityResultItem>>(
  //       path + '/lipidquantities/findByLipidQuery',
  //       queryParams,
  //       options
  //     )
  //     .toPromise()
  //     .then(data => <SearchResponse<LipidQuantityResultItem>>this.asLipidQuantityListSearchResponse(data, 200))
  //     .catch(error => {
  //       this.notificationservice.publishMessage('messages',
  //         <ServiceMessage>{
  //           level: 'ALERT',
  //           title: 'Problem while running search',
  //           content: this.formatError(error)
  //         });
  //       return this.asLipidQuantityListSearchErrorResponse(error, error['status']);
  //     });
  // }

  /*
  {"_embedded":{"lipidList":[{"id":"23327416","transactionUuid":null,"visibility":"PUBLIC","nativeUrl":null,"nativeId":"LCLID:SM 37:2","commonName":null,"normalizedShorthandName":"SM 37:2","lipidLevel":"SPECIES","synonyms":null,"systematicName":null,"chemicalFormula":"C42H83N2O4P","exactMass":710.6091,"inchiKey":null,"inchi":null,"smiles":null,"mdlModel":null,"crossReferences":[],"lipidMapsCategory":null,"lipidMapsMainClass":null,"lipidMapsSubClass":null,"swissLipidsEntry":null,"revision":"_bkcMRBO---","dateCreated":"2020-12-15T17:59:07.571+00:00","dateLastModified":"2020-12-15T17:59:07.571+00:00","createdBy":"nilstest","updatedBy":"nilstest","_links":{"self":{"href":"http://192.168.178.45:8081/lipids/23327416"}}}]},"page":{"size":1,"totalElements":1,"totalPages":1,"number":0}}
  */

  private asLipidQuantityListSearchResponse(data: any, code: number): SearchResponse<LipidQuantityResultItem> {
    console.debug("Response: " + JSON.stringify(data));
    return new SearchResponse<LipidQuantityResultItem>(
      this.asLipidQuantityListResponseList(data),
      code == 200 ? true : false,
      '',
      code
    );
  }

  private asLipidListSearchResponse(data: any, code: number): SearchResponse<LipidResultItem> {
    console.debug("Response: " + JSON.stringify(data));
    return new SearchResponse<LipidResultItem>(
      this.asLipidListResponseList(data),
      code == 200 ? true : false,
      '',
      code
    );
  }

  private asLipidListSearchErrorResponse(data: any, code: number): SearchResponse<LipidResultItem> {
    const page: Page<LipidResultItem> = {
      data: [],
      size: data.page['size'],
      totalElements: data.page['totalElements'],
      totalPages: data.page['totalPages'],
      number: data.page['number']
    };
    return new SearchResponse(page, false, data['statusText'], code);
  }

  private asLipidQuantityListSearchErrorResponse(data: any, code: number): SearchResponse<LipidQuantityResultItem> {
    const page: Page<LipidQuantityResultItem> = {
      data: [],
      size: data.page['size'],
      totalElements: data.page['totalElements'],
      totalPages: data.page['totalPages'],
      number: data.page['number']
    };
    return new SearchResponse(page, false, data['statusText'], code);
  }

  private asLipidQuantityListResponseList(data: any): Page<LipidQuantityResultItem> {
    const page: Page<LipidQuantityResultItem> = {
      data: data['_embedded'] ? this.asLipidQuantityResultItemResponseList(data['_embedded']['lipidQuantityList']) : [],
      size: data.page['size'],
      totalElements: data.page['totalElements'],
      totalPages: data.page['totalPages'],
      number: data.page['number']
    };
    return page;
  }

  private asLipidListResponseList(data: any): Page<LipidResultItem> {
    const page: Page<LipidResultItem> = {
      data: data['_embedded'] ? this.asLipidResultItemResponseList(data['_embedded']['lipids']) : [],
      size: data.page['size'],
      totalElements: data.page['totalElements'],
      totalPages: data.page['totalPages'],
      number: data.page['number']
    };
    return page;
  }

  private asLipidQuantityResultItemResponseList(list: Array<any>): Array<LipidQuantityResultItem> {
    const responseList = new Array<LipidQuantityResultItem>();
    list.forEach(result => {
      responseList.push(
        new LipidQuantityResultItem(
          <LipidQuantity>result
        )
      );
    });
    return responseList;
  }

  private asLipidResultItemResponseList(list: Array<any>): Array<LipidResultItem> {
    const responseList = new Array<LipidResultItem>();
    list.forEach(result => {
      responseList.push(
        new LipidResultItem(
          <Lipid>result
        )
      );
    });
    return responseList;
  }

  private buildLipidQueryBody(lipidLevel: string, name: string, matchingMode: string, normalizeName: boolean, addDefaultFacets: boolean, pageAndSort: PageAndSort): LipidQuery {
    const query: LipidQuery = {
      lipidLevel: lipidLevel === null ? "" : lipidLevel,
      shorthandLipidNames: name === null ? [] : [name],
      matchingMode: matchingMode,
      normalizeName: normalizeName,
      addDefaultFacets: addDefaultFacets,
      unit: null,
      cellTypes: null,
      diseases: null,
      goTerms: null,
      organisms: null,
      tissues: null,
      mzRange: null,
      quantityRange: null,
      mzTabResults: null,
      pageAndSort: pageAndSort
    };
    console.debug("Query Body: " + JSON.stringify(query));
    return query;
  }

  private buildHttpParams(queryParams: QueryParams): HttpParams {
    const httpParams = new HttpParams()
      .append('query', queryParams.query)
      .append('type', queryParams.type)
      .append('start', '' + queryParams.start)
      .append('limit', '' + queryParams.limit)
      .append('page', '' + queryParams.page);
    console.debug('HTTP params: ' + JSON.stringify(httpParams));
    return httpParams;
  }

  private formatError(error: any): string {
    return (
      'Response status: ' +
      error['status'] +
      '\nMessage: ' +
      error['statusText']
    );
  }
}
