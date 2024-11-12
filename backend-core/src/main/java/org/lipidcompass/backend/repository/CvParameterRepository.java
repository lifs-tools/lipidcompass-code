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
import org.lipidcompass.data.model.ControlledVocabulary;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.dto.GroupedCvParameters;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "lipids", path = "lipid")
@Repository
public interface CvParameterRepository extends SecuredArangoRepository<CvParameter>, CustomArangoRepository<CvParameter, String>  {
//    public CvParameter findByAccession(String accession);
    public <S extends CvParameter> Page<CvParameter> findAllByName(String name, Pageable pageable);
    public <S extends CvParameter> Page<CvParameter> findAllByCv(ControlledVocabulary cv, Pageable pageable);
//    public <S extends CvParameter> Page<CvParameter> findAllByNativeId(String nativeId, Pageable pageable);
//    public <S extends CvParameter> Iterable<CvParameter> findAllByNativeId(String nativeId, Sort sort);
    public <S extends CvParameter> Page<CvParameter> findAllByAccession(String accession, Pageable pageable);

    @Query(
        """
        FOR doc IN #collection
            FILTER doc.`referenceType` == @referenceType
            SORT doc.accession, doc.value ASC
            #pageable
            RETURN DISTINCT {doc}
        """
    )
    public <S extends CvParameter> Page<CvParameter> findAllByReferenceType(@Param("referenceType") HasCvParameterReference.ReferenceType referenceType, Pageable pageable);
    
    @Query(
        """
        RETURN COUNT(
            FOR doc IN #collection
                FILTER doc.`referenceType` == @referenceType
                RETURN DISTINCT doc.accession
        )
        """
    )
    public Long countByReferenceTypeDistinct(@Param("referenceType") HasCvParameterReference.ReferenceType referenceType);

    @Query(
        """
        FOR doc IN #collection
            COLLECT referenceType = doc.referenceType INTO paramReferenceTypes
            SORT referenceType ASC
            #pageable
            RETURN { 
                referenceType: referenceType,
                name: UNIQUE(paramReferenceTypes[*].doc.name),
                accession: UNIQUE(paramReferenceTypes[*].doc.accession)
            }
        """
    )
    public Page<GroupedCvParameters> findAllGroupedByReferenceType(Pageable p);
}
