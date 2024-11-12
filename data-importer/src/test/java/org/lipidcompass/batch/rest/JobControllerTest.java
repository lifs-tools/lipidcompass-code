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
package org.lipidcompass.batch.rest;

import org.lipidcompass.batch.rest.dto.JobDto;
import org.lipidcompass.batch.rest.dto.JobInstanceDto;
import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.arangodb.springframework.core.ArangoOperations;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lipidcompass.backend.repository.CvParameterRepository;
import org.lipidcompass.batch.jobs.JobManagerService;
import org.lipidcompass.batch.jobs.SmilesService;
import org.lipidcompass.batch.rest.JobControllerTest.MvcIntegrationTestConfiguration;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 *
 * @author nilshoffmann
 */
@WebMvcTest(JobController.class)
@Import(value = {MvcIntegrationTestConfiguration.class, JobController.class})
@ActiveProfiles(profiles = {"test", "dev"})
@ExtendWith(SpringExtension.class)
@Testcontainers
@Slf4j
@Disabled
//@EnableAutoConfiguration(exclude={ArangodbConfiguration.class, ArangoDbIntegrationTestConfiguration.class})
public class JobControllerTest {

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
    public static class MvcIntegrationTestConfiguration implements ArangoConfiguration {

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
    private WebApplicationContext context;

    @MockBean
    private JobManagerService jobManagerService;

    @Autowired
    private ArangoOperations arangoOperations;

    @MockBean
    private SmilesService smilesService;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testGetJobsFail() throws Exception {
        JobDto jobDto = new JobDto();
        jobDto.setName("job1");
        JobInstanceDto jobInstanceDto = new JobInstanceDto();
        jobDto.setJobInstances(Arrays.asList(jobInstanceDto));
        Page<JobDto> p = new PageImpl<>(Arrays.asList(jobDto), Pageable.unpaged(), 1);
        Mockito.when(jobManagerService.getJobDtos(Pageable.unpaged())).thenReturn(p);
        this.mockMvc.perform(get("/jobs")).andExpect(authenticated().withUsername("user").withRoles("USER")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testGetJobs() throws Exception {
        this.mockMvc.perform(get("/jobs")).andDo(print()).andExpect(authenticated().withUsername("admin").withRoles("ADMIN")).andExpect(status().isOk());
    }

    @Test
    public void testFindRunningJobExecutions() {
    }

    @Test
    public void testStartIndexingJob() throws Exception {
    }

    @Test
    public void testCanonicalizeSmiles() throws Exception {
    }

    @Test
    public void testStartJob_3args() throws Exception {
    }

    @Test
    public void testStartJob_String_Authentication() throws Exception {
    }

    @Test
    public void testGetJobStatus() {
    }

}
