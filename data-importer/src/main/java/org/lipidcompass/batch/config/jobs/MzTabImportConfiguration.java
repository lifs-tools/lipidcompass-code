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
import org.lipidcompass.backend.repository.ControlledVocabularyRepository;
import org.lipidcompass.backend.repository.CvParameterRepository;
import org.lipidcompass.backend.repository.HasAssayLipidQuantityRepository;
import org.lipidcompass.backend.repository.HasCvParameterReferenceRepository;
import org.lipidcompass.backend.repository.HasCvParentRepository;
import org.lipidcompass.backend.repository.HasLipidMapsReferenceRepository;
import org.lipidcompass.backend.repository.HasSwissLipidsReferenceRepository;
import org.lipidcompass.backend.repository.LipidMapsEntryRepository;
import org.lipidcompass.backend.repository.LipidQuantityRepository;
import org.lipidcompass.backend.repository.LipidRepository;
import org.lipidcompass.backend.repository.MzTabAssayRepository;
import org.lipidcompass.backend.repository.MzTabDataRepository;
import org.lipidcompass.backend.repository.MzTabResultRepository;
import org.lipidcompass.backend.repository.MzTabStudyVariableRepository;
import org.lipidcompass.backend.repository.SwissLipidsEntryRepository;
import org.lipidcompass.data.parser.GoslinAllGrammarsParser;
import org.lipidcompass.batch.data.importer.mztab.MzTabResultWriter;
import org.lipidcompass.batch.data.importer.mztab.MzTabResultItemReader;
import org.lipidcompass.batch.data.importer.mztab.MzTabResultProcessor;
import org.lipidcompass.data.model.MzTabResult;
import de.isas.mztab2.model.MzTab;
import java.io.File;
import java.io.IOException;
import org.lipidcompass.backend.repository.FattyAcylRepository;
import org.lipidcompass.backend.repository.HasFattyAcylRepository;
import org.lipidcompass.backend.repository.HasLipidQuantityLipidReferenceRepository;
import org.lipidcompass.backend.repository.HasMzTabResultRepository;
import org.lipidcompass.backend.repository.MzTabMsRunRepository;
import org.lipidcompass.backend.repository.StudyRepository;
import org.lipidcompass.backend.repository.SubmissionRepository;
import org.lipidcompass.batch.data.importer.mztab.MzTabResultMinioItemReader;
import org.lipidcompass.service.core.io.MinioStorageService;
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
public class MzTabImportConfiguration {

    @Value("${mzTab.import.dir}")
    public File mzTabImportDir;

    @Bean("mzTabFileItemReader")
    @StepScope
    public MzTabResultItemReader mzTabFileReader(@Value("#{jobParameters['fileName']}") String fileName) throws IOException {
        return new MzTabResultItemReader(new FileSystemResource(new File(mzTabImportDir,
                fileName)));
    }

    @Bean("mzTabFileMinioItemReader")
    @StepScope
    public MzTabResultMinioItemReader mzTabFileMinioReader(@Value("#{jobParameters['submissionId']}") String submissionId, SubmissionRepository submissionRepository, MinioStorageService storageService) throws IOException {
        return new MzTabResultMinioItemReader(submissionId, submissionRepository, storageService);
    }

    @Bean
    @StepScope
    public MzTabResultWriter mzTabResultWriter(
            MzTabResultRepository client,
            MzTabDataRepository mzTabDataRepository,
            LipidRepository lipidRepository,
            FattyAcylRepository fattyAcylRepository,
            LipidQuantityRepository lipidQuantityRepository,
            ControlledVocabularyRepository cvRepository,
            CvParameterRepository cvParamRepository,
            LipidMapsEntryRepository lmEntryRepository,
            SwissLipidsEntryRepository slEntryRepository,
            HasAssayLipidQuantityRepository hasAssayLipidQuantityRepository,
            HasCvParentRepository hasCvParentRepository,
            HasLipidMapsReferenceRepository hasLipidMapsReferenceRepository,
            HasFattyAcylRepository hasFattyAcylRepository,
            HasSwissLipidsReferenceRepository hasSwissLipidsReferenceRepository,
            HasCvParameterReferenceRepository hasCvParameterReferenceRepository,
            HasMzTabResultRepository hasMzTabResultRepository,
            HasLipidQuantityLipidReferenceRepository hasLipidQuantityLipidReferenceRepository,
            MzTabAssayRepository mzTabAssayRepository,
            MzTabStudyVariableRepository mzTabStudyVariableRepository,
            MzTabMsRunRepository mzTabMsRunRepository,
            SubmissionRepository submissionRepository,
            StudyRepository studyRepository,
            ArangoOperations arangoOperations,
            @Value("${arangodb.database:lipidcompass}") String arangodbDatabase,
            @Value("#{jobParameters['principal']}") String principal,
            @Value("#{jobParameters['maxRows']}") Long maxRows) {
//        return new MzTabResultWriter1(
//                client, 
//                mzTabDataRepository, 
//                lipidRepository, hasMzTabResultRepository, submissionRepository, studyRepository, arangoOperations, arangodbDatabase, principal, maxRows);
        return new MzTabResultWriter(
                client,
                mzTabDataRepository,
                lipidRepository,
                fattyAcylRepository,
                lipidQuantityRepository,
                cvRepository,
                cvParamRepository,
                lmEntryRepository,
                slEntryRepository,
                hasAssayLipidQuantityRepository,
                hasCvParentRepository,
                hasLipidMapsReferenceRepository,
                hasFattyAcylRepository,
                hasSwissLipidsReferenceRepository,
                hasCvParameterReferenceRepository,
                hasMzTabResultRepository,
                hasLipidQuantityLipidReferenceRepository,
                mzTabAssayRepository,
                mzTabStudyVariableRepository,
                mzTabMsRunRepository,
                submissionRepository,
                studyRepository,
                new GoslinAllGrammarsParser(),
                arangoOperations,
                arangodbDatabase,
                principal,
                maxRows
        );
    }

    @Bean
    public Step mzTabResultStudyImport(JobRepository jobRepository, PlatformTransactionManager transactionManager, MzTabResultWriter writer, MzTabResultMinioItemReader mzTabFileMinioReader) throws IOException {
        return new StepBuilder("mzTabResultStudyImport", jobRepository).
                <MzTab, MzTabResult>chunk(1, transactionManager).
                reader(mzTabFileMinioReader).
                processor(mzTabFileProcessor()).
                faultTolerant().
                writer(writer).
                build();
    }

    @Bean
    public Step mzTabResultImport(JobRepository jobRepository, PlatformTransactionManager transactionManager, MzTabResultWriter writer, MzTabResultItemReader mzTabFileReader) throws IOException {
        return new StepBuilder("mzTabResultImport", jobRepository).
                <MzTab, MzTabResult>chunk(1, transactionManager).
                reader(mzTabFileReader).
                processor(mzTabFileProcessor()).
                faultTolerant().
                writer(writer).
                build();
    }

    @Bean
    public MzTabResultProcessor mzTabFileProcessor() {
        return new MzTabResultProcessor();
    }

    @Bean("mzTabResultImportJob")
    public Job mzTabResultImportJob(JobRepository jobRepository, JobExecutionListener listener, @Qualifier(
            "mzTabResultStudyImport") Step mzTabResultStudyImport) {
        return new JobBuilder("mzTabResultImportJob", jobRepository).
                incrementer(new RunIdIncrementer()).
                preventRestart().
                listener(listener).
                flow(mzTabResultStudyImport).
                //                next(mzTabResultImport).
                end().
                build();
    }
}
