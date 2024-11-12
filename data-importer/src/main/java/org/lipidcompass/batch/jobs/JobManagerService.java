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
package org.lipidcompass.batch.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.batch.data.importer.utilities.AuthenticationPrincipalNameExtractor;
import org.lipidcompass.batch.rest.dto.JobDto;
import org.lipidcompass.batch.rest.dto.JobFileInputDto;
import org.lipidcompass.batch.rest.dto.JobExecutionDto;
import org.lipidcompass.batch.rest.dto.JobSubmissionInputDto;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Service
@lombok.Data
public class JobManagerService {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
//    @Qualifier("lipidMapsImportJob")
    Job lipidMapsImportJob;

    @Autowired
//    @Qualifier("swissLipidsImportJob")
    Job swissLipidsImportJob;
    
    @Autowired
//    @Qualifier("swissLipidsFlatFileOutputJob")
    Job swissLipidsFlatFileOutputJob;

    @Autowired
//    @Qualifier("studyImportJob")
    Job studyImportJob;

    @Autowired
//    @Qualifier("mzTabResultImportJob")
    Job mzTabResultImportJob;

    @Value("${lipidMaps.import.dir}")
    public File lipidMapsImportDir;

    @Value("${swissLipids.import.dir}")
    public File swissLipidsImportDir;

    @Value("${mzTab.import.dir}")
    public File mzTabImportDir;

    @Autowired
    AuthenticationPrincipalNameExtractor authPrincipalNameExtractor;

    private Set<String> listFilesRecursively(File baseDir, String suffix) {
        String lowerCaseSuffix = suffix.toLowerCase();
        Path baseDirPath = baseDir.toPath().normalize();
        log.info("Visiting files below {}, filtering by suffix: {}", baseDir, suffix);
        try (Stream<Path> stream = Files.walk(baseDirPath, 10, FileVisitOption.FOLLOW_LINKS)) {
//            Set<Path> directories = stream.filter(p -> Files.isDirectory(p)).collect(Collectors.toSet());
//            log.info("Added the following directories: {}", directories);
            Set<String> fileNames = stream
                    .map(path -> baseDirPath.relativize(path))
                    .map(Path::toString)
                    .collect(Collectors.toSet()).stream()
                    .filter(s -> s.toLowerCase().endsWith(lowerCaseSuffix))
                    .collect(Collectors.toSet());
            return fileNames;
        } catch (IOException ioex) {
            log.error("Caught IOException while trying to list files below " + baseDir, ioex);
            return Collections.emptySet();
        }
    }

    @Secured("ROLE_ADMIN")
    public Set<JobExecutionDto> findRunningJobExecutions(String jobName, Authentication authentication) {
        log.info("Authenticated principal is {}", authPrincipalNameExtractor.getName(authentication));
        return jobExplorer.findRunningJobExecutions(jobName).stream().map((t) -> {
            JobExecutionDto jed = new JobExecutionDto();
            jed.setExecutionId(t.getId());
            jed.setJobId(t.getJobId());
            jed.setJobName(t.getJobInstance().getJobName());
            jed.setStatus(t.getStatus());
            jed.setJobParameters(t.getJobParameters().getParameters());
            jed.setCreateTime(t.getCreateTime());
            jed.setEndTime(t.getEndTime());
            jed.setStartTime(t.getStartTime());
            jed.setLastUpdateTime(t.getLastUpdated());
            jed.setExitStatus(t.getExitStatus());
            return jed;
        }).collect(Collectors.toSet());
    }

    @Secured("ROLE_ADMIN")
    public JobExecutionDto startJob(String name, JobFileInputDto jobDto, Authentication authentication) throws Exception {
        File f = new File(jobDto.getFile());
        if (!f.isAbsolute()) {
            if (f.getPath().contains("..")) {
                throw new IllegalArgumentException("file argument must be a relative path without '..'!");
            }
            log.info("Authenticated principal is {}", authPrincipalNameExtractor.getName(authentication));
            if (null != name) {
                log.info("Received jobDto: {}", jobDto);
                JobParameters jobParameters
                        = new JobParametersBuilder().
                                addLong("time", System.currentTimeMillis()).
                                addString("fileName", jobDto.getFile()).
                                addString("principal", authPrincipalNameExtractor.getName(authentication)).
                                addLong("maxRows", jobDto.getMaxRows()).
                                toJobParameters();
                switch (name) {
                    case "lipidMapsImportJob": {
                        return JobExecutionDto.of(jobLauncher.run(lipidMapsImportJob, jobParameters));
                    }
                    case "swissLipidsImportJob": {
                        return JobExecutionDto.of(jobLauncher.run(swissLipidsImportJob, jobParameters));
                    }
                    case "swissLipidsFlatFileOutputJob": {
                        return JobExecutionDto.of(jobLauncher.run(swissLipidsFlatFileOutputJob, jobParameters));
                    }
//                    case "mzTabResultImportJob": {
//                        return JobExecutionDto.of(jobLauncher.run(mzTabResultImportJob, jobParameters));
//                    }
                    case "studyImportJob": {
                        return JobExecutionDto.of(jobLauncher.run(studyImportJob, jobParameters));
                    }
                    default:
                        break;
                }
            }
        } else {
            log.warn("File argument is absolute: {}", f.getPath());
        }
        throw new NullPointerException("No such job: " + name + " for file " + jobDto.getFile());
    }

