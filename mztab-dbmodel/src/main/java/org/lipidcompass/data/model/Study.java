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

import java.util.Collection;

import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.lipidcompass.data.model.relations.HasMzTabResult;
import org.lipidcompass.data.model.User;
import org.springframework.data.annotation.Reference;

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.FulltextIndexed;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;

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
@Document(collection = "studies", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PersistentIndex(fields = {"nativeId"}, unique = false, deduplicate = true)
public class Study extends ArangoBaseEntity {

    private String nativeId;
    
    private String name;
    @FulltextIndexed(minLength = 5)
    private String description;

    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private Collection<CvParameter> cvTermReferences;

    @Relations(edges = HasMzTabResult.class, lazy = true)
    private Collection<MzTabResult> mzTabResultReferences;
    
    @Reference
    @Ref(lazy = true)
    private User owner;

    @PersistentIndexed
    private SubmissionStatus status;

    @Builder
    public Study(SubmissionStatus status, String nativeId, String name, String description, User owner, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.status = status;
        this.nativeId = nativeId;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

}
