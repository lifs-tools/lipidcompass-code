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
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.PlottableLipidQuantity;
import org.lipidcompass.data.model.PlottableLipidSummaryStats;
import org.lipidcompass.data.model.dto.MzTabResultsForLipid;
import org.lifstools.jgoslin.domain.LipidLevel;
import de.isas.mztab2.model.Assay;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.StudyVariable;
import java.util.List;
import org.lipidcompass.data.model.LipidQuantityDatasetAssayTableRow;
import org.lipidcompass.data.model.dto.LipidsForMzTabResults;
import org.lipidcompass.data.model.dto.SummarizedLipidDataset;
import org.lipidcompass.data.model.dto.SummarizedLipidStatistics;
import org.lipidcompass.data.model.dto.query.LipidCompassQuery;
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
public interface LipidQuantityRepository extends SecuredArangoRepository<LipidQuantity>, CustomArangoRepository<LipidQuantity, String> {

//    public LipidQuantity findByNativeId(String nativeId);
    public Page<LipidQuantity> findByLipids(Lipid lipid, Pageable pageable);

//    public Page<LipidQuantity> findByTissue(Tissue tissue, Pageable pageable);
//    public Page<LipidQuantity> findByOrganism(Organism organism, Pageable pageable);
    @Query("""
           WITH 
             lipidQuantity,
             lipids,
             parameters
           FOR q in #collection
               FILTER q.visibility == "PUBLIC"
               FILTER (@mzTabResultIds == null || LENGTH(@mzTabResultIds) == 0) ? true: @mzTabResultIds ANY == q.mzTabResultId
               LET lipids = DOCUMENT("lipids", q.lipids), quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit)
               FILTER (@shorthandLipidNames == null || LENGTH(@shorthandLipidNames) == 0) ? true: @shorthandLipidNames ANY IN lipids[*].normalizedShorthandName
               COLLECT 
                   normalizedShorthandNames = CONCAT_SEPARATOR(" | ", lipids[*].normalizedShorthandName),
                   lipidClass = CONCAT_SEPARATOR(" | ", lipids[*].lipidClass),
                   lipidCategory = CONCAT_SEPARATOR(" | ", lipids[*].lipidCategory),
                   lipidIds = lipids[*]._key,
                   dataset = q.mzTabResultId,
                   quantificationUnit = quantificationUnitParam.name,
                   quantificationUnitAccession = quantificationUnitParam.accession
               AGGREGATE 
                   minAssayQuantity = MIN(q.assayQuantity), 
                   maxAssayQuantity = MAX(q.assayQuantity), 
                   averageAssayQuantity = AVERAGE(q.assayQuantity),
                   stddevAssayQuantity = STDDEV_SAMPLE(q.assayQuantity),
                   countAssayQuantity = COUNT(q.assayQuantity)
               INTO quantitiesByLipidName
               LET assayQuantityIds = quantitiesByLipidName[*].q._id
               LET assayQuantities = quantitiesByLipidName[*].q.assayQuantity
               LET perc25 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 25)
               LET perc50 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 50)
               LET perc75 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 75)
               LET iqr = (perc75 - perc25)
               LET lowerWhisker = MAX([perc25 - (1.5*iqr), minAssayQuantity])
               LET upperWhisker = MIN([perc75 + (1.5*iqr), maxAssayQuantity])
               #pageable
               RETURN { 
                   dataset,
                   normalizedShorthandNames,
                   lipidClass,
                   lipidCategory,
                   lipidIds,
                   quantificationUnitAccession, 
                   quantificationUnit, 
                   assayQuantityIds, 
                   assayQuantities, 
                   minAssayQuantity, 
                   averageAssayQuantity, 
                   maxAssayQuantity, 
                   stddevAssayQuantity, 
                   countAssayQuantity,
                   perc25,
                   perc50,
                   perc75,
                   iqr, 
                   lowerWhisker, 
                   upperWhisker 
               }
           """
    )
    public Page<PlottableLipidQuantity> findLipidQuantitiesGroupedByLipid(@Param("shorthandLipidNames") List<String> shorthandLipidNames, @Param("mzTabResultIds") List<String> mzTabResultIds, Pageable pageable);

