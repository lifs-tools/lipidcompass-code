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
package org.lipidcompass.backend.bootstrap;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.lipidcompass.backend.repository.ControlledVocabularyRepository;
import org.lipidcompass.backend.repository.CrossReferenceRepository;
import org.lipidcompass.backend.repository.DatabaseInfoRepository;
import org.lipidcompass.backend.repository.HasCvParameterReferenceRepository;
import org.lipidcompass.backend.repository.HasMzTabResultRepository;
import org.lipidcompass.backend.repository.StudyRepository;
import org.lipidcompass.config.LipidCompassProperties;
import org.lipidcompass.data.model.DatabaseInfo;
import static org.lipidcompass.data.model.Visibility.PUBLIC;
import org.lipidcompass.data.model.roles.SystemRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.arangodb.entity.arangosearch.AnalyzerType;
import com.arangodb.entity.arangosearch.CollectionLink;
import com.arangodb.entity.arangosearch.StoreValuesType;
import com.arangodb.model.arangosearch.ArangoSearchCreateOptions;
import com.arangodb.springframework.config.ArangoEntityClassScanner;
import com.arangodb.springframework.core.ArangoOperations;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Profile({"dev", "integ"})
@Component
public class DevIntegBootstrap {

    @Value("${arangodb.database:lipidcompass}")
    private String database;

    @Value("${arangodb.drop:false}")
    private boolean drop;

    @Autowired
    private DatabaseInfoRepository databaseInfoRepository;
    @Autowired
    private CrossReferenceRepository crossReferenceRepository;
    @Autowired
    private ControlledVocabularyRepository controlledVocabularyRepository;
    @Autowired
    private HasCvParameterReferenceRepository hasCvParameterReferenceRepository;
    @Autowired
    private StudyRepository experimentRepository;
    @Autowired
    private HasMzTabResultRepository hasMzTabResultRepository;
    @Autowired
    private ArangoOperations arangoOperations;
    @Autowired
    private AuthorityAndUserBootstrap authorityAndUserBootstrap;
    @Autowired
    private LipidCompassProperties lipidCompassProperties;
    @Autowired
    private BootstrapUserProvider bootstrapUserProvider;

    @PostConstruct
    public void init() {
        try {

            if (drop) {
                log.info("Dropping database {}", database);
                arangoOperations.dropDatabase();
                arangoOperations.driver().createDatabase(database);
            }
            log.info("Creating collections for database {}", database);
            try {
                ArangoEntityClassScanner.scanForEntities("org.lipidcompass.data.model",
                        "org.lipidcompass.data.model.relations").stream().forEach((c) -> {
                            log.info("Initializing collection for entity {}", c);
                            arangoOperations.collection(c).count();
                        });
            } catch (ClassNotFoundException e) {
                log.error("Could not find class for collection!", e);
            }
            authorityAndUserBootstrap.init();
            bootstrapUserProvider.setSecurityContextUser(lipidCompassProperties.getSystemAdminName(), new SimpleGrantedAuthority(SystemRole.ADMIN.toString()));
            databaseInfoRepository.deleteAll();
            databaseInfoRepository.save(DatabaseInfo.builder().visibility(PUBLIC).transactionUuid(UUID.randomUUID().toString()).releaseDate(new Date()).releaseVersion("DEVELOPMENT").build());

//            ControlledVocabulary ncbiTaxon = new ControlledVocabulary("NCBITaxon", "NCBI Taxonomy", "http://purl.obolibrary.org/obo/ncbitaxon.owl", "2020-04-18");
//            controlledVocabularyRepository.save(ncbiTaxon);
//            
//            CrossReference ncbiTaxonMm = crossReferenceRepository.save(new CrossReference(CrossReferenceType.CV_TERM_ID,
//                    "http://purl.obolibrary.org/obo/NCBITaxon_10090", "NCBITaxon:10090"));
//
//            Organism og = new Organism("NCBITaxon:10090", "Mus musculus", "House Mouse", Collections.emptyList(), Collections.emptyList());
//            Organism musMusculus = organismRepository.save(og);
//
//            HasOrganismCrossReference hasNcbiMM = findOrUpdateEdge(new HasOrganismCrossReference(
//                    musMusculus, ncbiTaxonMm), hasOrganismCrossReferenceRepository);
//            log.info("{}", musMusculus);
//            log.info("{}", ncbiTaxonMm);
//            log.info("{}", hasNcbiMM);
//            CrossReference ncbiTaxonHs = crossReferenceRepository.save(new CrossReference(CrossReferenceType.CV_TERM_ID,
//                    "http://purl.obolibrary.org/obo/NCBITaxon_9606", "NCBITaxon:9606"));
//
//            Organism homoSapiens = organismRepository.save(new Organism("NCBITaxon:9606", "Homo sapiens", "Human", Collections.emptyList(), Collections.emptyList()));
//
//            HasOrganismCrossReference hasNcbiHs = findOrUpdateEdge(new HasOrganismCrossReference(
//                    homoSapiens, ncbiTaxonHs), hasOrganismCrossReferenceRepository);
//            ExperimentBuilder eb = Experiment.builder();
//            eb.name("LC23215").description("A test experiment").visibility(Visibility.PRIVATE);
//            Experiment exp = experimentRepository.save(eb.build());
////        exp = findOrUpdate(exp, experimentRepository);
//
//            CrossReference btoId = crossReferenceRepository.save(new CrossReference(CrossReferenceType.CV_TERM_ID, "http://purl.obolibrary.org/obo/BTO_0000131", "BTO:0000131"));
//            Tissue tissue = tissueRepository.save(new Tissue("BTO:0000131", "blood plasma", "The fluid portion of the blood in which the particulate components are suspended. [ Dorlands_Medical_Dictionary:MerckSource ] ", Collections.emptyList(), Collections.emptyList()));
//
//            findOrUpdateEdge(new HasTissueCrossReference(
//                    tissue, btoId), hasTissueCrossReferenceRepository);
//            IsExperimentWithOrganism ho = findOrUpdateEdge(new IsExperimentWithOrganism(exp, homoSapiens), hasOrganismRepository);
//            IsExperimentWithTissue ht = findOrUpdateEdge(new IsExperimentWithTissue(exp, tissue), hasTissueRepository);
//            arangoOperations.driver().db(database).arangoSearch("lipidQuantityFacetSearch").drop();
            ArangoSearchCreateOptions asco = new ArangoSearchCreateOptions();
            CollectionLink lipidQuantityLink
                    = CollectionLink.on("lipidQuantity").includeAllFields(Boolean.TRUE).analyzers(AnalyzerType.identity.toString()).storeValues(StoreValuesType.NONE).trackListPositions(Boolean.FALSE);
            CollectionLink lipidLink
                    = CollectionLink.on("lipids").includeAllFields(Boolean.TRUE).analyzers(AnalyzerType.identity.toString()).storeValues(StoreValuesType.NONE).trackListPositions(Boolean.FALSE);
            CollectionLink cvParamLink
                    = CollectionLink.on("parameters").includeAllFields(Boolean.TRUE).analyzers(AnalyzerType.identity.toString()).storeValues(StoreValuesType.NONE).trackListPositions(Boolean.FALSE);
            asco.link(lipidQuantityLink, lipidLink, cvParamLink);
//            arangoOperations.driver().db(database).createArangoSearch("lipidQuantityFacetSearch", asco);
        } finally {
            bootstrapUserProvider.clearSecurityContextUser();
        }
    }
}
