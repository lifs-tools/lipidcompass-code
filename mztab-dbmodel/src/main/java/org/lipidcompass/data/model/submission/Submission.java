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
package org.lipidcompass.data.model.submission;

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Ref;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.SubmissionStatus;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Reference;

/**
 * A submission object, capturing submitted files, the submitting user, and the associated experiment.
 * @author nilshoffmann
 */
@Document(value = "submissionProcess", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class Submission extends ArangoBaseEntity {
    
    @PersistentIndexed
    private SubmissionStatus status;
    
    private List<FileResource> submittedFiles;
    
    private UUID storageBucket;
    
    @Reference
    @Ref(lazy = true)
    private Study study;
    
    @Reference
    @Ref(lazy = true)
    private User submitter;
    
    private UUID privateLinkUuid;

    public Submission(SubmissionStatus status, User submitter, List<FileResource> submittedFiles, UUID storageBucket, Study study, UUID privateLinkUuid, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.status = status;
        this.submittedFiles = submittedFiles;
        this.storageBucket = storageBucket;
        this.study = study;
        this.submitter = submitter;
        this.privateLinkUuid = privateLinkUuid;
    }
    
}
