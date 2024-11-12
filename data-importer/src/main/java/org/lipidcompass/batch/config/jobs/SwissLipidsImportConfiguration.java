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

import org.lipidcompass.backend.repository.HasSwissLipidsChildRepository;
import org.lipidcompass.backend.repository.SwissLipidsEntryRepository;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsArangoWriter;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsFieldSetMapper;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsItemReader;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsLipid;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsResultProcessor;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import java.io.File;
import org.lipidcompass.batch.data.importer.swisslipids.SwissLipidsFlatFileItemWriter;
import org.lipidcompass.batch.data.importer.utilities.GzipLazyResource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Import job configuration for SwissLipids.
 *
 * @author nilshoffmann
 */
@Configuration
public class SwissLipidsImportConfiguration {

    @Value("${swissLipids.import.dir}")
    public File swissLipidsImportDir;

    @Value("${lipidMaps.import.chunksize:100}")
    public Integer lipidMapsImportChunkSize;

    @Bean("swissLipidsCategoryProcessor")
    public SwissLipidsResultProcessor swissLipidsCategoryResultProcessor() {
        return new SwissLipidsResultProcessor("Category");
    }

    @Bean("swissLipidsClassProcessor")
    public SwissLipidsResultProcessor swissLipidsClassResultProcessor() {
        return new SwissLipidsResultProcessor("Class");
    }

    @Bean("swissLipidsSpeciesProcessor")
    public SwissLipidsResultProcessor swissLipidsSpeciesResultProcessor() {
        return new SwissLipidsResultProcessor("Species");
    }

    @Bean("swissLipidsMolecularSubspeciesProcessor")
    public SwissLipidsResultProcessor swissLipidsMolecularSubspeciesResultProcessor() {
        return new SwissLipidsResultProcessor("Molecular subspecies");
    }

    @Bean("swissLipidsStructuralSubspeciesProcessor")
    public SwissLipidsResultProcessor swissLipidsStructuralSubspeciesResultProcessor() {
        return new SwissLipidsResultProcessor("Structural subspecies");
    }

    @Bean("swissLipidsIsomericSubspeciesProcessor")
    public SwissLipidsResultProcessor swissLipidsIsomericSubspeciesResultProcessor() {
        return new SwissLipidsResultProcessor("Isomeric subspecies");
    }

    @StepScope
    @Bean("swissLipidsArangoWriter")
    public SwissLipidsArangoWriter swissLipidsArangoWriter(
            SwissLipidsEntryRepository slEntryRepository,
            HasSwissLipidsChildRepository hasSwissLipidsChildRepository,
            @Value("#{jobParameters['principal']}") String principal
    ) {
        return new SwissLipidsArangoWriter(
                slEntryRepository, hasSwissLipidsChildRepository, principal);
    }

