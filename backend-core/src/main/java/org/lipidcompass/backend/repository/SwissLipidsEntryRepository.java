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
import java.util.Collection;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
@Repository
public interface SwissLipidsEntryRepository extends SecuredArangoRepository<SwissLipidsEntry>, CustomArangoRepository<SwissLipidsEntry, String> {

    public SwissLipidsEntry findByAbbreviation(String abbreviation);

    public SwissLipidsEntry findByDescription(String description);
    
    public SwissLipidsEntry findByNativeId(String nativeId);
    
    public Page<SwissLipidsEntry> findAllByNormalizedName(String normalizedName, Pageable pageable);
    
    public Iterable<SwissLipidsEntry> findAllByNativeId(Collection<String> nativeIds);

    @Query("FOR e IN #collection FILTER e.level==@level #pageable RETURN e")
    public Page<SwissLipidsEntry> findByLevel(@Param("level") String level, Pageable pageable);
    
    public Page<SwissLipidsEntry> children(Pageable pageable);

}
