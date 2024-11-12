import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { LipidCompassSearchConfiguration } from './configuration';
import { HttpClient } from '@angular/common/http';

import { CollectionControllerService } from './api/collectionController.service';
import { LipidDocumentEntityControllerService } from './api/lipidDocumentEntityController.service';
import { LipidDocumentSearchControllerService } from './api/lipidDocumentSearchController.service';
import { LipidQuantityDocumentEntityControllerService } from './api/lipidQuantityDocumentEntityController.service';
import { LipidQuantityDocumentSearchControllerService } from './api/lipidQuantityDocumentSearchController.service';
import { MzTabResultDocumentEntityControllerService } from './api/mzTabResultDocumentEntityController.service';
import { MzTabResultDocumentSearchControllerService } from './api/mzTabResultDocumentSearchController.service';
import { ProfileControllerService } from './api/profileController.service';
import { StudyDocumentEntityControllerService } from './api/studyDocumentEntityController.service';
import { StudyDocumentSearchControllerService } from './api/studyDocumentSearchController.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: []
})
export class LipidCompassSearchApiModule {
    public static forRoot(configurationFactory: () => LipidCompassSearchConfiguration): ModuleWithProviders<LipidCompassSearchApiModule> {
        return {
            ngModule: LipidCompassSearchApiModule,
            providers: [ { provide: LipidCompassSearchConfiguration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: LipidCompassSearchApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('LipidCompassSearchApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
