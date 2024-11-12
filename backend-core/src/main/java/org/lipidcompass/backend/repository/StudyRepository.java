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

import com.arangodb.model.AqlQueryOptions;
import com.arangodb.springframework.annotation.Query;
import java.util.Optional;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "experiments", path = "experiment")
@Repository
public interface StudyRepository extends SecuredArangoRepository<Study>, CustomArangoRepository<Study, String>  {
    
    public Optional<Study> findByNativeId(String nativeId);

    public Page<Study> findByOwnerUserName(String name, Pageable pageable);
    
    public Page<Study> findByVisibilityAndOwnerUserName(Visibility visibility, String name, Pageable pageable);
    
    @Query(
        """
    FOR doc IN #collection
      FILTER doc.visibility == "PUBLIC"
      SORT RAND() 
      LIMIT 1 
      RETURN doc
    """
    )
    public Study random(AqlQueryOptions options);
    
    // TODO add get contributors AQL
//    public 
    
    
}
