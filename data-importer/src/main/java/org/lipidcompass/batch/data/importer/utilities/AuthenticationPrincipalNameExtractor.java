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
package org.lipidcompass.batch.data.importer.utilities;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.User.UserBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * This class extracts the user name from the Authentication object.
 *
 * Currently, only JwtAuthenticationToken with claim "user_name" is supported.
 * For all other implementations {@code Authentication#getName} is returned.
 *
 *
 * @author nilshoffmann
 */
@Slf4j
public class AuthenticationPrincipalNameExtractor {

    private final UserRepository userRepository;

    public AuthenticationPrincipalNameExtractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getName(Authentication authentication) {
        if (authentication == null) {
            return "NO_AUTHENTICATION_PROVIDED";
        }
        if (authentication instanceof JwtAuthenticationToken) {
            Object principal = ((JwtAuthenticationToken) authentication).getPrincipal();
            log.debug("Principal: {}, class={}", principal, principal.getClass().getName());
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                String userName = jwt.getClaimAsString("user_name");
                if (userName == null) {
                    userName = jwt.getClaimAsString("preferred_username");
                }
                log.debug("UserName: {}", userName);
                if (userName != null && !userName.isEmpty()) {
                    return userName;
                }
            }
        }
        return authentication.getName();
    }

    public Optional<User> getOrCreateUser(Authentication authentication) {
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication instanceof JwtAuthenticationToken) {
            Object principal = ((JwtAuthenticationToken) authentication).getPrincipal();
            log.debug("Principal: {}, class={}", principal, principal.getClass().getName());
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                String userName = jwt.getClaimAsString("user_name");
                Optional<User> users = userRepository.findByUserName(userName);
                User user;
                if (users.isPresent()) {
                    user = users.get();
                    user.setFirstName(jwt.getClaimAsString("given_name"));
                    user.setFamilyName(jwt.getClaimAsString("family_name"));
                    user.setEmailAddress(jwt.getClaimAsString("email"));
                } else {
                    if (userName == null) {
                        userName = jwt.getClaimAsString("preferred_username");
                    }
                    UserBuilder userBuilder = User.builder();
                    userBuilder.userName(userName);
                    userBuilder.firstName(jwt.getClaimAsString("given_name"));
                    userBuilder.familyName(jwt.getClaimAsString("family_name"));
                    userBuilder.emailAddress(jwt.getClaimAsString("email"));
                    user = userBuilder.build();
                }
                return Optional.of(userRepository.save(user));
            }
        }
        return Optional.empty();
    }

}
