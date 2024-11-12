/*
 * Copyright 2021 The LipidCompass Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lipidcompass.batch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author nilshoffmann
 */
@OpenAPIDefinition
@Configuration
public class OpenApiConfiguration {

    @Value("${springdoc.version}")
    private String apiVersion;

    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String oidcUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().
                        addSecuritySchemes("BearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER).name("Authorization")).
                        addSecuritySchemes("OpenID",
                                new SecurityScheme().type(SecurityScheme.Type.OPENIDCONNECT).openIdConnectUrl(oidcUrl + "/.well-known/openid-configuration")
                                        .in(SecurityScheme.In.HEADER).name("Authorization")).
                        addSecuritySchemes("OAuth2",
                                new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(
                                        new OAuthFlows().authorizationCode(
                                                new OAuthFlow().
                                                        authorizationUrl(authorizationUrl).
                                                        tokenUrl(tokenUrl).
                                                        scopes(
                                                                new Scopes()
                                                        )
                                        )
                                )
                                        .in(SecurityScheme.In.HEADER).name("Authorization")
                        )
                )
                .info(new Info().title("LipidCompass Data Importer API").version(apiVersion).description("Documentation for LipidCompass Data Importer API v1.0"))
                .addSecurityItem(
                        new SecurityRequirement().addList("BearerAuth", Arrays.asList())
                ).addSecurityItem(
                        new SecurityRequirement().addList("OpenID", Arrays.asList())
                ).addSecurityItem(
                        new SecurityRequirement().addList("OAuth2", Arrays.asList())
                );
    }
}