    @Query("""
           WITH 
             lipidQuantity,
             lipids,
             parameters
           FOR q in #collection
               FILTER q.visibility == "PUBLIC"
               FILTER (@mzTabResultIds == null || LENGTH(@mzTabResultIds) == 0) ? true: @mzTabResultIds ANY == q.mzTabResultId
               LET lipids = DOCUMENT("lipids", q.lipids), quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit)
               FILTER (@shorthandLipidNames == null || LENGTH(@shorthandLipidNames) == 0) ? true: @shorthandLipidNames ANY IN lipids[*].normalizedShorthandName
               COLLECT 
                   normalizedShorthandNames = CONCAT_SEPARATOR(" | ", lipids[*].normalizedShorthandName),
                   lipidIds = lipids[*]._key,
                   dataset = q.mzTabResultId,
                   quantificationUnit = quantificationUnitParam.name,
                   quantificationUnitAccession = quantificationUnitParam.accession
               AGGREGATE 
                   minAssayQuantity = MIN(q.assayQuantity), 
                   maxAssayQuantity = MAX(q.assayQuantity), 
                   averageAssayQuantity = AVERAGE(q.assayQuantity),
                   stddevAssayQuantity = STDDEV_SAMPLE(q.assayQuantity),
                   countAssayQuantity = COUNT(q.assayQuantity)
               INTO quantitiesByLipidName
               LET perc25 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 25)
               LET perc50 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 50)
               LET perc75 = PERCENTILE(quantitiesByLipidName[*].q.assayQuantity, 75)
               LET iqr = (perc75 - perc25)
               LET lowerWhisker = MAX([perc25 - (1.5*iqr), minAssayQuantity])
               LET upperWhisker = MIN([perc75 + (1.5*iqr), maxAssayQuantity])
               #pageable
               RETURN { dataset, normalizedShorthandNames, lipidIds, quantificationUnit, quantificationUnitAccession, minAssayQuantity, averageAssayQuantity, maxAssayQuantity, stddevAssayQuantity, countAssayQuantity, perc25, perc50, perc75, iqr, lowerWhisker, upperWhisker}
           """
    )
    public Page<PlottableLipidSummaryStats> findLipidSummaryStatsGroupedByLipids(@Param("shorthandLipidNames") List<String> shorthandLipidNames, @Param("mzTabResultIds") List<String> mzTabResultIds, Pageable pageable);

    public Page<LipidQuantity> findByMzTabResultId(String mzTabResultId, Pageable pageable);

    public Page<LipidQuantity> findByAssay(Assay assay, Pageable pageable);

    public Page<LipidQuantity> findByStudyVariable(StudyVariable studyVariable, Pageable pageable);

    public Page<LipidQuantity> findByAssayQuantity(Double assayQuantity, Pageable pageable);

    public Page<LipidQuantity> findByAssayQuantityBetween(Double lower, Double upper, Pageable pageable);

    public Page<LipidQuantity> findByQuantificationUnit(Parameter quantificationUnit, Pageable pageable);

    public Page<LipidQuantity> findByQuantificationUnitAndAssayQuantityBetween(Parameter quantificationUnit, Double lower, Double upper, Pageable pageable);

    public Page<LipidQuantity> findByIdentificationReliability(Parameter identificationReliability, Pageable pageable);

    @Query("""
           FOR e IN #collection
               LET lipids = DOCUMENT("lipids", e.lipids)
               FILTER lipids[*].lipidLevel ANY ==@lipidLevel
               FILTER @normalizedShorthandNames ANY IN lipids[*].normalizedShorthandName
               #pageable
               RETURN e
           """
    )
    public Page<LipidQuantity> findByLipidLevelAndLipidNormalizedShorthandNameIn(@Param("lipidLevel") LipidLevel lipidLevel, @Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);

    @Query("""
           FOR e IN #collection
               LET lipids = DOCUMENT("lipids", e.lipids)
               FILTER lipids[*].lipidLevel ANY ==@lipidLevel
               LET matchingLipids = lipids[* FILTER STARTS_WITH(CURRENT.normalizedShorthandName, @normalizedShorthandNames)].normalizedShorthandName
               FILTER LENGTH(matchingLipids) > 0
               #pageable
               RETURN e
           """
    )
    public Page<LipidQuantity> findByLipidLevelAndLipidNormalizedShorthandNameStartsWithIn(@Param("lipidLevel") LipidLevel lipidLevel, @Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);

