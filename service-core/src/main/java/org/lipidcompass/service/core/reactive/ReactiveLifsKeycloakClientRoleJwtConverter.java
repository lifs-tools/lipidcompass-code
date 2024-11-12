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

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Convert resource_access elements of Keycloak to client specific Spring
 * security roles aka GrantedAuthory's.
 *
 * @author nilshoffmann
 */
@Slf4j
public class ReactiveLifsKeycloakClientRoleJwtConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final Converter<Jwt, AbstractAuthenticationToken> converter;

    public ReactiveLifsKeycloakClientRoleJwtConverter(Converter<Jwt, AbstractAuthenticationToken> converter) {
        this.converter = converter;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(final Jwt source) {
        return Mono.just(converter.convert(source));
    }
}
