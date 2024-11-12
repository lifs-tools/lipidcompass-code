## Lipid Compass Search-client@1.0.8-SNAPSHOT

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
npm install Lipid Compass Search-client@1.0.8-SNAPSHOT --save
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
npm link Lipid Compass Search-client
```

__Note for Windows users:__ The Angular CLI has troubles to use linked npm packages.
Please refer to this issue https://github.com/angular/angular-cli/issues/8284 for a solution / workaround.
Published packages are not effected by this issue.


#### General usage

In your Angular project:


```
// without configuring providers
import { LipidCompassSearchApiModule } from 'Lipid Compass Search-client';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    imports: [
        LipidCompassSearchApiModule,
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
import { LipidCompassSearchApiModule, LipidCompassSearchConfiguration, LipidCompassSearchConfigurationParameters } from 'Lipid Compass Search-client';

export function apiConfigFactory (): LipidCompassSearchConfiguration {
  const params: LipidCompassSearchConfigurationParameters = {
    // set configuration parameters here.
  }
  return new LipidCompassSearchConfiguration(params);
}

@NgModule({
    imports: [ LipidCompassSearchApiModule.forRoot(apiConfigFactory) ],
    declarations: [ AppComponent ],
    providers: [],
    bootstrap: [ AppComponent ]
})
export class AppModule {}
```

```
// configuring providers with an authentication service that manages your access tokens
import { LipidCompassSearchApiModule, LipidCompassSearchConfiguration } from 'Lipid Compass Search-client';

@NgModule({
    imports: [ LipidCompassSearchApiModule ],
    declarations: [ AppComponent ],
    providers: [
      {
        provide: LipidCompassSearchConfiguration,
        useFactory: (authService: AuthService) => new LipidCompassSearchConfiguration(
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
import { DefaultApi } from 'Lipid Compass Search-client';

export class AppComponent {
    constructor(private apiGateway: DefaultApi) { }
}
```

Note: The LipidCompassSearchApiModule is restricted to being instantiated once app wide.
This is to ensure that all services are treated as singletons.

#### Using multiple OpenAPI files / APIs / LipidCompassSearchApiModules
In order to use multiple `LipidCompassSearchApiModules` generated from different OpenAPI files,
you can create an alias name when importing the modules
in order to avoid naming conflicts:
```
import { LipidCompassSearchApiModule } from 'my-api-path';
import { LipidCompassSearchApiModule as OtherApiModule } from 'my-other-api-path';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    LipidCompassSearchApiModule,
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
import { BASE_PATH } from 'Lipid Compass Search-client';

bootstrap(AppComponent, [
    { provide: BASE_PATH, useValue: 'https://your-web-service.com' },
]);
```
or

```
import { BASE_PATH } from 'Lipid Compass Search-client';

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
import { BASE_PATH } from 'Lipid Compass Search-client';
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
