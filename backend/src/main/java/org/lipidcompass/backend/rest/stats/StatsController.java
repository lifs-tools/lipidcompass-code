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
package org.lipidcompass.backend.rest.stats;

import static org.lipidcompass.backend.config.CachingConfiguration.DBSTATS;
import org.lipidcompass.backend.repository.CrossReferenceRepository;
import org.lipidcompass.backend.repository.CvParameterRepository;
import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.lipidcompass.backend.repository.LipidRepository;
import org.lipidcompass.backend.repository.MzTabResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lipidcompass.data.model.dto.DatabaseStatistics;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.Date;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.repository.DatabaseInfoRepository;
import org.lipidcompass.data.model.DatabaseInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.lipidcompass.backend.repository.StudyRepository;
//import org.lipidcompass.backend.repository.LipidMapsLipidRepository;

/**
 *
 * @author nilshoffmann
 */
@RestController
@ExposesResourceFor(DatabaseStatistics.class)
@RequestMapping(value = "/stats",
        produces = "application/hal+json")
@Slf4j
public class StatsController {

    @Autowired
    private DatabaseInfoRepository databaseInfoRepository;
    @Autowired
    private LipidRepository lipids;
    @Autowired
    private LipidQuantityRepository quantifiedLipids;
    @Autowired
    private StudyRepository experiments;
    @Autowired
    private MzTabResultRepository results;
    @Autowired
    private CvParameterRepository cvParameters;
    @Autowired
    private CrossReferenceRepository crossReferences;

    @PreAuthorize("permitAll()")
    @Cacheable(DBSTATS)
    @GetMapping(path = {"", "/"})
    public ResponseEntity<LipidStatsResource> stats(Authentication authentication) {
        DatabaseStatistics dbStats = new DatabaseStatistics();
        try {
            Iterable<DatabaseInfo> iterable = databaseInfoRepository.findAll();
            if (iterable != null) {
                Iterator<DatabaseInfo> iterator = databaseInfoRepository.findAll().iterator();
                if (iterator.hasNext()) {
                    DatabaseInfo di = iterator.next();
                    dbStats.setReleaseVersion(di.getReleaseVersion());
                    dbStats.setReleaseDate(di.getReleaseDate());
                } else {
                    dbStats.setReleaseVersion("UNDEFINED");
                    dbStats.setReleaseDate(new Date());
                }
            } else {
                dbStats.setReleaseVersion("UNDEFINED");
                dbStats.setReleaseDate(new Date());
            }
            dbStats.setCategories(lipids.countByLipidLevel(LipidLevel.CATEGORY));
            dbStats.setClasses(lipids.countByLipidLevel(LipidLevel.CLASS));
            dbStats.setSpecies(lipids.countByLipidLevel(LipidLevel.SPECIES));
            dbStats.setMolecularSpecies(lipids.countByLipidLevel(LipidLevel.MOLECULAR_SPECIES));
            dbStats.setStructureDefined(lipids.countByLipidLevel(LipidLevel.STRUCTURE_DEFINED));
            dbStats.setSnPosition(lipids.countByLipidLevel(LipidLevel.SN_POSITION));
            dbStats.setCompleteStructure(lipids.countByLipidLevel(LipidLevel.COMPLETE_STRUCTURE));
            dbStats.setFullStructure(lipids.countByLipidLevel(LipidLevel.FULL_STRUCTURE));
            dbStats.setCrossReferences(crossReferences.countPublic());
            dbStats.setExperiments(experiments.countPublic());
            dbStats.setResults(results.countPublic());
            dbStats.setLipidQuantities(quantifiedLipids.countPublic());
            dbStats.setTaxonomicSpecies(cvParameters.countByReferenceTypeDistinct(HasCvParameterReference.ReferenceType.SAMPLE_ORGANISM));
            dbStats.setTissues(cvParameters.countByReferenceTypeDistinct(HasCvParameterReference.ReferenceType.SAMPLE_TISSUE));
            dbStats.setCellTypes(cvParameters.countByReferenceTypeDistinct(HasCvParameterReference.ReferenceType.SAMPLE_CELLTYPE));
            dbStats.setDiseases(cvParameters.countByReferenceTypeDistinct(HasCvParameterReference.ReferenceType.SAMPLE_DISEASE));
            dbStats.setStudyVariableFactors(cvParameters.countByReferenceTypeDistinct(HasCvParameterReference.ReferenceType.STUDY_VARIABLE_FACTOR));
        } catch(RuntimeException re) {
            log.error("Database repositories may not have been initialized yet!", re);
            dbStats.setReleaseVersion("UNDEFINED");
            dbStats.setReleaseDate(new Date());
        }
        return ResponseEntity.ok(new LipidStatsResource(dbStats, authentication));
    }
}