    @Bean("swissLipidsCategoryImport")
    public Step swissLipidsLipidCategoryImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsCategoryImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsCategoryResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }

    @Bean("swissLipidsClassImport")
    public Step swissLipidsLipidClassImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsClassImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsClassResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }

    @Bean("swissLipidsSpeciesImport")
    public Step swissLipidsLipidSpeciesImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsSpeciesImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsSpeciesResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }

    @Bean("swissLipidsMolecularSubspeciesImport")
    public Step swissLipidsLipidMolecularSubspeciesImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsMolecularSubspeciesImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsMolecularSubspeciesResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }

    @Bean("swissLipidsStructuralSubspeciesImport")
    public Step swissLipidsLipidStructuralSubspeciesImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsStructuralSubspeciesImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsStructuralSubspeciesResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }

    @Bean("swissLipidsIsomericSubspeciesImport")
    public Step swissLipidsLipidIsomericSubspeciesImport(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager, 
            @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
            @Qualifier("swissLipidsArangoWriter") ItemWriter<SwissLipidsEntry> swissLipidsArangoWriter
    ) {
        return new StepBuilder("swissLipidsIsomericSubspeciesImport", jobRepository).
                <SwissLipidsLipid, SwissLipidsEntry>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                processor(swissLipidsIsomericSubspeciesResultProcessor()).
                writer(swissLipidsArangoWriter).
                build();
    }
    
    @Bean("swissLipidsFlatFileOutput")
    public Step swissLipidsFlatFileOutput(
         JobRepository jobRepository,
         PlatformTransactionManager transactionManager, 
         @Qualifier("swissLipidsLipidItemReader") ItemReader<SwissLipidsLipid> itemReader,
         @Qualifier("swissLipidsFlatFileItemWriter") ItemWriter<SwissLipidsLipid> itemWriter
    ) {
        return new StepBuilder("swissLipidsFlatFileOutput", jobRepository).
                <SwissLipidsLipid, SwissLipidsLipid>chunk(lipidMapsImportChunkSize, transactionManager).
                reader(itemReader).
                writer(itemWriter).
                build();
        
    }

    @Bean
    public Job swissLipidsImportJob(JobRepository jobRepository,
                                    @Autowired JobExecutionListener listener, @Qualifier(
                                    "swissLipidsCategoryImport") Step swissLipidsCategoryImport,
                                    @Qualifier("swissLipidsClassImport") Step swissLipidsClassImport,
                                    @Qualifier("swissLipidsSpeciesImport") Step swissLipidsSpeciesImport,
                                    @Qualifier("swissLipidsMolecularSubspeciesImport") Step swissLipidsMolecularSubspeciesImport,
                                    @Qualifier("swissLipidsStructuralSubspeciesImport") Step swissLipidsStructuralSubspeciesImport,
                                    @Qualifier("swissLipidsIsomericSubspeciesImport") Step swissLipidsIsomericSubspeciesImport
//                                    @Qualifier("swissLipidsFlatFileOutput") Step swissLipidsFlatFileOutput
    ) {
        return new JobBuilder("swissLipidsImportJob", jobRepository).
                incrementer(new RunIdIncrementer()).
                preventRestart().
                listener(listener).
//                flow(swissLipidsFlatFileOutput).
                flow(swissLipidsCategoryImport).
                next(swissLipidsClassImport).
                next(swissLipidsSpeciesImport).
                next(swissLipidsMolecularSubspeciesImport).
                next(swissLipidsStructuralSubspeciesImport).
                next(swissLipidsIsomericSubspeciesImport).
                end().
                build();
    }
    
    @Bean
    public Job swissLipidsFlatFileOutputJob(
            JobRepository jobRepository,
            @Autowired JobExecutionListener listener, 
            @Qualifier("swissLipidsFlatFileOutput") Step swissLipidsFlatFileOutput
    ) {
        return new JobBuilder("swissLipidsFlatFileOutputJob", jobRepository).
                incrementer(new RunIdIncrementer()).
                preventRestart().
                listener(listener).
                flow(swissLipidsFlatFileOutput).
                end().
                build();
    }

    @Bean("swissLipidsLipidItemReader")
    @StepScope
    public SwissLipidsItemReader swissLipidsLipidItemReader(@Value("#{jobParameters['fileName']}") String fileName) {
        SwissLipidsItemReader csvFileReader = new SwissLipidsItemReader();
        if(fileName.endsWith(".gz")) {
            csvFileReader.setResource(new GzipLazyResource(new File(swissLipidsImportDir, fileName)));
        } else {
            csvFileReader.setResource(new FileSystemResource(new File(swissLipidsImportDir, fileName)));
        }
        csvFileReader.setLinesToSkip(1);

        LineMapper<SwissLipidsLipid> swissLipidsLineMapper = createSwissLipidsLineMapper();
        csvFileReader.setLineMapper(swissLipidsLineMapper);
        return csvFileReader;
    }
    
    @Bean("swissLipidsFlatFileItemWriter")
    @StepScope
    public SwissLipidsFlatFileItemWriter swissLipidsFlatFileItemWriter(@Value("#{jobParameters['fileName']}") String fileName) {
        SwissLipidsFlatFileItemWriter csvFileWriter = new SwissLipidsFlatFileItemWriter();
        csvFileWriter.setOutputDirectory(new File(swissLipidsImportDir, "/export/"));
        return csvFileWriter;
    }

    private LineMapper<SwissLipidsLipid> createSwissLipidsLineMapper() {
        DefaultLineMapper<SwissLipidsLipid> swissLipidsLineMapper = new DefaultLineMapper<>();

        LineTokenizer swissLipidsLineTokenizer = createSwissLipidsLineTokenizer();
        swissLipidsLineMapper.setLineTokenizer(swissLipidsLineTokenizer);

        FieldSetMapper<SwissLipidsLipid> swissLipidsMapper = createSwissLipidsMapper();
        swissLipidsLineMapper.setFieldSetMapper(swissLipidsMapper);

        return swissLipidsLineMapper;
    }

    private LineTokenizer createSwissLipidsLineTokenizer() {
        DelimitedLineTokenizer swissLipidsLine = new DelimitedLineTokenizer();
        swissLipidsLine.setDelimiter("\t");
        swissLipidsLine.setQuoteCharacter('"');
        swissLipidsLine.setNames(SwissLipidsLipid.CSV_ALL_FIELDS);
        swissLipidsLine.setStrict(false);
        return swissLipidsLine;
    }

    private FieldSetMapper<SwissLipidsLipid> createSwissLipidsMapper() {
        return new SwissLipidsFieldSetMapper();
    }
}
