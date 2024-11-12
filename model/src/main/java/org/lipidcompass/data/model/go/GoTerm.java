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
package org.lipidcompass.data.model.go;

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Relations;
import io.swagger.v3.oas.annotations.media.Schema;
import org.lipidcompass.data.model.CrossReference;
import org.lipidcompass.data.model.relations.HasCrossReference;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lipidcompass.data.model.ArangoBaseEntity;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "goTerms", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoTerm extends ArangoBaseEntity {
    @PersistentIndexed(unique = true)
    private String id;
    
    @PersistentIndexed
    private String name;
    
    @PersistentIndexed
    private String namespace;
    
    @PersistentIndexed
    private String definition;
    
    private List<String> synonyms;
    
    @Relations(edges = HasCrossReference.class, lazy = true)
    private List<CrossReference> crossReference;
}
