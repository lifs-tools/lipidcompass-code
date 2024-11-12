import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { MzTabValidationConfiguration } from './configuration';
import { HttpClient } from '@angular/common/http';


import { ValidateService } from './api/validate.service';
import { ValidatePlainService } from './api/validatePlain.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: [
    ValidateService,
    ValidatePlainService ]
})
export class MzTabValidationApiModule {
    public static forRoot(configurationFactory: () => MzTabValidationConfiguration): ModuleWithProviders<MzTabValidationApiModule> {
        return {
            ngModule: MzTabValidationApiModule,
            providers: [ { provide: MzTabValidationConfiguration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: MzTabValidationApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('MzTabValidationApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
