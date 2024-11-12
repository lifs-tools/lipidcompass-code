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
package org.lipidcompass.data.model;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.FulltextIndexed;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document("users")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends ArangoBaseEntity {

    @PersistentIndexed(unique = true)
    private String userName;
    @JsonIgnore
    private String password;
    @PersistentIndexed
    private String firstName;
    @PersistentIndexed
    private String familyName;
    @PersistentIndexed
    private String emailAddress;
    @PersistentIndexed
    private String orcid;

    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @Relations(edges = HasAuthority.class, lazy = true)
    private Collection<Authority> authorities;

    @Builder
    public User(String transactionUuid, String userName, String password, String firstName, String familyName, String emailAddress, String orcid, Boolean enabled, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Collection<Authority> authorities, Visibility visibility) {
        super(transactionUuid, visibility);
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.familyName = familyName;
        this.emailAddress = emailAddress;
        this.orcid = orcid;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

}
