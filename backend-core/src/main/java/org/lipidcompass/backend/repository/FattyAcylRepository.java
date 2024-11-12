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

import org.lipidcompass.data.model.FattyAcyl;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "lipids", path = "lipid")
@Repository
public interface FattyAcylRepository extends SecuredArangoRepository<FattyAcyl>, CustomArangoRepository<FattyAcyl, String> {

////    public Lipid findByNativeId(String nativeId);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsCategory==CONCAT('lipidMapsEntries/', @lipidMapsCategory) #pageable RETURN e")
//    public Page<Lipid> findByLipidMapsCategory(String lipidMapsCategory, Pageable pageable);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsCategory==CONCAT('lipidMapsEntries/', @lipidMapsCategory) COLLECT WITH COUNT INTO length RETURN length")
//    public Long countByLipidMapsCategory(String lipidMapsCategory);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsMainClass==CONCAT('lipidMapsEntries/', @lipidMapsMainClass) #pageable RETURN e")
//    public Page<Lipid> findByLipidMapsMainClass(String lipidMapsMainClass, Pageable pageable);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsMainClass==CONCAT('lipidMapsEntries/', @lipidMapsMainClass) COLLECT WITH COUNT INTO length RETURN length")
//    public Long countByLipidMapsMainClass(String lipidMapsSubClass);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsSubClass==CONCAT('lipidMapsEntries/', @lipidMapsSubClass) #pageable RETURN e")
//    public Page<Lipid> findByLipidMapsSubClass(String lipidMapsSubClass, Pageable pageable);
//
//    @Query("FOR e IN #collection FILTER e.lipidMapsSubClass==CONCAT('lipidMapsEntries/', @lipidMapsSubClass) COLLECT WITH COUNT INTO length RETURN length")
//    public Long countByLipidMapsSubClass(String lipidMapsSubClass);
//    
//    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel COLLECT WITH COUNT INTO length RETURN length")
//    public Long countByLipidLevel(@Param("lipidLevel") LipidLevel lipidLevel);
//    
//    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel #pageable RETURN e")
//    public Page<Lipid> findByLipidLevel(@Param("lipidLevel") LipidLevel lipidLevel, Pageable pageable);
//
//    public Page<Lipid> findByCommonNameOrderByCommonNameAsc(String commonName, Pageable pageable);
//    
//    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel FILTER @normalizedShorthandNames ANY == e.normalizedShorthandName #pageable RETURN e")
//    public Page<Lipid> findByLipidLevelAndNormalizedShorthandNameIn(@Param("lipidLevel") LipidLevel lipidLevel, @Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);
//
//    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel FILTER STARTS_WITH(e.normalizedShorthandName, @normalizedShorthandNames) #pageable RETURN e")
//    public Page<Lipid> findByLipidLevelAndNormalizedShorthandNameStartsWithIn(LipidLevel lipidLevel, @Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);
//    
//    @Query("FOR e IN #collection FILTER @normalizedShorthandNames ANY == e.normalizedShorthandName #pageable RETURN e")
//    public Page<Lipid> findByNormalizedShorthandNameIn(@Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);
//    
//    @Query("FOR e IN #collection FILTER STARTS_WITH(e.normalizedShorthandName, @normalizedShorthandNames) #pageable RETURN e")
//    public Page<Lipid> findByNormalizedShorthandNameStartsWithIn(@Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);
//    
//    public Page<Lipid> findBySynonyms(String synonym, Pageable pageable);
//
//    public Page<Lipid> findBySystematicName(String systematicName, Pageable pageable);
//
//    public Page<Lipid> findByChemicalFormula(String chemicalFormula, Pageable pageable);
//
//    public Page<Lipid> findByExactMassOrderByExactMassAsc(Float exactMass, Pageable pageable);
//
//    public Page<Lipid> findByInchiKey(String inchiKey, Pageable pageable);
//
//    public Page<Lipid> findByInchi(String inchi, Pageable pageable);
//
//    public Page<Lipid> findBySmiles(String smiles, Pageable pageable);

}
