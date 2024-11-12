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
package org.lipidcompass.backend.audit;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 *
 * @author nilshoffmann
 */
public class AuditorService implements AuditorAware<String> {

    private static final Logger log = LoggerFactory.getLogger(AuditorService.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        if (auth.isPresent()) {
            final Object p = auth.get().getPrincipal();
            log.debug("Principal is instance of {}", p.getClass().getSimpleName());
            log.debug("{}", p);
            if (p instanceof Jwt) {
                log.debug("Handling JWT");
                Jwt jwt = (Jwt) p;
                String userName = jwt.getClaimAsString("user_name");
                if (userName == null) {
                    userName = jwt.getClaimAsString("preferred_username");
                }
                log.debug("Authenticated auditor: {}", userName);
                return Optional.ofNullable(userName);
            }
            if (p instanceof UserDetails) {
                log.debug("Handling UserDetails");
                UserDetails userDetails = (UserDetails) p;
                return Optional.ofNullable(userDetails.getUsername());
            }
            log.warn("No Handler matched for principal! Returning empty auditor String!");
            return Optional.empty();
        }
        log.warn("No Auth was present in context! Returning empty auditor String!");
        return Optional.empty();
    }

}