    @Query("""
           FOR e IN #collection
               LET lipids = DOCUMENT("lipids", e.lipids)
               LET matchingLipids = lipids[* FILTER STARTS_WITH(CURRENT.normalizedShorthandName, @normalizedShorthandNames)].normalizedShorthandName
               FILTER LENGTH(matchingLipids) > 0
               #pageable
               RETURN e
           """
    )
    public Page<LipidQuantity> findByLipidNormalizedShorthandNameIn(@Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);

    @Query("""
           FOR e IN #collection
               LET lipids = DOCUMENT("lipids", e.lipids)
               LET matchingLipids = lipids[* FILTER STARTS_WITH(CURRENT.normalizedShorthandName, @normalizedShorthandNames)].normalizedShorthandName
               FILTER LENGTH(matchingLipids) > 0
               #pageable
               RETURN e
           """
    )
    public Page<LipidQuantity> findByLipidNormalizedShorthandNameStartsWithIn(@Param("normalizedShorthandNames") List<String> normalizedShorthandNames, Pageable pageable);

    public default Page<LipidQuantity> findByLipidQuery(LipidCompassQuery query, Pageable p) {

        return Page.empty();
    }

    @Query("""
           WITH 
             lipidQuantity,
             lipids,
             parameters
           LET results = (
               FOR q IN lipidQuantity
                 FILTER q.visibility == "PUBLIC"
                 LET 
                   lipids = DOCUMENT("lipids", q.lipids), 
                   quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit)
                 COLLECT
                   namesForLevel = LENGTH(lipids[*].@nameAttributeForLevel) > 0 ? CONCAT_SEPARATOR(" | ", lipids[*].@nameAttributeForLevel) : null,
                   quantificationUnitParamName = quantificationUnitParam.name,
                   quantificationUnitParamAccession = quantificationUnitParam.accession
                 AGGREGATE
                   sumAssayQuantity = SUM(q.assayQuantity),
                   countAssayQuantity = COUNT(q.assayQuantity),
                   mzTabResultId = UNIQUE(q.mzTabResultId)
                 FILTER namesForLevel != null
                 SORT namesForLevel DESC
                 RETURN {
                   mzTabResultId: mzTabResultId, 
                   quantificationUnitParamName,
                   quantificationUnitParamAccession,
                   x: namesForLevel,
                   y: sumAssayQuantity,
                   groupCount: countAssayQuantity 
                 }
           )
           RETURN {
               lipidLevel: @nameAttributeForLevel,
               quantificationUnitParamName: FIRST(UNIQUE(results[*].quantificationUnitParamName)),
               quantificationUnitParamAccession: FIRST(UNIQUE(results[*].quantificationUnitParamAccession)),
               x: results[*].x,
               y: results[*].y,
               groupCount: results[*].groupCount,
               datasetCount: COUNT(UNIQUE(FLATTEN(results[*].mzTabResultId)))
           }
           """
    )
    public List<SummarizedLipidStatistics> findByLipidLevel(@Param("nameAttributeForLevel") String nameAttributeForLevel);

