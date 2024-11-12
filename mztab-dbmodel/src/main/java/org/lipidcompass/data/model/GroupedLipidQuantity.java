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

import org.lipidcompass.data.model.relations.HasGroupedAssayLipidQuantity;
import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.StudyVariable;
import java.util.Collection;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nils.hoffmann
 */
@Document(collection = "groupedLipidQuantity", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupedLipidQuantity extends ArangoBaseEntity {

    @Reference
    @Ref(lazy = true)
    private Lipid lipid;

    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private Collection<CvParameter> cvParameters;

    private String mzTabResultId;

    private String smlId;

    @Relations(edges = HasGroupedAssayLipidQuantity.class, lazy = true)
    private Collection<Lipid> lipidAssayQuantities;
    private StudyVariable studyVariable;

    @PersistentIndexed
    private Double studyVariableAverage;
    @PersistentIndexed
    private Double studyVariableVariation;

    @PersistentIndexed
    private Parameter studyVariableAverageFunction;
    @PersistentIndexed
    private Parameter studyVariableVariationFunction;

    @PersistentIndexed
    private Parameter quantificationUnit;
    @PersistentIndexed
    private Parameter identificationReliability;

    @Builder
    public GroupedLipidQuantity(Lipid lipid, String mzTabResultId, String smlId, StudyVariable studyVariable, Double studyVariableAverage, Double studyVariableVariation, Parameter studyVariableAverageFunction, Parameter studyVariableVariationFunction, Parameter quantificationUnit, Parameter identificationReliability, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.lipid = lipid;
        this.mzTabResultId = mzTabResultId;
        this.smlId = smlId;
        this.studyVariable = studyVariable;
        this.studyVariableAverage = studyVariableAverage;
        this.studyVariableVariation = studyVariableVariation;
        this.studyVariableAverageFunction = studyVariableAverageFunction;
        this.studyVariableVariationFunction = studyVariableVariationFunction;
        this.quantificationUnit = quantificationUnit;
        this.identificationReliability = identificationReliability;
    }

}
