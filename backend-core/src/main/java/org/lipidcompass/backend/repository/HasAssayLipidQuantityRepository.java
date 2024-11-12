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

import org.lipidcompass.data.model.relations.HasMzTabResultLipidQuantity;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "hasEvidence", path = "hasEvidence")
@Repository
public interface HasAssayLipidQuantityRepository extends SecuredArangoRepository<HasMzTabResultLipidQuantity> {
    
}