    @Secured("ROLE_ADMIN")
    public JobExecutionDto startSubmissionJob(JobSubmissionInputDto jobDto, Authentication authentication) throws Exception {
        log.info("Authenticated principal is {}", authPrincipalNameExtractor.getName(authentication));
        log.info("Received jobSubmissionDto: {}", jobDto);
        JobParameters jobParameters
                = new JobParametersBuilder().
                        addLong("time", System.currentTimeMillis()).
                        addString("submissionId", jobDto.getSubmissionId()).
                        addString("principal", authPrincipalNameExtractor.getName(authentication)).
                        addLong("maxRows", jobDto.getMaxRows()).
                        toJobParameters();
        return JobExecutionDto.of(jobLauncher.run(mzTabResultImportJob, jobParameters));
    }

//    @Secured("ROLE_USER")
//    public JobExecutionDto startDataImportJob(String name, JobFileInputDto jobDto, Authentication authentication) throws Exception {
//        Optional<User> user = authPrincipalNameExtractor.getOrCreateUser(authentication);
//        if (user.isEmpty()) {
//            throw new IllegalStateException("Could not determine current user!");
//        }
//        File f = new File(jobDto.getFile());
//        if (!f.isAbsolute()) {
//            if (f.getPath().contains("..")) {
//                throw new IllegalArgumentException("file argument must be a relative path without '..'!");
//            }
//            log.info("Authenticated principal is {}", authPrincipalNameExtractor.getName(authentication));
//            if (null != name) {
//                log.info("Received jobDto: {}", jobDto);
//                JobParameters jobParameters
//                        = new JobParametersBuilder().
//                                addLong("time", System.currentTimeMillis()).
//                                addString("fileName", jobDto.getFile()).
//                                addString("principal", authPrincipalNameExtractor.getName(authentication)).
//                                addString("userId", user.get().getId()).
//                                addLong("maxRows", jobDto.getMaxRows()).
//                                toJobParameters();
//                switch (name) {
//                    case "studyImportJob": {
//                        return JobExecutionDto.of(jobLauncher.run(studyImportJob, jobParameters));
//                    }
//                    default:
//                        break;
//                }
//            }
//        } else {
//            log.warn("File argument is absolute: {}", f.getPath());
//        }
//        throw new NullPointerException("No such job: " + name + " for file " + jobDto.getFile());
//    }
    @Secured("ROLE_ADMIN")
    public Collection<String> listFiles(JobExecutionDto jobExecutionDto, Authentication authentication) throws Exception {
        log.info("Authenticated principal is {}", authPrincipalNameExtractor.getName(authentication));
        if (jobExecutionDto != null) {
            switch (jobExecutionDto.getJobName()) {
                case "lipidMapsImportJob": {
                    return listFilesRecursively(lipidMapsImportDir, ".sdf");
                }
                case "swissLipidsImportJob": {
                    return listFilesRecursively(swissLipidsImportDir, ".tsv.gz");
                }
                case "mzTabResultImportJob": {
                    return listFilesRecursively(mzTabImportDir, ".mzTab");
                }
                default:
                    break;
            }
            throw new NullPointerException("No such job: " + jobExecutionDto.getJobName());
        }
        throw new NullPointerException("Argument jobExecutionDto was null!");
    }

    @Secured("ROLE_ADMIN")
    public BatchStatus getJobStatus(Long jobId, Authentication authentication) {
        return Optional.ofNullable(jobExplorer.getJobExecution(jobId)).map((t) -> {
            if (t != null) {
                return t.getStatus();
            } else {
                return BatchStatus.UNKNOWN;
            }
        }).orElse(BatchStatus.UNKNOWN);
    }

    @Secured("ROLE_ADMIN")
    public Page<JobDto> getJobDtos(Pageable p) {
        List<String> jobNames = jobExplorer.getJobNames();
        List<JobDto> jobs = jobNames.stream().map(
                name -> JobDto.of(
                        name,
                        jobExplorer.
                                getJobInstances(name, 0, -1),
                        jobExplorer
                )
        ).collect(Collectors.toList());
        return asPage(jobs, p);
    }

    private static <T> Page<T> asPage(List<T> list, Pageable pageable) {
        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if (startOfPage > list.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        int endOfPage = Math.min(startOfPage + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }

}
