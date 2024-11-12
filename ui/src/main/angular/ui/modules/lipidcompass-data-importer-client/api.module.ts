import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { LipidCompassDataImporterConfiguration } from './configuration';
import { HttpClient } from '@angular/common/http';


@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: []
})
export class LipidCompassDataImporterApiModule {
    public static forRoot(configurationFactory: () => LipidCompassDataImporterConfiguration): ModuleWithProviders<LipidCompassDataImporterApiModule> {
        return {
            ngModule: LipidCompassDataImporterApiModule,
            providers: [ { provide: LipidCompassDataImporterConfiguration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: LipidCompassDataImporterApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('LipidCompassDataImporterApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
