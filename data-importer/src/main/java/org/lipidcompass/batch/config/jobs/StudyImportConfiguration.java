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
package org.lipidcompass.batch.config.jobs;

import com.arangodb.springframework.core.ArangoOperations;
import org.lipidcompass.backend.repository.MzTabResultRepository;
import java.io.File;
import java.io.IOException;
import org.lipidcompass.backend.repository.HasMzTabResultRepository;
import org.lipidcompass.backend.repository.StudyRepository;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.batch.data.importer.study.StudyDto;
import org.lipidcompass.batch.data.importer.study.StudyItemReader;
import org.lipidcompass.batch.data.importer.study.StudyWriter;
import org.lipidcompass.config.LipidCompassProperties;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Import job configuration for mzTab-M imports.
 *
 * @author nilshoffmann
 */
@Configuration
public class StudyImportConfiguration {
    
    @Value("${mzTab.import.dir}")
    public File mzTabImportDir;

    @Bean("studyFileItemReader")
    @StepScope
    public StudyItemReader studyFileItemReader(@Value("#{jobParameters['fileName']}") String fileName) throws IOException {
        return new StudyItemReader(new FileSystemResource(new File(mzTabImportDir,
                fileName)));
    }

    @Bean
    @StepScope
    public StudyWriter studyWriter(
            StudyRepository studyRepository,
            MzTabResultRepository mzTabResultRepository,
            HasMzTabResultRepository hasMzTabResultRepository,
            UserRepository userRepository,
            ArangoOperations arangoOperations,
            LipidCompassProperties lipidCompassProperties,
            @Value("#{jobParameters['principal']}") String principal) {
        return new StudyWriter(
                studyRepository,
                mzTabResultRepository,
                hasMzTabResultRepository,
                userRepository,
                arangoOperations,
                principal,
                lipidCompassProperties
        );

    }

    @Bean
    public Step studyImport(JobRepository jobRepository, PlatformTransactionManager transactionManager, StudyWriter studyWriter, StudyItemReader studyItemReader) throws IOException {
        return new StepBuilder("studyImport", jobRepository).
                <StudyDto, StudyDto>chunk(1, transactionManager).
                reader(studyItemReader).
                faultTolerant().
                writer(studyWriter).
                build();
    }

    @Bean("studyImportJob")
    public Job studyImportJob(JobRepository jobRepository, JobExecutionListener listener, @Qualifier(
            "studyImport") Step studyImport) {
        return new JobBuilder("studyImportJob", jobRepository).
                incrementer(new RunIdIncrementer()).
                preventRestart().
                listener(listener).
                flow(studyImport).
                end().
                build();
    }
}
