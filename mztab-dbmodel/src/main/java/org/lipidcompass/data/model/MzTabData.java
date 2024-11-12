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
import de.isas.mztab2.model.MzTab;
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
@Document(collection = "mzTabData")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MzTabData extends ArangoBaseEntity {

    private MzTab mzTab;
    
    @PersistentIndexed(unique = false, deduplicate = true)
    private String nativeId;
    
    @Reference
    @Ref(lazy = true)
    private Submission submission;

    @Builder
    public MzTabData(Submission submission, MzTab mzTab, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.submission = submission;
        this.mzTab = mzTab;
        this.nativeId = mzTab.getMetadata().getMzTabID();
    }
}
