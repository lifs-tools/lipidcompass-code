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
import com.arangodb.springframework.annotation.QueryOptions;
import org.lipidcompass.data.model.Lipid;
import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.List;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.dto.IsobaricLipids;
import org.lipidcompass.data.model.dto.query.MatchMode;
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
public interface LipidRepository extends SecuredArangoRepository<Lipid>, CustomArangoRepository<Lipid, String> {

//    public Lipid findByNativeId(String nativeId);
    @Query("FOR e IN #collection FILTER e.lipidMapsCategory==CONCAT('lipidMapsEntries/', @lipidMapsCategory) && e.visibility == \"PUBLIC\" #pageable RETURN e")
    public Page<Lipid> findByLipidMapsCategory(String lipidMapsCategory, Pageable pageable);

    @Query("FOR e IN #collection FILTER e.lipidMapsCategory==CONCAT('lipidMapsEntries/', @lipidMapsCategory) && e.visibility == \"PUBLIC\" COLLECT WITH COUNT INTO length RETURN length")
    public Long countByLipidMapsCategory(String lipidMapsCategory);

    @Query("FOR e IN #collection FILTER e.lipidMapsMainClass==CONCAT('lipidMapsEntries/', @lipidMapsMainClass) && e.visibility == \"PUBLIC\" #pageable RETURN e")
    public Page<Lipid> findByLipidMapsMainClass(String lipidMapsMainClass, Pageable pageable);

    @Query("FOR e IN #collection FILTER e.lipidMapsMainClass==CONCAT('lipidMapsEntries/', @lipidMapsMainClass) && e.visibility == \"PUBLIC\" COLLECT WITH COUNT INTO length RETURN length")
    public Long countByLipidMapsMainClass(String lipidMapsSubClass);

    @Query("FOR e IN #collection FILTER e.lipidMapsSubClass==CONCAT('lipidMapsEntries/', @lipidMapsSubClass) && e.visibility == \"PUBLIC\" #pageable RETURN e")
    public Page<Lipid> findByLipidMapsSubClass(String lipidMapsSubClass, Pageable pageable);

    @Query("FOR e IN #collection FILTER e.lipidMapsSubClass==CONCAT('lipidMapsEntries/', @lipidMapsSubClass) && e.visibility == \"PUBLIC\" COLLECT WITH COUNT INTO length RETURN length")
    public Long countByLipidMapsSubClass(String lipidMapsSubClass);

    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel && e.visibility == \"PUBLIC\" COLLECT WITH COUNT INTO length RETURN length")
    public Long countByLipidLevel(@Param("lipidLevel") LipidLevel lipidLevel);

    @Query("FOR e IN #collection FILTER e.lipidLevel==@lipidLevel && e.visibility == \"PUBLIC\" #pageable RETURN e")
    public Page<Lipid> findByLipidLevel(@Param("lipidLevel") LipidLevel lipidLevel, Pageable pageable);

    public Page<Lipid> findByCommonNameOrderByCommonNameAsc(String commonName, Pageable pageable);

    /* LipidQuery methods */
    @Query("FOR e IN #collection\n"
            + "  FILTER e.visibility == \"PUBLIC\"\n"
            + "  FILTER ((@lipidLevel == null || @lipidLevel == \"\" || @lipidLevel == \"UNDEFINED_LEVEL\") ? true : (e.lipidLevel==@lipidLevel))\n"
            + "  FILTER ((@sumFormula == null || @sumFormula == \"\") ? true : (e.chemicalFormula==null ? false : e.chemicalFormula == @sumFormula))\n"
            + "  FILTER ((@minMass==null || @maxMass==null) ? true : (e.exactMass==null ? false : (e.exactMass >= @minMass && e.exactMass <= @maxMass)))\n"
            + "  FILTER (@normalizedShorthandNames == null || LENGTH(@normalizedShorthandNames)==0) ? true : (@matchMode == \"EXACT\" ? (@normalizedShorthandNames ANY == e.normalizedShorthandName) : true)\n"
            + "  FILTER (@normalizedShorthandNames == null || LENGTH(@normalizedShorthandNames)==0) ? true : (@matchMode == \"PREFIX\" ? (STARTS_WITH(e.normalizedShorthandName, @normalizedShorthandNames)) : true)\n"
            + "  FILTER (@normalizedShorthandNames == null || LENGTH(@normalizedShorthandNames)==0) ? true : (@matchMode == \"FUZZY\" ? (LIKE(e.normalizedShorthandName, @normalizedShorthandNames)) : true)\n"
            + "#pageable\n"
            + "RETURN e\n")
    public Page<Lipid> findByLipidLevelAndNormalizedShorthandNameMatches(
            @Param("lipidLevel") LipidLevel lipidLevel,
            @Param("sumFormula") String sumFormula,
            @Param("minMass") Double minMass,
            @Param("maxMass") Double maxMass,
            @Param("normalizedShorthandNames") List<String> normalizedShorthandNames,
            @Param("matchMode") MatchMode matchMode,
            Pageable pageable);

    public Page<Lipid> findBySynonyms(String synonym, Pageable pageable);

    public Page<Lipid> findBySystematicName(String systematicName, Pageable pageable);

    public Page<Lipid> findByChemicalFormula(String chemicalFormula, Pageable pageable);

    public Page<Lipid> findByExactMassOrderByExactMassAsc(Float exactMass, Pageable pageable);

    public Page<Lipid> findByInchiKey(String inchiKey, Pageable pageable);

    public Page<Lipid> findByInchi(String inchi, Pageable pageable);

    public Page<Lipid> findBySmiles(String smiles, Pageable pageable);

    @Query(
            "FOR doc IN #collection\n"
            + "  FILTER doc.visibility == \"PUBLIC\"\n"
            + "  LET PREC = @precision?POW(10, MIN([MAX([0, @precision]),8])):10000.0\n"
            + "  COLLECT exactMass = round(doc.exactMass*PREC)/PREC INTO isobars = {\n"
            + "    \"id\": doc._id,\n"
            + "    \"normalizedShorthandName\" : doc.normalizedShorthandName, \n"
            + "    \"lipidLevel\": doc.lipidLevel,\n"
            + "    \"chemicalFormula\": doc.chemicalFormula\n"
            + "  }\n"
            + "  FILTER exactMass > 0\n"
            + "  #pageable\n"
            + "  RETURN { \"exactMass\": exactMass, \"count\": length(isobars), \"isobars\": isobars}"
    )
    public Page<IsobaricLipids> findByExactMassIsobaric(Integer precision, Pageable pageable);

    @Query(
            "FOR doc1 IN #collection\n"
            + "  FOR doc2 IN swissLipidsEntries\n"
            + "    FILTER doc1.normalizedShorthandName == doc2.normalizedName\n"
            + "    FILTER doc2.smiles != \"\"\n"
            + "    UPDATE { _key: doc1._key, smiles: doc2.smiles } IN #collection"
    )
    public void annotateWithSwissLipidsSmiles();

    @Query(
            """
    FOR doc IN #collection
      FILTER doc.visibility == "PUBLIC"
      FILTER ["SPECIES", "MOLECULAR_SPECIES", "SN_POSITION", "STRUCTURE_DEFINED", "FULL_STRUCTURE", "COMPLETE_STRUCTURE"] ANY == doc.lipidLevel 
      SORT RAND() 
      LIMIT 1 
      RETURN doc
    """
    )
    public Lipid random(AqlQueryOptions options);

}
