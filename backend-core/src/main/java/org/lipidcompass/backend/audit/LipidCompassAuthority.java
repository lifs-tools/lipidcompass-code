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

import org.lipidcompass.data.model.Authority;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author nilshoffmann
 */
public class LipidCompassAuthority implements GrantedAuthority {

    private final Authority authority;

    public LipidCompassAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.authority.getName();
    }

}
