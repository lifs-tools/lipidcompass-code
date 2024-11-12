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
package org.lipidcompass.service.core.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Convert resource_access elements of Keycloak to client specific Spring
 * security roles aka GrantedAuthory's.
 *
 * @author nilshoffmann
 */
@Slf4j
public class LifsKeycloakClientRoleJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final String clientId;

    public LifsKeycloakClientRoleJwtConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(defaultGrantedAuthoritiesConverter.convert(source)
                .stream(),
                extractResourceRoles(source, clientId).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(source, authorities);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt, final String resourceId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        log.debug("Trying to access resource roles for " + resourceId);
        if (resourceAccess != null && (resource = (Map<String, Object>) resourceAccess.get(resourceId)) != null
                && (resourceRoles = (Collection<String>) resource.get("roles")) != null) {
            log.debug("Found resource roles for client: " + resourceId + "=" + resource.get("roles"));
            return resourceRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
