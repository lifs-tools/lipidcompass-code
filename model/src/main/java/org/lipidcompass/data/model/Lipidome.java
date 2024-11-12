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
import com.arangodb.springframework.annotation.Relations;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lipidcompass.data.model.relations.HasCrossReference;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.lipidcompass.data.model.relations.HasLipid;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "lipidomes", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Lipidome extends ArangoBaseEntity {
    
    private String name;
    private String description;
    
    @Reference
    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private List<CvParameter> organisms;
    
    @Reference
    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private List<CvParameter> tissues;
    
    @Reference
    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private List<CvParameter> diseases;
    
    @Reference
    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private List<CvParameter> customParameters;
    
    @Reference
    @Relations(edges = HasCrossReference.class, lazy = true)
    private List<CrossReference> crossReferences;
    
    @Reference
    @Relations(edges = HasLipid.class, lazy = true)
    private List<Lipid> lipids;
}
