/*
 * Copyright 2024 The LipidCompass Developers.
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

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *
 * @author nilshoffmann
 */
@SpringBatchTest
@SpringJUnitConfig
public class MzTabImportTestIT {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public JobParameters defaultJobParameters() {
        Map<String, JobParameter<?>> parameters = new HashMap<String, JobParameter<?>>();
        return new JobParameters(parameters);
    }

    @Test
    public void testJob(@Autowired Job job) throws Exception {
        this.jobLauncherTestUtils.setJob(job);
        

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());


        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
