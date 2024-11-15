## Lipid Compass Data Importer-client@1.0.13-SNAPSHOT

### Building

To install the required dependencies and to build the typescript sources run:
```
npm install
npm run build
```

### publishing

First build the package then run ```npm publish dist``` (don't forget to specify the `dist` folder!)

### consuming

Navigate to the folder of your consuming project and run one of next commands.

_published:_

```
npm install Lipid Compass Data Importer-client@1.0.13-SNAPSHOT --save
```

_without publishing (not recommended):_

```
npm install PATH_TO_GENERATED_PACKAGE/dist.tgz --save
```

_It's important to take the tgz file, otherwise you'll get trouble with links on windows_

_using `npm link`:_

In PATH_TO_GENERATED_PACKAGE/dist:
```
npm link
```

In your project:
```
npm link Lipid Compass Data Importer-client
```

__Note for Windows users:__ The Angular CLI has troubles to use linked npm packages.
Please refer to this issue https://github.com/angular/angular-cli/issues/8284 for a solution / workaround.
Published packages are not effected by this issue.


#### General usage

In your Angular project:


```
// without configuring providers
import { LipidCompassDataImporterApiModule } from 'Lipid Compass Data Importer-client';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    imports: [
        LipidCompassDataImporterApiModule,
        // make sure to import the HttpClientModule in the AppModule only,
        // see https://github.com/angular/angular/issues/20575
        HttpClientModule
    ],
    declarations: [ AppComponent ],
    providers: [],
    bootstrap: [ AppComponent ]
})
export class AppModule {}
```

```
// configuring providers
import { LipidCompassDataImporterApiModule, LipidCompassDataImporterConfiguration, LipidCompassDataImporterConfigurationParameters } from 'Lipid Compass Data Importer-client';

export function apiConfigFactory (): LipidCompassDataImporterConfiguration {
  const params: LipidCompassDataImporterConfigurationParameters = {
    // set configuration parameters here.
  }
  return new LipidCompassDataImporterConfiguration(params);
}

@NgModule({
    imports: [ LipidCompassDataImporterApiModule.forRoot(apiConfigFactory) ],
    declarations: [ AppComponent ],
    providers: [],
    bootstrap: [ AppComponent ]
})
export class AppModule {}
```

```
// configuring providers with an authentication service that manages your access tokens
import { LipidCompassDataImporterApiModule, LipidCompassDataImporterConfiguration } from 'Lipid Compass Data Importer-client';

@NgModule({
    imports: [ LipidCompassDataImporterApiModule ],
    declarations: [ AppComponent ],
    providers: [
      {
        provide: LipidCompassDataImporterConfiguration,
        useFactory: (authService: AuthService) => new LipidCompassDataImporterConfiguration(
          {
            basePath: environment.apiUrl,
            accessToken: authService.getAccessToken.bind(authService)
          }
        ),
        deps: [AuthService],
        multi: false
      }
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule {}
```

```
import { DefaultApi } from 'Lipid Compass Data Importer-client';

export class AppComponent {
    constructor(private apiGateway: DefaultApi) { }
}
```

Note: The LipidCompassDataImporterApiModule is restricted to being instantiated once app wide.
This is to ensure that all services are treated as singletons.

#### Using multiple OpenAPI files / APIs / LipidCompassDataImporterApiModules
In order to use multiple `LipidCompassDataImporterApiModules` generated from different OpenAPI files,
you can create an alias name when importing the modules
in order to avoid naming conflicts:
```
import { LipidCompassDataImporterApiModule } from 'my-api-path';
import { LipidCompassDataImporterApiModule as OtherApiModule } from 'my-other-api-path';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    LipidCompassDataImporterApiModule,
    OtherApiModule,
    // make sure to import the HttpClientModule in the AppModule only,
    // see https://github.com/angular/angular/issues/20575
    HttpClientModule
  ]
})
export class AppModule {

}
```


### Set service base path
If different than the generated base path, during app bootstrap, you can provide the base path to your service.

```
import { BASE_PATH } from 'Lipid Compass Data Importer-client';

bootstrap(AppComponent, [
    { provide: BASE_PATH, useValue: 'https://your-web-service.com' },
]);
```
or

```
import { BASE_PATH } from 'Lipid Compass Data Importer-client';

@NgModule({
    imports: [],
    declarations: [ AppComponent ],
    providers: [ provide: BASE_PATH, useValue: 'https://your-web-service.com' ],
    bootstrap: [ AppComponent ]
})
export class AppModule {}
```


#### Using @angular/cli
First extend your `src/environments/*.ts` files by adding the corresponding base path:

```
export const environment = {
  production: false,
  API_BASE_PATH: 'http://127.0.0.1:8080'
};
```

In the src/app/app.module.ts:
```
import { BASE_PATH } from 'Lipid Compass Data Importer-client';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [ ],
  providers: [{ provide: BASE_PATH, useValue: environment.API_BASE_PATH }],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
```

### Customizing path parameter encoding

Without further customization, only [path-parameters][parameter-locations-url] of [style][style-values-url] 'simple'
and Dates for format 'date-time' are encoded correctly.

Other styles (e.g. "matrix") are not that easy to encode
and thus are best delegated to other libraries (e.g.: [@honoluluhenk/http-param-expander]).

To implement your own parameter encoding (or call another library),
pass an arrow-function or method-reference to the `encodeParam` property of the Configuration-object
(see [General Usage](#general-usage) above).

Example value for use in your Configuration-Provider:
```typescript
new Configuration({
    encodeParam: (param: Param) => myFancyParamEncoder(param),
})
```

[parameter-locations-url]: https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.1.0.md#parameter-locations
[style-values-url]: https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.1.0.md#style-values
[@honoluluhenk/http-param-expander]: https://www.npmjs.com/package/@honoluluhenk/http-param-expander
