package org.lipidcompass.backend.rest.submission;

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
import com.google.common.io.Files;
import de.isas.mztab2.io.MzTabFileParser;
import de.isas.mztab2.model.ValidationMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.lipidcompass.backend.rest.AbstractArangoController;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.repository.SubmissionRepository;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.SubmissionStatus;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.submission.FileResource;
import org.lipidcompass.data.model.submission.FileResource.FileType;
import org.lipidcompass.data.model.submission.Submission;
import org.lipidcompass.service.core.io.MinioStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.lipidcompass.backend.repository.StudyRepository;
import static org.lipidcompass.data.model.Roles.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.hateoas.CollectionModel;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorType;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@RestController
@ExposesResourceFor(Submission.class)
@RequestMapping(value = "/submission",
        produces = "application/hal+json")
public class SubmissionController extends AbstractArangoController<Submission, SubmissionRepository> {

    private final UserRepository userRepository;
    private final MinioStorageService storageService;
    private final StudyRepository studyRepository;

    @Autowired
    public SubmissionController(MinioStorageService storageService, SubmissionRepository repository, UserRepository userRepository, StudyRepository studyRepository,
            EntityLinks entityLinks) {
        super(repository, entityLinks);
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.studyRepository = studyRepository;
    }

    @Secured({ROLE_USER, ROLE_ADMIN, ROLE_CURATOR})
    @ResponseBody
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<EntityModel<? extends Submission>> getById(
            @PathVariable("id") String id, Authentication authentication) {
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Optional<Submission> opt = repository.findById(id);
            if (opt.isPresent() && loggedInUser.get().getUserName().equals(opt.get().getSubmitter().getUserName())) {
                return ResponseEntity.ok(assembler.toModel(opt.get()));
            } else if (hasAnyGroups(authentication, "ROLE_ADMIN", "ROLE_CURATOR") || opt.get().getVisibility() == Visibility.PUBLIC) {
                if (opt.isPresent()) {
                    return ResponseEntity.ok(assembler.toModel(opt.get()));
                }
            }
            return ResponseEntity.notFound().
                    build();
        }
        return ResponseEntity.noContent().build();
    }

    @PageableAsQueryParam
    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @ResponseBody
    @PostMapping("/query")
    public ResponseEntity<PagedModel<EntityModel<Submission>>> queryUserSubmissions(@ParameterObject Pageable p, Authentication authentication) throws Exception {
        log.debug("Querying user submissions for user {}", authentication.getName());
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Page<Submission> page = this.repository.findBySubmitter(loggedInUser.get().getUserName(), p);
            log.debug("Returning {} submissions for user {}", page.getTotalElements(), authentication.getName());
            return ResponseEntity.ok(assembler.toPagedModel(page, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SubmissionController.class).queryUserSubmissions(p, authentication)).
                    withSelfRel()));
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @PostMapping("/save")
    public ResponseEntity<EntityModel<Submission>> saveSingle(@RequestBody Submission entity, Authentication authentication) {
        log.debug("Saving submission: {}", entity);
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            User user = loggedInUser.get();
            Submission s;
            String transactionUuid = UUID.randomUUID().toString();
            if (entity == null) {
                log.debug("Creating submission from scratch");
                s = Submission.builder().transactionUuid(transactionUuid).storageBucket(UUID.randomUUID()).createdBy(user.getUserName()).privateLinkUuid(UUID.randomUUID()).status(SubmissionStatus.IN_PROGRESS).transactionUuid(transactionUuid).submitter(user).visibility(Visibility.PRIVATE).build();
                Study study = studyRepository.save(Study.builder().description("Please add a description").name("Please add a name").owner(user).transactionUuid(transactionUuid).visibility(Visibility.PRIVATE).build());
                s.setStudy(study);
            } else {
                if (entity.getSubmitter() != null) {
                    if (!entity.getSubmitter().getUserName().equals(loggedInUser.get().getUserName())) {
                        log.debug("Submitter username and authenticated user mismatch!");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                }
                log.debug("Returning existing submission");
                s = entity;
                if (s.getTransactionUuid() == null) {
                    s.setTransactionUuid(transactionUuid);
                }
                if (s.getPrivateLinkUuid() == null) {
                    s.setPrivateLinkUuid(UUID.randomUUID());
                }
                if (s.getSubmitter() == null) {
                    s.setSubmitter(user);
                }
                if (s.getStatus() == null) {
                    s.setStatus(SubmissionStatus.IN_PROGRESS);
                }
                if (s.getVisibility() == null) {
                    s.setVisibility(Visibility.PRIVATE);
                }
                if (s.getStorageBucket() == null) {
                    s.setStorageBucket(UUID.randomUUID());
                }
                if (s.getStudy() == null) {
                    Study study = studyRepository.save(Study.builder().description("Please add a description").name("Please add a name").owner(user).transactionUuid(transactionUuid).visibility(Visibility.PRIVATE).build());
                    s.setStudy(study);
                }
            }
            return ResponseEntity.of(Optional.of(EntityModel.of(this.repository.save(s))));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @PostMapping(path = "/{submissionId:.+}/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EntityModel<Submission>> upload(@PathVariable String submissionId, @RequestParam("file") MultipartFile file, Authentication authentication) {
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Optional<Submission> optSubmission = this.repository.findById(submissionId);
            if (optSubmission.isPresent()) {
                if (!optSubmission.get().getSubmitter().getUserName().equals(loggedInUser.get().getUserName())) {
                    log.debug("Submitter username and authenticated user mismatch!");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                Submission submission = optSubmission.get();
                if (submission.getSubmittedFiles() != null) {
                    if (submission.getSubmittedFiles().stream().anyMatch((t) -> {
                        return file.getName().equals(t.getFileName());
                    })) {
                        return ResponseEntity.of(Optional.of(EntityModel.of(submission)));// file is already present
                    } else {
                        if (storeFile(file, submission)) {
                            return ResponseEntity.of(Optional.of(EntityModel.of(this.repository.save(submission))));
                        }
                    }
                } else {
                    if (storeFile(file, submission)) {
                        return ResponseEntity.of(Optional.of(EntityModel.of(this.repository.save(submission))));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private Optional<String> getFileExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.toLowerCase().substring(fileName.lastIndexOf(".") + 1));
    }

    private boolean storeFile(MultipartFile file, Submission submission) {
        String originalFileName = file.getOriginalFilename();
        Optional<String> fileExtension = getFileExtension(originalFileName);
        FileType fileType = FileType.BINARY;
        if (fileExtension.isPresent()) {
            String contentType = file.getContentType();
            fileType = switch (fileExtension.get()) {
                case "mztab" ->
                    FileType.MZTAB_M;
                case "mzml" ->
                    FileType.MZML;
                case "csv", "tsv" ->
                    FileType.SPREADSHEET;
                case "txt" ->
                    FileType.TEXT;
                case "raw" ->
                    FileType.NATIVE_MS_FORMAT;
                default ->
                    FileType.BINARY;
            };
        }
        log.info("FileType for file {} is {}", file.getOriginalFilename(), fileType);
        Optional<FileResource> fileResource = storageService.store(file, submission.getStorageBucket(), fileType);
        if (fileResource.isPresent()) {
            List<FileResource> submittedFiles;
            if (submission.getSubmittedFiles() == null) {
                submittedFiles = new ArrayList<>();
                submission.setSubmittedFiles(submittedFiles);
            }
            submission.getSubmittedFiles().add(fileResource.get());
            return true;
        }
        return false;
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @DeleteMapping("/{submissionId:.+}/file")
    public ResponseEntity<EntityModel<Submission>> deleteFile(@PathVariable String submissionId, @RequestBody FileResource fileResource, Authentication authentication) {
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Optional<Submission> optSubmission = this.repository.findById(submissionId);
            if (optSubmission.isPresent()) {
                if (!optSubmission.get().getSubmitter().getUserName().equals(loggedInUser.get().getUserName())) {
                    log.debug("Submitter username and authenticated user mismatch!");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                if (optSubmission.get().getStatus() == SubmissionStatus.PUBLISHED || optSubmission.get().getStatus() == SubmissionStatus.IN_CURATION) {
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
                }
                User user = loggedInUser.get();
                Submission submission = optSubmission.get();
                submission.getSubmittedFiles().remove(fileResource);
                storageService.delete(fileResource.getFileName(), submission.getStorageBucket());
                return ResponseEntity.of(Optional.of(EntityModel.of(this.repository.save(submission))));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @GetMapping("/{submissionId:.+}/file")
    public ResponseEntity<Resource> getFile(@PathVariable String submissionId, @RequestBody FileResource fileResource, Authentication authentication) throws FileNotFoundException {
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Optional<Submission> optSubmission = this.repository.findById(submissionId);
            if (optSubmission.isPresent()) {
                if (!optSubmission.get().getSubmitter().getUserName().equals(loggedInUser.get().getUserName())) {
                    log.debug("Submitter username and authenticated user mismatch!");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                MediaType mediaType = MediaType.ALL;
                String ext = Files.getFileExtension(fileResource.getFileName()).toLowerCase();
                switch (ext) {
                    case "zip" ->
                        mediaType = MediaType.valueOf("application/zip");
                    case "txt", "log", "csv", "tsv", "mztab" ->
                        mediaType = MediaType.TEXT_PLAIN;
                    case "svg", "xml" ->
                        mediaType = MediaType.APPLICATION_XML;
                    case "png" ->
                        mediaType = MediaType.IMAGE_PNG;
                    case "gif" ->
                        mediaType = MediaType.IMAGE_GIF;
                    case "jpg", "jpeg" ->
                        mediaType = MediaType.IMAGE_JPEG;
                    case "json" ->
                        mediaType = MediaType.APPLICATION_JSON;
                    default -> {
                        mediaType = MediaType.APPLICATION_OCTET_STREAM;
                        throw new FileNotFoundException(
                                "No handler for file extension '" + ext + "' available for filename '" + fileResource.getFileName() + "'!");
                    }
                }
                Submission submission = optSubmission.get();
                Optional<Resource> file = storageService.loadFile(fileResource.getFileName(), submission.getStorageBucket());
                if (file.isPresent()) {
                    return ResponseEntity.ok().
                            header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=\"" + fileResource.getFileName() + "\"").
                            contentType(mediaType).
                            body(file.get());
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Secured({ROLE_ADMIN, ROLE_CURATOR, ROLE_USER})
    @PostMapping(path = "/{submissionId:.+}/file/validate")
    public ResponseEntity<CollectionModel<ValidationMessage>> validateMzTabFile(@PathVariable String submissionId, @RequestBody FileResource fileResource, Authentication authentication) throws FileNotFoundException {
        log.info("Received file {} for submission {} for validation!", fileResource, submissionId);
        Optional<User> loggedInUser = this.userRepository.findByUserName(authentication.getName());
        if (loggedInUser.isPresent()) {
            Optional<Submission> optSubmission = this.repository.findById(submissionId);
            if (optSubmission.isPresent()) {
                if (!optSubmission.get().getSubmitter().getUserName().equals(loggedInUser.get().getUserName())) {
                    log.debug("Submitter username and authenticated user mismatch!");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                Optional<Resource> file = storageService.loadFile(fileResource.getFileName(), optSubmission.get().getStorageBucket());
                try {
                    MzTabFileParser mzTabFileParser = new MzTabFileParser(file.get().getURI());
                    MZTabErrorList errorList = mzTabFileParser.parse(OutputStream.nullOutputStream(), MZTabErrorType.Level.Warn, 50);
                    List<ValidationMessage> messages = errorList.convertToValidationMessages();
                    List<org.lipidcompass.data.model.submission.ValidationMessage> validationMessages = messages.stream().map(message -> {
                        org.lipidcompass.data.model.submission.ValidationMessage m = new org.lipidcompass.data.model.submission.ValidationMessage();
                        m.setCode(message.getCode());
                        m.setLevel(org.lipidcompass.data.model.submission.ValidationMessage.Level.valueOf(message.getMessageType().name()));
                        Long lineNumber = message.getLineNumber() != null ? message.getLineNumber() : -1;
                        m.setMessage((lineNumber != -1 ? lineNumber + ": " : "") + message.getMessage());
                        m.setSource(org.lipidcompass.data.model.submission.ValidationMessage.Source.MZTAB_M_VALIDATION);
                        return m;
                    }).collect(Collectors.toList());
                    Submission submission = optSubmission.get();
                    Optional<FileResource> foundResource = submission.getSubmittedFiles().stream().filter(fr -> fileResource.getFileName().equals(fr.getFileName())).findFirst();
                    foundResource.ifPresent((t) -> {
                        t.setValidationMessages(validationMessages);
                    });
                    this.repository.save(submission);
                    //invalid input
                    return ResponseEntity.status(HttpStatus.valueOf(200)).body(CollectionModel.of(messages));
                    // TODO implement
                } catch (IOException ex) {
                    log.error("Caught exception while trying to validate mzTabFile: {}", fileResource.getFileName(), ex);
                    Logger.getLogger(SubmissionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
