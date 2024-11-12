import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { LipidCompassBackendConfiguration } from './configuration';
import { HttpClient } from '@angular/common/http';


@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: []
})
export class LipidCompassBackendApiModule {
    public static forRoot(configurationFactory: () => LipidCompassBackendConfiguration): ModuleWithProviders<LipidCompassBackendApiModule> {
        return {
            ngModule: LipidCompassBackendApiModule,
            providers: [ { provide: LipidCompassBackendConfiguration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: LipidCompassBackendApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('LipidCompassBackendApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
