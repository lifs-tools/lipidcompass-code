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
package org.lipidcompass.data.model.roles;

import java.util.Optional;
import org.lipidcompass.data.model.Roles;

/**
 * System roles for users in LipidCompass. Anonymous users can use the
 * application without authentication. Users have an account and need to be
 * authenticated to use the application. Curators can create, update and edit
 * experiments and data that was originally created by a user. Admins have
 * access to everything and can also delete data.
 *
 * @author nilshoffmann
 */
public enum SystemRole {
    ANONYMOUS,
    USER,
    ACTUATOR,
    CURATOR,
    ADMIN;

    public static SystemRole of(String role) {
        Optional<String> optRole = Optional.ofNullable(role);
        if (optRole.isPresent()) {
            switch (role) {
                case Roles.ROLE_ADMIN:
                    return ADMIN;
                case Roles.ROLE_CURATOR:
                    return CURATOR;
                case Roles.ROLE_ACTUATOR:
                    return ACTUATOR;
                case Roles.ROLE_USER:
                    return USER;
                case Roles.ROLE_ANONYMOUS:
                default:
                    return ANONYMOUS;
            }
        }
        return ANONYMOUS;
    }
}
