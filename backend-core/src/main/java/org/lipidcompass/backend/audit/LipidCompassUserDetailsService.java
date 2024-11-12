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

import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.data.model.User;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 *
 * @author nilshoffmann
 */
@Service
public class LipidCompassUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuditorService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Looking for user {}", username);
        Optional<User> users = userRepository.findByUserName(username);
        log.debug("Found the following user: {}", users);
        if (users.isPresent()) {
            return new LipidCompassUserPrincipal(users.get());
        } else {
            throw new IllegalArgumentException("User " + username + " not found!");
        }
    }

}