    @Query("""
           WITH 
             lipidQuantity,
             lipids,
             parameters
           LET results = (
               FOR q IN lipidQuantity
                 FILTER q.visibility == "PUBLIC"
                 FILTER q.mzTabResultId == @mzTabResultId
                 LET 
                   lipids = DOCUMENT("lipids", q.lipids), 
                   quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit)
                 COLLECT
                   namesForLevel = LENGTH(lipids[*].@nameAttributeForLevel) > 0 ? CONCAT_SEPARATOR(" | ", lipids[*].@nameAttributeForLevel) : null,
                   lipidClass = CONCAT_SEPARATOR(" | ", lipids[*].lipidClass),
                   lipidCategory = CONCAT_SEPARATOR(" | ", lipids[*].lipidCategory),
                   quantificationUnitParamName = quantificationUnitParam.name,
                   quantificationUnitParamAccession = quantificationUnitParam.accession
                 AGGREGATE
                   sumAssayQuantity = SUM(q.assayQuantity),
                   countAssayQuantity = COUNT(q.assayQuantity)
                 FILTER namesForLevel != null
                 SORT lipidCategory DESC, lipidClass DESC, namesForLevel DESC
                 RETURN {
                   mzTabResultId: @mzTabResultId,
                   quantificationUnitParamName,
                   quantificationUnitParamAccession,
                   x: namesForLevel,
                   y: sumAssayQuantity,
                   lipidCategory: lipidCategory,
                   lipidClass: lipidClass,
                   groupCount: countAssayQuantity 
                 }
           )
           RETURN {
               mzTabResultId: @mzTabResultId,
               lipidLevel: @nameAttributeForLevel,
               quantificationUnitParamName: FIRST(UNIQUE(results[*].quantificationUnitParamName)),
               quantificationUnitParamAccession: FIRST(UNIQUE(results[*].quantificationUnitParamAccession)),
               x: results[*].x,
               y: results[*].y,
               lipidCategory: results[*].lipidCategory,
               lipidClass: results[*].lipidClass,
               groupCount: results[*].groupCount
           }
           """
    )
    public SummarizedLipidDataset findyByMzTabResultIdAndByLipidLevel(@Param("mzTabResultId") String mzTabResultId, @Param("nameAttributeForLevel") String nameAttributeForLevel);

//    public Page<LipidQuantity> findByLipidQueryExpanded(
//            @Param("lipidLevel") LipidLevel lipidLevel,
//            @Param("name") String name,
//            @Param("exactMatch") boolean exactMatch,
//            @Param("normalizeName") boolean normalizeName,
//            @Param("unit") String unit,
//            @Param("mzRange") Double[] mzRange,
//            @Param("quantityRange") Double[] quantityRange,
//            @Param("mzTabResults") List<String> mzTabResults,
//            @Param("organisms") List<String> organisms,
//            @Param("tissues") List<String> tissues,
//            @Param("diseases") List<String> diseases,
//            @Param("cellTypes") List<String> cellTypes,
//            @Param("goTerms") List<String> goTerms
//            );
//    public Page<LipidQuantity> findByOrganismAndTissue(Organism organism, Tissue tissue, Pageable pageable);
//    public Page<LipidQuantity> findByLipidAndOrganismAndTissue(Lipid lipid, Organism organism, Tissue tissue, Pageable pageable);
    public Long countByLipids(Lipid lipid);

//    public Long countByOrganism(Organism organism);
//    public Long countByTissue(Tissue tissue);
    public Long countByMzTabResultId(String mzTabResultId);

    public Long countByAssay(Assay assay);

    public Long countByStudyVariable(StudyVariable studyVariable);

    public Long countByIdentificationReliability(Parameter identificationReliability);

    @Query(
            """
            WITH lipidQuantity, mzTabResult
            FOR q in #collection
                FILTER @lipidId IN q.lipids
                LET mzTabResultDocument = DOCUMENT("mzTabResult", q.mzTabResultId)
                RETURN DISTINCT { id: mzTabResultDocument._key, nativeId:mzTabResultDocument.nativeId }
            """
    )
    public List<MzTabResultsForLipid> findMzTabResultsForLipid(@Param("lipidId") String lipidId);

    @Query(
            """
            WITH mzTabResult, lipidQuantity, hasAssayLipidQuantity
            FOR startVertex IN @mzTabResultIds
                LET lipidIds = (
                    FOR v, e, p IN 1..1 OUTBOUND startVertex hasAssayLipidQuantity
                        LET lq = DOCUMENT(e._to)
                        RETURN DISTINCT lq.lipids
                )
            RETURN DISTINCT {lipidIds: UNIQUE(FLATTEN(lipidIds))}
            """
    )
    public LipidsForMzTabResults findLipidsForMzTabResults(@Param("mzTabResultIds") List<String> mzTabResultIds);

