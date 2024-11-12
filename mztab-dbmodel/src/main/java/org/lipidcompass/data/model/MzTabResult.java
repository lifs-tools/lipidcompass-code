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
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.lipidcompass.data.model.dto.MzTabSummary;
import org.lipidcompass.data.model.relations.HasMzTabResultLipidQuantity;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lipidcompass.data.model.submission.Submission;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nilshoffmann
 */
@Document(collection = "mzTabResult")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@HashIndex(fields = { "nativeId" }, unique = false)
public class MzTabResult extends ArangoBaseEntity {

    public static enum Rating {
        UNRATED,
        AUTOMATICALLY_CHECKED,
        MANUALLY_CURATED
    }

    public static enum CompletenessLevel {
        INCOMPLETE,
        SUMMARY,
        FULL
    }

    private MzTabSummary mzTabSummary;

    @PersistentIndexed(unique = false, deduplicate = true)
    private String nativeId;

//    @JsonSerialize(using = MzTabDataSerializer.class)
    @JsonIgnore
    @Reference
    @Ref(lazy = true)
    private MzTabData mzTabData;

    //TODO connection to approval status / review status and audit trail
    private SubmissionStatus submissionStatus;

    @JsonIgnore
//    @JsonSerialize(using = LipidQuantitySerializer.class)
    @Relations(edges = HasMzTabResultLipidQuantity.class, lazy = true)
    private List<LipidQuantity> lipidQuantities;

    @Reference
    @Ref(lazy = true)
    private List<CvParameter> cvParameters;

    private Rating rating;

    private CompletenessLevel completeness;
    
    @Reference
    @Ref(lazy = true)
    private Submission submission;

    @Builder
    public MzTabResult(Submission submission, MzTabSummary mzTabSummary, MzTabData mzTabData, SubmissionStatus submissionStatus, List<CvParameter> cvParameters, Rating rating, CompletenessLevel completeness, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.submission = submission;
        this.mzTabSummary = mzTabSummary;
        this.mzTabData = mzTabData;
        this.nativeId = mzTabData.getNativeId();
        this.submissionStatus = submissionStatus;
        this.cvParameters = cvParameters;
        if (rating == null) {
            this.rating = Rating.UNRATED;
        } else {
            this.rating = rating;
        }
        if (completeness == null) {
            this.completeness = CompletenessLevel.INCOMPLETE;
        } else {
            this.completeness = completeness;
        }
    }

}
