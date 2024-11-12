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

import org.lipidcompass.batch.rest.dto.JobFileInputDto;
import org.lipidcompass.batch.rest.dto.SmilesDto;
import org.lipidcompass.batch.rest.dto.JobExecutionDto;
import org.lipidcompass.batch.rest.dto.JobDto;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.batch.jobs.JobManagerService;
import org.lipidcompass.batch.jobs.SmilesService;
import org.lipidcompass.batch.rest.dto.JobSubmissionInputDto;
import static org.lipidcompass.data.model.Roles.ROLE_ADMIN;
import static org.lipidcompass.data.model.Roles.ROLE_CURATOR;

import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@RestController
@RequestMapping(value = "/jobs",
        produces = "application/hal+json")
public class JobController {

//    @Autowired
//    private DtoRepresentationModelAssembler<JobExecutionDto> jobExecutionDtoRepresentationModelAssembler;
    @Autowired
    private JobManagerService jobManagerService;

    @Autowired
    private SmilesService smilesService;

    @Autowired
    public JobController(JobManagerService jobManagerService) {
        this.jobManagerService = jobManagerService;
    }

    @PageableAsQueryParam
    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping(path = {"", "/"})
    public ResponseEntity<PagedModel<EntityModel<JobDto>>> getJobs(@ParameterObject Pageable p, Authentication authentication) throws Exception {
        return ResponseEntity.of(Optional.ofNullable(toPagedModel(jobManagerService.getJobDtos(p), JobDto.class)));
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping(path = "/executions/{jobName}")
    public ResponseEntity<CollectionModel<EntityModel<JobExecutionDto>>> findRunningJobExecutions(@PathVariable String jobName, Authentication authentication) {
        return ResponseEntity.of(Optional.ofNullable(StreamSupport.stream(jobManagerService.findRunningJobExecutions(jobName, authentication).spliterator(), false).map((t) -> toModel(t, JobExecutionDto.class)).collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of))));
    }
//
//    @Secured(ROLE_ADMIN)
//    @ResponseBody
//    @PostMapping(path = "/indexing/{name}")
//    public ResponseEntity<EntityModel<JobExecutionDto>> startIndexingJob(@PathVariable String name, Authentication authentication) throws Exception {
//        return ResponseEntity.of(Optional.ofNullable(EntityModel.of(jobManagerService.startIndexingJob(name, authentication))));
//    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @PostMapping(path = "/smiles/canonicalize")
    public ResponseEntity<EntityModel<SmilesDto>> canonicalizeSmiles(@RequestBody @Valid SmilesDto inputSmiles, Authentication authentication) throws Exception {
        return ResponseEntity.of(Optional.ofNullable(EntityModel.of(smilesService.canonicalizeSmiles(inputSmiles, authentication))));
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @PostMapping(path = "/start/{name}")
    public ResponseEntity<EntityModel<JobExecutionDto>> startJob(@PathVariable String name, @RequestBody @Valid JobFileInputDto jobDto, Authentication authentication) throws Exception {
        return ResponseEntity.of(Optional.ofNullable(EntityModel.of(jobManagerService.startJob(name, jobDto, authentication))));
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @PostMapping(path = "/submission")
    public ResponseEntity<EntityModel<JobExecutionDto>> startSubmissionImportJob(@RequestBody @Valid JobSubmissionInputDto jobDto, Authentication authentication) throws Exception {
        return ResponseEntity.of(Optional.ofNullable(EntityModel.of(jobManagerService.startSubmissionJob(jobDto, authentication))));
    }

//    @Secured("ROLE_USER")
//    @ResponseBody
//    @PostMapping(path = "/{name}")
//    public JobExecutionDto startDataImportJob(@PathVariable String name, @RequestBody @Valid JobFileInputDto jobDto, Authentication authentication) throws Exception {
//        return jobManagerService.startDataImportJob(name, jobDto, authentication);
//    }
    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping(path = "/files/{jobExecutionId}")
    public ResponseEntity<CollectionModel<EntityModel<String>>> listFiles(@PathVariable Long jobExecutionId, Authentication authentication) throws Exception {
        return ResponseEntity.of(Optional.ofNullable(StreamSupport.stream(jobManagerService.listFiles(JobExecutionDto.of(jobManagerService.getJobExplorer().getJobExecution(jobExecutionId)), authentication).spliterator(), false).map((t) -> toModel(t, String.class)).collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of))));
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping(path = "/{jobId}")
    public ResponseEntity<EntityModel<BatchStatus>> getJobStatus(@PathVariable Long jobId, Authentication authentication) {
        return ResponseEntity.of(Optional.ofNullable(EntityModel.of(jobManagerService.getJobStatus(jobId, authentication))));
    }

    private static <T> EntityModel<T> toModel(T entity, Class<T> clazz) {
        return EntityModel.of(clazz.cast(entity));
    }

    private static <T> CollectionModel<EntityModel<T>> toCollectionModel(Iterable<? extends T> entities, Class<T> clazz) {
        return StreamSupport.stream(entities.spliterator(), false).map((t) -> toModel(t, clazz)).collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }

    private static <T> PagedModel<EntityModel<T>> toPagedModel(Page<? extends T> page, Class<T> clazz, Link... links) {
        CollectionModel<EntityModel<T>> cm = toCollectionModel(page, clazz);
        cm.add(links);
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        return PagedModel.of(cm.getContent(), meta);
    }

}