    @Query(
            """
            WITH 
             lipidQuantity,
             lipids,
             parameters,
             mzTabAssay
            FOR q in #collection
               FILTER q.visibility == "PUBLIC"
               FILTER (@mzTabResultIds == null || LENGTH(@mzTabResultIds) == 0) ? true: @mzTabResultIds ANY == q.mzTabResultId
               LET lipids = DOCUMENT("lipids", q.lipids), quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit), qassay = DOCUMENT("mzTabAssay", q.assay)
               FILTER (@shorthandLipidNames == null || LENGTH(@shorthandLipidNames) == 0 || LENGTH(lipids[*].normalizedShorthandName) == 0) ? true: @shorthandLipidNames ANY IN lipids[*].normalizedShorthandName
               COLLECT 
                   normalizedShorthandNames = CONCAT_SEPARATOR(" | ", lipids[*].normalizedShorthandName),
                   lipidLevel = lipids[*].lipidLevel,
                   lipidCategory = CONCAT_SEPARATOR(" | ", lipids[*].lipidCategory),
                   lipidClass = CONCAT_SEPARATOR(" | ", lipids[*].lipidClass),
                   lipidSpecies = CONCAT_SEPARATOR(" | ", lipids[*].lipidSpecies),
                   lipidMolecularSpecies = CONCAT_SEPARATOR(" | ", lipids[*].lipidMolecularSpecies),
                   lipidSnPosition = CONCAT_SEPARATOR(" | ", lipids[*].lipidSnPosition),
                   lipidStructureDefined = CONCAT_SEPARATOR(" | ", lipids[*].lipidStructureDefined),
                   lipidFullStructure = CONCAT_SEPARATOR(" | ", lipids[*].lipidFullStructure),
                   lipidCompleteStructure = CONCAT_SEPARATOR(" | ", lipids[*].lipidCompleteStructure),
                   lipidIds = lipids[*]._key,
                   dataset = q.mzTabResultId,
                   assay = qassay.assay.name==null ? qassay.assay.id : qassay.assay.name,
                   quantificationUnit = quantificationUnitParam.name,
                   quantificationUnitAccession = quantificationUnitParam.accession,
                   assayQuantity = q.assayQuantity,
                   assayRelativeQuantity = q.assayRelativeQuantity
               INTO quantitiesByLipidName
               #pageable
               RETURN { 
                   dataset,
                   assay,
                   normalizedShorthandNames,
                   lipidLevel,
                   lipidCategory,
                   lipidClass,
                   lipidSpecies,
                   lipidMolecularSpecies,
                   lipidSnPosition,
                   lipidStructureDefined,
                   lipidFullStructure,
                   lipidCompleteStructure,
                   lipidIds,
                   quantificationUnitAccession, 
                   quantificationUnit, 
                   assayQuantity,
                   assayRelativeQuantity
               }
            """
    )
    public Page<LipidQuantityDatasetAssayTableRow> findLipidQuantityTableByMzTabResultIdsAndNormalizedShorthandName(@Param("shorthandLipidNames") List<String> shorthandLipidNames, @Param("mzTabResultIds") List<String> mzTabResultIds, Pageable pageable);

