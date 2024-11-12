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
import org.lipidcompass.data.model.submission.Submission;
import org.springframework.data.annotation.Reference;

import com.arangodb.springframework.annotation.Document;
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
 * @author nils.hoffmann
 */
@Document(collection = "lipidQuantity")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@HashIndex(fields = { "nativeId" }, unique = false)
public class LipidQuantity extends ArangoBaseEntity {

    //TODO this needs to be a proper edge with potentially multiple lipids (for isobars)
//    @Relations(edges = HasLipidQuantityLipidReference.class, lazy = true)
    @Reference
    @Ref(lazy = true)
    private Collection<Lipid> lipids;

    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private Collection<CvParameter> cvParameters;

    @PersistentIndexed(unique = false)
    private String nativeId;

    private String mzTabResultId;

    private Integer smlId;

    @Reference
    @Ref(lazy = true)
    private MzTabAssay assay;

    @Reference
    @Ref(lazy = true)
    private MzTabStudyVariable studyVariable;

    @PersistentIndexed
    private Double assayQuantity;

    @PersistentIndexed
    private Double assayRelativeQuantity;

    @Reference
    @Ref(lazy = true)
    private CvParameter quantificationUnit;

    private String identificationReliability;

    @Reference
    @Ref(lazy = true)
    private CvParameter bestIdentificationConfidenceMeasure;

    private Double bestIdConfidenceValue;
    
    @Reference
    @Ref(lazy = true)
    private Submission submission;

    @Builder
    public LipidQuantity(Submission submission, Collection<Lipid> lipid, String mzTabResultId, Integer smlId, MzTabAssay assay, MzTabStudyVariable studyVariable, Double assayQuantity, Double assayRelativeQuantity, CvParameter quantificationUnit, String identificationReliability, CvParameter bestIdentificationConfidenceMeasure, Double bestIdConfidenceValue, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.submission = submission;
        this.lipids = lipid;
//        this.cvParameterIds = cvParameterIds;
        this.mzTabResultId = mzTabResultId;
        this.smlId = smlId;
        this.nativeId = mzTabResultId + "/sml/" + smlId + "/assay/" + assay.getAssay().getId();
        this.assay = assay;
        this.studyVariable = studyVariable;
        this.assayQuantity = assayQuantity;
        this.assayRelativeQuantity = assayRelativeQuantity;
        this.quantificationUnit = quantificationUnit;
        this.identificationReliability = identificationReliability;
        this.bestIdentificationConfidenceMeasure = bestIdentificationConfidenceMeasure;
        this.bestIdConfidenceValue = bestIdConfidenceValue;
    }

}
