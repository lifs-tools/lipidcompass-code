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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_REQUEST;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.CollectionUtils;

/**
 * Service implementation to map Keycloak's clientId specific roles below the
 * resource_access element to Spring security roles.
 *
 * @author nilshoffmann
 */
@Slf4j
public class LifsKeycloakOauthUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final JwtDecoder jwtDecoder;

    public LifsKeycloakOauthUserService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest r) throws OAuth2AuthenticationException {
        if (r == null) {
            return null;
        }
        OidcUserService delegate = new OidcUserService();
        // Delegate to the default implementation for loading a user
        OidcUser oidcUser = delegate.loadUser(r);

        Set<GrantedAuthority> newAuthorities = new LinkedHashSet<>();
        // remove any ROLEs that have been preassigned, usually, this is ROLE_USER
        newAuthorities.addAll(oidcUser.getAuthorities().stream().filter((ga) -> !ga.getAuthority().startsWith("ROLE_")).collect(Collectors.toList()));
        Collection<? extends GrantedAuthority> kcClientAuthorities = extractKeycloakAuthorities(jwtDecoder, r);
        newAuthorities.addAll(kcClientAuthorities);
        if (newAuthorities.isEmpty()) {
            log.warn("No specific roles found, adding default ROLE_ANONYMOUS");
            // we need to assign at least one ROLE, otherwise spring security will complain.
            newAuthorities.add(new SimpleGrantedAuthority("ANONYMOUS"));
        }
        oidcUser = new DefaultOidcUser(newAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        return oidcUser;
    }

    /**
     * Extracts {@link GrantedAuthority GrantedAuthorities} from the AccessToken
     * in the {@link OidcUserRequest} for the clientId extracted from the client
     * registration.
     *
     * @see OidcUserRequest#getClientRegistration
     */
    private Collection<GrantedAuthority> extractKeycloakAuthorities(JwtDecoder jwtDecoder, OidcUserRequest userRequest) {

        Jwt token = parseJwt(jwtDecoder, userRequest.getAccessToken().getTokenValue());

        @SuppressWarnings("unchecked")
        Map<String, Object> resourceMap = (Map<String, Object>) token.getClaims().get("resource_access");
        String clientId = userRequest.getClientRegistration().getClientId();

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> clientResource = (Map<String, Map<String, Object>>) resourceMap.get(clientId);
        if (CollectionUtils.isEmpty(clientResource)) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> clientRoles = (List<String>) clientResource.get("roles");
        if (CollectionUtils.isEmpty(clientRoles)) {
            return Collections.emptyList();
        }

        return clientRoles.stream().map((t) -> t.toUpperCase()).map((role) -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    private Jwt parseJwt(JwtDecoder jwtDecoder, String accessTokenValue) {
        try {
            // Token is already verified by spring security infrastructure.
            return jwtDecoder.decode(accessTokenValue);
        } catch (JwtException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error(INVALID_REQUEST), e);
        }
    }

}