    @Query(
            """
           WITH 
            lipidQuantity,
            lipids,
            parameters,
            mzTabAssay
           FOR q in lipidQuantity
               FILTER q.visibility == "PUBLIC"
               FILTER (@mzTabResultIds == null || LENGTH(@mzTabResultIds) == 0) ? true: @mzTabResultIds ANY == q.mzTabResultId
               LET lipids = DOCUMENT("lipids", q.lipids), quantificationUnitParam = DOCUMENT("parameters", q.quantificationUnit), qassay = DOCUMENT("mzTabAssay", q.assay)
               FILTER (@shorthandLipidNames == null || LENGTH(@shorthandLipidNames) == 0) ? true: @shorthandLipidNames ANY IN lipids[*].normalizedShorthandName
               COLLECT 
                   normalizedShorthandNames = CONCAT_SEPARATOR(" | ", lipids[*].normalizedShorthandName),
                   lipidLevel = lipids[*].lipidLevel,
                   lipidCategory = CONCAT_SEPARATOR(" | ", lipids[*].lipidCategory),
                   lipidClass = CONCAT_SEPARATOR(" | ", lipids[*].lipidClass),
                   lipidSpecies = CONCAT_SEPARATOR(" | ", lipids[*].lipidSpecies),
                   lipidMolecularSpecies = CONCAT_SEPARATOR(" | ", lipids[*].lipidMolecularSpecies),
                   lipidSnPosition = CONCAT_SEPARATOR(" | ", lipids[*].lipidSnPosition),
                   lipidStructureDefined = CONCAT_SEPARATOR(" | ", lipids[*].lipidStructureDefined),
                   lipidFullStructure = CONCAT_SEPARATOR(" | ", lipids[*].lipidFullStructure),
                   lipidCompleteStructure = CONCAT_SEPARATOR(" | ", lipids[*].lipidCompleteStructure),
                   lipidIds = lipids[*]._key,
                   dataset = q.mzTabResultId,
                   assay = qassay.assay.name==null ? qassay.assay.id : qassay.assay.name,
                   quantificationUnit = quantificationUnitParam.name,
                   quantificationUnitAccession = quantificationUnitParam.accession,
                   assayQuantity = q.assayQuantity,
                   assayRelativeQuantity = q.assayRelativeQuantity
               #pageable
               RETURN {
                   normalizedShorthandNames,
                   lipidLevel,
                   lipidCategory,
                   lipidClass,
                   lipidSpecies,
                   lipidMolecularSpecies,
                   lipidSnPosition,
                   lipidStructureDefined,
                   lipidFullStructure,
                   lipidCompleteStructure,
                   lipidIds,
                   dataset,
                   assay,
                   quantificationUnit,
                   quantificationUnitAccession,
                   assayQuantity,
                   assayRelativeQuantity
               }
           """
    )
    public Page<LipidQuantityDatasetAssayTableRow> findLipidQuantityTableForLipidSpace(@Param("shorthandLipidNames") List<String> shorthandLipidNames, @Param("mzTabResultIds") List<String> mzTabResultIds, Pageable pageable);
    // query to retrieve lipid counts for given lipids on level x and lower and number of distinct datasets they occur in
//    @Query(
//            "WITH mzTabResult, lipidQuantity, hasAssayLipidQuantity\n" +
//"FOR startVertex IN lipidQuantity\n" +
//"    LET doc = DOCUMENT(startVertex.lipid)\n" +
//"    FILTER doc[@aggregationLevel] IN @normalizedNames\n" +
//"    LET lipidsByClass = (\n" +
//"        FOR v, e, p IN 1..1 INBOUND startVertex hasAssayLipidQuantity\n" +
//"            FILTER e._from IN @mzTabResultIds\n" +
//"            LET lipid = DOCUMENT(startVertex.lipid)\n" +
//"            FILTER lipid[@aggregationLevel] IN @normalizedNames\n" +
//"            RETURN { lipid: lipid, mzTabResultKey: e._from }\n" +
//"    )\n" +
//"    FOR lipid IN lipidsByClass\n" +
//"        COLLECT level = @aggregationLevel, \n" +
//"                name = lipid.lipid[@aggregationLevel],\n" +
//"                numLipids = lipid.lipid[@aggregationLevel]\n" +
//"        AGGREGATE numDatasets = UNIQUE(lipid.mzTabResultKey)\n" +
//"        SORT name, numLipids DESC\n" +
//"        RETURN {level, name: name, numLipids: LENGTH(numLipids), numDatasets: COUNT(numDatasets)}"
//    )
    // facets with counts for lipid names on specific levels and below
//    
//    @Query(
//            "WITH mzTabResult, lipidQuantity, hasAssayLipidQuantity, lipids\n" +
//"LET lipidNames = LENGTH(@normalizedNames)>0?@normalizedNames:(\n" +
//"    FOR l in lipids\n" +
//"        FILTER l[@aggregationLevel] != null\n" +
//"        COLLECT name = l[@aggregationLevel]\n" +
//"        RETURN name\n" +
//")\n" +
//"FOR lipid IN lipids\n" +
//"    FILTER lipid[@aggregationLevel] IN lipidNames\n" +
//"    COLLECT level = @aggregationLevel, \n" +
//"            name = lipid[@aggregationLevel] WITH COUNT INTO numLipids\n" +
//"    SORT name, numLipids DESC\n" +
//"    RETURN {level, name: name, numLipids: numLipids}"
//    )
//    public Long countByOrganismAndTissue(Organism organism, Tissue tissue);
//    public Long countByLipidAndOrganismAndTissue(Lipid lipid, Organism organism, Tissue tissue);
    /* Return an overview of all lipids on all levels, reporting the FA summary */
    //WITH lipids
//FOR doc IN lipids
//  FILTER doc.visibility == "PUBLIC"
//  COLLECT
//    nameForLevel = doc.@nameAttributeForLevel,
//    lipidLevel = doc.lipidLevel,
//    lipidClass = doc.lipidClass,
//    lipidCategory = doc.lipidCategory,
//    nCarbon = doc.lipidSpeciesInfo.nCarbon,
//    nHydroxy = doc.lipidSpeciesInfo.nHydroxy,
//    nDoubleBonds = doc.lipidSpeciesInfo.nDoubleBonds,
//    faBondType = doc.lipidSpeciesInfo.faBondType
//  RETURN {
//    lipidLevel,
//    nameForLevel,
//    lipidCategory,
//    lipidClass,
//    nCarbon,
//    nHydroxy,
//    nDoubleBonds,
//    faBondType
//  }
    /* Return all lipids that contain a specific FA pattern */
// find vertices in fattyAcyls that match the query (and) 
// use these vertices to traverse the graph to the linked lipids
// return the list of lipids
// bonus: also return parent lipids? or limit the minimum level (e.g. species) of returned lipids?
}
