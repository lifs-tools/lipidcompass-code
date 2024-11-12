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
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "lipidMapsEntries", path = "/hierarchy/lipidmaps")
@Repository
public interface LipidMapsEntryRepository extends SecuredArangoRepository<LipidMapsEntry>, CustomArangoRepository<LipidMapsEntry, String> {

    public LipidMapsEntry findByAbbreviation(String abbreviation);

    public LipidMapsEntry findByName(String name);

    public LipidMapsEntry findByNativeId(String nativeId);
    
    public Iterable<LipidMapsEntry> findAllByNativeId(Collection<String> nativeIds);

    public Page<LipidMapsEntry> findAllByNormalizedName(String normalizedName, Pageable pageable);

    @Query("FOR e IN #collection FILTER e.level==@level #pageable RETURN e")
    public Page<LipidMapsEntry> findByLevel(@Param("level") String level, Pageable pageable);

    public Page<LipidMapsEntry> children(Pageable pageable);

}
