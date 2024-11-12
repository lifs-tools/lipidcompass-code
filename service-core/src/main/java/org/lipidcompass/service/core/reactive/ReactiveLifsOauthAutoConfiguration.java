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
package org.lipidcompass.service.core.reactive;

import org.lipidcompass.service.core.servlet.LifsKeycloakClientRoleJwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

/**
 * Autoconfiguration of JWT infrastructure for webflux-based (reactive) web
 * applications.
 *
 * @author nilshoffmann
 */
@Configuration
@ConditionalOnWebApplication(type = REACTIVE)
public class ReactiveLifsOauthAutoConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id:lipidcompass}")
    private String clientId;

    @Bean
    @ConditionalOnBean(type = "org.springframework.web.reactive.function.client.WebClient")
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "jwtAuthenticationConverter")
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return new ReactiveLifsKeycloakClientRoleJwtConverter(new LifsKeycloakClientRoleJwtConverter(clientId));
    }

    @Bean
    @ConditionalOnMissingBean(ReactiveOAuth2UserService.class)
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> reactiveOauth2UserService(@Autowired ReactiveJwtDecoder reactiveJwtDecoder) {
        return new ReactiveLifsKeycloakOauthUserService(reactiveJwtDecoder);
    }

}
