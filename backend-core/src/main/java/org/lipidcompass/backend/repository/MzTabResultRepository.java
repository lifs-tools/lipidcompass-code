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
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.SubmissionStatus;
import org.lipidcompass.data.model.dto.CvParameterFacet;
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
public interface MzTabResultRepository extends SecuredArangoRepository<MzTabResult>, CustomArangoRepository<MzTabResult, String> {

    public Optional<MzTabResult> findByNativeId(String nativeId);
    
    public Page<MzTabResult> findBySubmissionStatus(SubmissionStatus submissionStatus, Pageable pageable);
    
    @Query(
    """
    WITH 
        parameters,
        mzTabResult
    LET filterAccs = @selectedFacets ? FLATTEN(
        FOR cvParam in @selectedFacets[*].facetValues
            RETURN(cvParam[*].accession)
    ) : []

    // collect all distinct cvParameters for those results for which at least one cvParameter in the selected facets has been selected
    LET allParams = filterAccs ? FLATTEN(
        FOR q in #collection
            FILTER q != null
            FILTER q.visibility == "PUBLIC"
            LET cvParams = q.cvParameters ? q.cvParameters : []
            LET qAccs = (
                FOR cvParam in cvParams
                    RETURN(DOCUMENT("parameters", cvParam).accession)
            ) || []
            LET accIntersection = INTERSECTION(filterAccs, qAccs)
            FILTER (@selectedFacets == null || LENGTH(@selectedFacets)==0) ? true : LENGTH(accIntersection) == LENGTH(@selectedFacets)
            LET params = (
                FOR cvParam in cvParams
                    LET cvp = DOCUMENT("parameters", cvParam)
                    RETURN({accession:cvp.accession, name:cvp.name, referenceType:cvp.referenceType})
            ) || []
            RETURN(params)
    , 1) : []

    LET facets = allParams ? (
        FOR u IN allParams
            COLLECT 
                referenceType = u.referenceType,
                accession = u.accession,
                name = u.name
            AGGREGATE count = LENGTH(1)
            RETURN {
                referenceType,
                accession,
                name,
                count
            }
    ) : []

    FOR u IN facets
        COLLECT 
            referenceType = u.referenceType
        INTO facetValues = { 
            "accession": u.accession, "name": u.name, "count": u.count 
        }
        SORT referenceType ASC
        RETURN {
            referenceType,
            facetValues
        }
    """
    )
    public List<CvParameterFacet> getFacets(@Param("selectedFacets") @NotNull List<CvParameterFacet> selectedFacets);

    // filter by cv params and unit first, then by names
    @Query(
    """
    WITH 
        parameters,
        mzTabResult,
        lipidQuantity,
        lipids
    FOR q in #collection
        FILTER q.visibility == "PUBLIC"
        FILTER (@mzTabResultIds == null || @mzTabResultIds == null || LENGTH(@mzTabResultIds)==0) ? true:(@mzTabResultIds ANY == q._key)
        LET params = (
            FOR cvParam in q.cvParameters
                LET cvp = DOCUMENT("parameters", cvParam)
                LET matchingParams = (FOR selectedParam in @selectedFacets
                    FILTER selectedParam.facetValues[*].accession ANY == cvp.accession
                    RETURN(1)
                )
                RETURN(SUM(matchingParams))
        )
        FILTER SUM(params) == LENGTH(@selectedFacets[*].facetValues[*].accession)
        LET matches = (
            FOR quantity, hasQuantity, path IN OUTBOUND q hasAssayLipidQuantity
                LET lipid = DOCUMENT(quantity.lipid)
                FILTER (@nameAttributeForLevel == null || @shorthandLipidNames == null || LENGTH(@shorthandLipidNames)==0) ? true: (@shorthandLipidNames ANY == lipid.@nameAttributeForLevel || @shorthandLipidNames ANY == lipid.normalizedShorthandName)
                COLLECT WITH COUNT INTO length
                RETURN(length)
        )
        FILTER SUM(matches) > 0
        #pageable
        RETURN(q)
    """)
    public Page<MzTabResult> findByQuery(@Param("mzTabResultIds") List<String> mzTabResultIds, @Param("nameAttributeForLevel") String nameAttributeForLevel, @Param("shorthandLipidNames") List<String> shorthandLipidNames, @Param("selectedFacets") List<CvParameterFacet> selectedFacets, Pageable pageable);
}
