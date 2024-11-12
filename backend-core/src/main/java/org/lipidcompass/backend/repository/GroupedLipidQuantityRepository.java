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

import org.lipidcompass.data.model.GroupedLipidQuantity;
import org.lipidcompass.data.model.Lipid;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.StudyVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "lipids", path = "lipid")
@Repository
public interface GroupedLipidQuantityRepository extends SecuredArangoRepository<GroupedLipidQuantity>, CustomArangoRepository<GroupedLipidQuantity, String>{
    
    public Page<Lipid> findByLipid(Lipid lipid, Pageable pageable);
    
//    public Page<Lipid> findByTissue(Tissue tissue, Pageable pageable);
//    
//    public Page<Lipid> findByOrganism(Organism organism, Pageable pageable);
    
    public Page<Lipid> findByMzTabResultId(String mzTabResultId, Pageable pageable);
    
    public Page<Lipid> findByStudyVariable(StudyVariable studyVariable, Pageable pageable);
    
    public Page<Lipid> findByQuantificationUnit(Parameter quantificationUnit, Pageable pageable);

    public Page<Lipid> findByIdentificationReliability(Parameter identificationReliability, Pageable pageable);

    public Page<GroupedLipidQuantity> findByStudyVariableAverageBetween(Double lower, Double higher, Pageable pageable);
    
    public Page<GroupedLipidQuantity> findByStudyVariableVariationBetween(Double lower, Double higher, Pageable pageable);
    
    public Page<GroupedLipidQuantity> findByStudyVariableAverageFunction(Parameter studyVariableAverageFunction, Pageable pageable);
    
    public Page<GroupedLipidQuantity> findByStudyVariableVariationFunction(Parameter studyVariableVariationFunction, Pageable pageable);
    
}
