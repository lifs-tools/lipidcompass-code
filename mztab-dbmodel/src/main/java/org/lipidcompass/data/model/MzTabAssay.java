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
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import de.isas.mztab2.model.Assay;
import de.isas.mztab2.model.MzTab;
import java.util.Collection;
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
@Document(collection = "mzTabAssay")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MzTabAssay extends ArangoBaseEntity {

    @Relations(edges = HasCvParameterReference.class, lazy = true)
    private Collection<CvParameter> cvParameters;
    
    @PersistentIndexed(unique=false, deduplicate = true)
    private String nativeId;
    
    private Assay assay;
    
    @Reference
    @Ref(lazy = true)
    private Submission submission;

    @Builder
    public MzTabAssay(Submission submission, MzTab mzTab, Assay assay, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.submission = submission;
        this.assay = assay;
        this.nativeId = mzTab.getMetadata().getMzTabID() + "/metadata/assay/" + assay.getId();
    }

}
