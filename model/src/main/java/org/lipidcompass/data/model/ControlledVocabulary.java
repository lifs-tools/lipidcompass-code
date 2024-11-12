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

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Document(value = "controlledVocabularies", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PersistentIndex(fields = {"label", "version"}, unique = true)
public class ControlledVocabulary extends ArangoBaseEntity {

//    @EqualsAndHashCode.Include
    private String label;
//    @EqualsAndHashCode.Include
    private String name;
//    @EqualsAndHashCode.Include
    private String uri;
//    @EqualsAndHashCode.Include
    private String version;

    @Builder
    public ControlledVocabulary(String label, String name, String uri, String version, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        if (label == null) {
            throw new IllegalArgumentException("Argument label must not be null!");
        }
        this.label = label;
        this.name = name;
        if (name == null) {
            throw new IllegalArgumentException("Argument name must not be null!");
        }
        this.uri = uri;
        if (uri == null) {
            throw new IllegalArgumentException("Argument uri must not be null!");
        }
        this.version = version;
        if (version == null) {
            throw new IllegalArgumentException("Argument version must not be null!");
        }
    }

}
