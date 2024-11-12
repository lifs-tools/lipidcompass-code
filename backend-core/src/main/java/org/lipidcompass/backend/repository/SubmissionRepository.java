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
package org.lipidcompass.backend.repository;

import com.arangodb.springframework.annotation.Query;
import org.lipidcompass.data.model.submission.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
@Repository
public interface SubmissionRepository extends SecuredArangoRepository<Submission>, CustomArangoRepository<Submission, String> {

    @Query(
        """
        FOR doc IN submissionProcess
          LET user = DOCUMENT("users", doc.submitter)
          FILTER user.userName == @submitterName
          #pageable
          RETURN doc
        """
    )
    public Page<Submission> findBySubmitter(@Param("submitterName")String submitterName, Pageable pageable);

    @Query(
        """
        FOR doc IN submissionProcess
          LET user = DOCUMENT("users", doc.submitter)
          FILTER user.userName == @submitterName
          FILTER doc._key == @submissionId
          #pageable
          RETURN doc
        """
    )
    public Page<Submission> findBySubmitterAndId(@Param("submitterName")String submitterName, @Param("submissionId") String submissionId, Pageable pageable);
    
}
