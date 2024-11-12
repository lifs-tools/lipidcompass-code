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
import org.lipidcompass.backend.repository.HasLipidMapsChildRepository;
import org.lipidcompass.backend.repository.LipidMapsEntryRepository;
import org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsIAtomContainerToLipidProcessor;
import org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsSdfItemReader;
import org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsArangoWriter;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import java.io.File;
import java.util.Arrays;
import org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsFlatFileItemWriter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Import job configuration for Lipid Maps.
 *
 * @author nilshoffmann
 */
@Configuration
public class LipidMapsImportConfiguration {

    @Value("${lipidMaps.import.dir}")
    public File lipidMapsImportDir;

    @Value("${lipidMaps.import.chunksize:100}")
    public Integer lipidMapsImportChunkSize;

    @Bean("lipidMapsSdfItemReader")
    @StepScope
    public ItemReader<IAtomContainer> lipidMapsSdfItemReader(@Value("#{jobParameters['fileName']}") String fileName) {
        return new LipidMapsSdfItemReader(
                lipidMapsImportDir, fileName);
    }

    @Bean("lipidMapsFlatFileItemWriter")
    @StepScope
    public LipidMapsFlatFileItemWriter lipidMapsFlatFileItemWriter(@Value("#{jobParameters['fileName']}") String fileName) {
        LipidMapsFlatFileItemWriter csvFileWriter = new LipidMapsFlatFileItemWriter();
        csvFileWriter.setOutputDirectory(new File(lipidMapsImportDir, "/export/"));
        return csvFileWriter;
    }

    @Bean("lipidMapsIAtomContainerToLipidProcessor")
    @StepScope
    public LipidMapsIAtomContainerToLipidProcessor lipidMapsIAtomContainerToLipidProcessor() {
        return new LipidMapsIAtomContainerToLipidProcessor();
    }

    @Bean("lipidMapsArangoItemWriter")
    @StepScope
    public LipidMapsArangoWriter lipidMapsArangoWriter(
            HasLipidMapsChildRepository lmChildRepository,
            LipidMapsEntryRepository lmEntryRepository,
            ArangoOperations arangoOperations,
            @Value("#{jobParameters['principal']}") String principal) {
        return new LipidMapsArangoWriter(
                lmChildRepository,
                lmEntryRepository,
                arangoOperations,
                principal
        );
    }

    @Bean("lipidMapsLipidImport")
    public Step lipidMapsLipidImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("lipidMapsSdfItemReader") ItemReader<IAtomContainer> itemReader,
            @Qualifier("lipidMapsIAtomContainerToLipidProcessor") ItemProcessor<IAtomContainer, LipidMapsEntry> lipidMapsIAtomContainerToLipidProcessor,
            @Qualifier("lipidMapsArangoItemWriter") ItemWriter<LipidMapsEntry> arangoWriter,
            @Qualifier("lipidMapsFlatFileItemWriter") ItemWriter<LipidMapsEntry> lmfwriter) {
        CompositeItemWriter<LipidMapsEntry> compositeWriter = new CompositeItemWriter<>();
        compositeWriter.setDelegates(Arrays.asList(arangoWriter, lmfwriter));
        return new StepBuilder("lipidMapsLipidImport", jobRepository).
                <IAtomContainer, LipidMapsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(lipidMapsIAtomContainerToLipidProcessor).
                writer(compositeWriter).
                build();
    }

    @Bean("lipidMapsImportJob")
    public Job lipidMapsImportJob(JobRepository jobRepository, JobExecutionListener listener, @Qualifier(
            "lipidMapsLipidImport") Step lipidMapsLipidImport) {
        return new JobBuilder("lipidMapsImportJob", jobRepository).
                incrementer(new RunIdIncrementer()).
                preventRestart().
                listener(listener).
                flow(lipidMapsLipidImport).
                end().
                build();
    }
}
