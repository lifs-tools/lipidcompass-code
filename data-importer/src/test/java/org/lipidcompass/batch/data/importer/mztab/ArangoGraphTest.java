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
package org.lipidcompass.batch.data.importer.mztab;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.arangodb.springframework.core.ArangoOperations;
import java.util.Optional;
import java.util.UUID;
import org.lipidcompass.backend.repository.ControlledVocabularyRepository;
import org.lipidcompass.backend.repository.CvParameterRepository;
import org.lipidcompass.backend.repository.HasCvParentRepository;
//import org.lipidcompass.batch.data.importer.mztab.ArangoGraphTest.ArangoDbIntegrationTestConfiguration;
import org.lipidcompass.data.model.ControlledVocabulary;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.relations.HasCvParent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lipidcompass.data.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
//@Import(ArangoDbIntegrationTestConfiguration.class)
@Testcontainers
@Slf4j
@ActiveProfiles(profiles = {"test", "dev"})
public class ArangoGraphTest {

    public static final int exposedPort = 8529;

    @Container
    public static GenericContainer arangodbContainer = new GenericContainer("arangodb:3.12").
            withEnv("ARANGO_STORAGE_ENGINE", "rocksdb").
            withEnv("ARANGO_NO_AUTH", "1").
            withExposedPorts(exposedPort).
            waitingFor(
                    Wait.forLogMessage(".*is ready for business.*\\n", 1)
            );

    @EnableArangoRepositories(basePackageClasses = CvParameterRepository.class)
    @Configuration
    public static class ArangoDbIntegrationTestConfiguration implements ArangoConfiguration {

        @Override
        public ArangoDB.Builder arango() {
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
            arangodbContainer.followOutput(logConsumer);
            String address = arangodbContainer.getHost();
            Integer port = arangodbContainer.getMappedPort(exposedPort);
            log.info("Listening for container at " + address + " and port " + port);
            return new ArangoDB.Builder().user("root").password(null).host(address, port);
        }

        @Override
        public String database() {
            return "arangotest";
        }
    }

    @Autowired
    private ArangoOperations arangoOperations;

    @Autowired
    private CvParameterRepository cvParameterRepository;

    @Autowired
    private ControlledVocabularyRepository cvRepository;

    @Autowired
    private HasCvParentRepository hasCvParentRepository;

    @BeforeEach
    public void createCollections() {
        // first drop the database so that we can run this multiple times with the same dataset
        try {
            arangoOperations.dropDatabase();
        } catch (ArangoDBException ex) {
            log.error("Caught exception while trying to delete database!", ex);
        }
        arangoOperations.collection(CvParameter.class).count();
        arangoOperations.collection(ControlledVocabulary.class).count();
        arangoOperations.collection(HasCvParent.class).count();
    }

    @Test
    public void testInsertFindOneAndUpdate() {
        final String transactionUUID = UUID.randomUUID().toString();
        final ControlledVocabulary cv = ControlledVocabulary.builder().
                label("TST").
                name("Test CV").
                uri("https://lifs-tools.org").
                version("1.0").
                transactionUuid(transactionUUID).
                visibility(Visibility.PUBLIC).
                build();
        cvRepository.save(cv);

        final CvParameter cvParam = CvParameter.builder().
                accession("TST:1289712").
                name("a test parameter").
                transactionUuid(transactionUUID).
                visibility(Visibility.PUBLIC).
                cvParameterType(CvParameter.CvParameterType.MARKER).
                build();

        final HasCvParent cvParent = HasCvParent.builder().
                from(cvParam).
                to(cv).
                transactionUuid(transactionUUID).
                visibility(Visibility.PUBLIC).
                build();

////        ArangoGraph<CvParameter, ControlledVocabulary, HasCvParent> ag
////                = new ArangoGraph<>(
////                        arangoOperations,
////                        cvParameterRepository,
////                        cvRepository,
////                        hasCvParentRepository,
////                        HasCvParent.class
////                );
//        ArangoBaseEntity fromVertex = ag.addFromVertex(cvParam);
//        ArangoBaseEntity toVertex = ag.addToVertex(cv);
////        CeqEntityWrapper<
//        HasCvParent edge = ag.addEdge(
//                fromVertex, 
//                toVertex, 
//                cvParent
//        );
////        ag.
//        
//        ag.persist();
        Page<CvParameter> page = cvParameterRepository.findAllByAccession("TST:1289712", Pageable.unpaged());
        Assertions.assertNotNull(page.getTotalElements() == 1l);
        Assertions.assertNotNull(cvRepository.findByLabel("TST"));
        // save a single entity in the database
        // there is no need of creating the collection first. This happen automatically
        final CvParameter cvParam2 = CvParameter.builder().
                accession("MS:1234567").
                name("Some name").
                transactionUuid(transactionUUID).
                visibility(Visibility.PUBLIC).
                value("false").
                cvParameterType(CvParameter.CvParameterType.BOOLEAN).
                build();
        cvParameterRepository.save(cvParam2);
        // the generated id from the database is set in the original entity
        System.out.println(String.format("CV Parameter saved in the database with id: '%s'", cvParam2.getId()));

        // lets take a look whether we can find Ned Stark in the database
        final Optional<CvParameter> foundNed = cvParameterRepository.findById(cvParam2.getId());
        System.out.println(String.format("Found %s", foundNed));
        Assertions.assertEquals("MS:1234567", cvParam2.getAccession());

        cvParam2.setBooleanValue(false);
        cvParameterRepository.save(cvParam2);
        final Optional<CvParameter> updatedCvParameter = cvParameterRepository.findById(cvParam2.getId());
        // we need dead Ned to be present in order to check if he is really dead :-)
        Assertions.assertTrue(updatedCvParameter.isPresent());
        Assertions.assertFalse(updatedCvParameter.get().getBooleanValue());
    }

//    @Test
//    public void testInsertMultiple() {
//        Collection<FictionalCharacter> createCharacters = createCharacters();
//        System.out.println(String.format("Save %s additional chracters", createCharacters.size()));
//        cvParameterRepository.saveAll(createCharacters);
//
//        Iterable<FictionalCharacter> all = cvParameterRepository.findAll();
//        long count = StreamSupport.stream(Spliterators.spliteratorUnknownSize(all.iterator(), 0), false).count();
//        System.out.println(String.format("A total of %s characters are persisted in the database", count));
//        Assertions.assertEquals(44l, count);
//
//        // alternatively, and much more efficient
//        long fastCount = cvParameterRepository.count();
//        Assertions.assertEquals(44l, fastCount);
//    }
}
