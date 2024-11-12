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
import org.lipidcompass.data.model.MzTabData;
import org.lipidcompass.data.model.dto.MzTabDataCvTermsSummary;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
//@RepositoryRestResource(collectionResourceRel = "lipids", path = "lipid")
@Repository
public interface MzTabDataRepository extends SecuredArangoRepository<MzTabData>, CustomArangoRepository<MzTabData, String> {

//    public Page<MzTabResult> findByNativeId(String nativeId, Pageable pageable);
//    public Page<MzTabResult> findBySubmissionStatus(SubmissionStatus publicationStatus, Pageable pageable);
    @Query("""
           FOR doc IN @@collection
             FILTER doc.`_key` == @mzTabResultId
             RETURN {    mzTabResultId: @mzTabResultId,
               sampleSpecies: FLATTEN(UNIQUE(doc.mzTab.metadata.sample[*].species[*])),
               sampleTissue:  FLATTEN(UNIQUE(doc.mzTab.metadata.sample[*].tissue[*])),
               sampleDisease: FLATTEN(UNIQUE(doc.mzTab.metadata.sample[*].disease[*])),
               sampleCelltype: FLATTEN(UNIQUE(doc.mzTab.metadata.sample[*].cellType[*])),
               sampleCustom:  FLATTEN(UNIQUE(doc.mzTab.metadata.sample[*].custom[* RETURN { cvLabel: CURRENT.cvLabel, cvAccession: CURRENT.cvAccession, name: CURRENT.name } ])),
               msRunFormat: FLATTEN(UNIQUE(doc.mzTab.metadata.msRun[*].format[*])),
               msRunIdFormat: FLATTEN(UNIQUE(doc.mzTab.metadata.msRun[*].idFormat[*])),
               msRunScanPolarity: FLATTEN(UNIQUE(doc.mzTab.metadata.msRun[*].scanPolarity[*])),
               msRunFragmentation: FLATTEN(UNIQUE(doc.mzTab.metadata.msRun[*].fragmentationMethod[*])),
               assayCustom: FLATTEN(UNIQUE(doc.mzTab.metadata.assay[*].custom[* RETURN { cvLabel: CURRENT.cvLabel, cvAccession: CURRENT.cvAccession, name: CURRENT.name } ])),
               studyVariableFactors: FLATTEN(UNIQUE(doc.mzTab.metadata.studyVariable[*].factors[*])),
               custom: FLATTEN(UNIQUE(doc.mzTab.metadata.custom[*])),
               cvs: FLATTEN(UNIQUE(doc.mzTab.metadata.cv[* RETURN {label: CURRENT.label , fullName: CURRENT.fullName, version: CURRENT.version, uri: CURRENT.uri}])),
               database: FLATTEN(UNIQUE(doc.mzTab.metadata.database[* RETURN {param: CURRENT.param, prefix: CURRENT.prefix, version: CURRENT.version, uri: CURRENT.uri}])),
               instrument: FLATTEN(UNIQUE(doc.mzTab.metadata.instrument[* RETURN {name: CURRENT.name, source: CURRENT.source, analyzer: CURRENT.analyzer, detector: CURRENT.detector}])),
               quantificationMethod: doc.mzTab.metadata.quantificationMethod,
               smallMoleculeQuantificationUnit: doc.mzTab.metadata.smallMoleculeQuantificationUnit,
               smallMoleculeFeatureQuantificationUnit: doc.mzTab.metadata.smallMoleculeFeatureQuantificationUnit,
               smallMoleculeIdentificationReliability: doc.mzTab.metadata.smallMoleculeIdentificationReliability,
               idConfidenceMeasure: FLATTEN(UNIQUE(doc.mzTab.metadata.idConfidenceMeasure[* RETURN {cvLabel: CURRENT.cvLabel, cvAccession: CURRENT.cvAccession, name:CURRENT.name}]))
             }
           """)
    public MzTabDataCvTermsSummary findUniqueCvTerms(String mzTabResultId);
}
