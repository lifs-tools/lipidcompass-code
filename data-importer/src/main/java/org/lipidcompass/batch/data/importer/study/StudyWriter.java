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
package org.lipidcompass.batch.data.importer.study;

import com.arangodb.springframework.core.ArangoOperations;
import org.lipidcompass.backend.repository.MzTabResultRepository;
import org.lipidcompass.data.model.User;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.backend.repository.HasMzTabResultRepository;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.config.LipidCompassProperties;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.relations.HasMzTabResult;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.lipidcompass.backend.repository.StudyRepository;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.Visibility;
import org.springframework.batch.item.Chunk;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class StudyWriter implements ItemWriter<StudyDto> {

    private final StudyRepository studyRepository;
    private final MzTabResultRepository mzTabResultRepository;
    private final HasMzTabResultRepository hasMzTabResultRepository;
    private final UserRepository userRepository;
    private final ArangoOperations arangoOperations;
    private final LipidCompassProperties lipidCompassProperties;
    private final String principal;

    @Autowired
    public StudyWriter(
            StudyRepository experimentRepository,
            MzTabResultRepository mzTabResultRepository,
            HasMzTabResultRepository hasMzTabResultRepository,
            UserRepository userRepository,
            ArangoOperations arangoOperations,
            String principal,
            LipidCompassProperties lipidCompassProperties) {
        this.studyRepository = experimentRepository;
        this.mzTabResultRepository = mzTabResultRepository;
        this.hasMzTabResultRepository = hasMzTabResultRepository;
        this.userRepository = userRepository;
        this.arangoOperations = arangoOperations;
        this.principal = principal;
        this.lipidCompassProperties = lipidCompassProperties;
    }

    private Long jobId;

    @BeforeStep
    public void getInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        this.jobId = jobExecution.getJobId();
    }

    @Transactional
    @Override
    public void write(
            Chunk<? extends StudyDto> items) throws Exception {
        Logger.getLogger(StudyWriter.class.getName()).
                log(Level.INFO, "Writing {0} studies to database!", items.
                        size());
        System.out.println("Received " + items.size() + " items for writing!");
        arangoOperations.collection(User.class).count();
        arangoOperations.collection(Study.class).count();
        arangoOperations.collection(MzTabResult.class).count();
        arangoOperations.collection(HasMzTabResult.class).count();
        for (StudyDto l : items) {
            Optional<User> potentialOwner = userRepository.findByUserName(principal);
            User user = null;
            if (potentialOwner.isPresent()) {
                user = potentialOwner.get();
            } else {
                Optional<User> adminOwner = userRepository.findByUserName(lipidCompassProperties.getSystemAdminName());
                if (adminOwner.isPresent()) {
                    user = adminOwner.get();
                } else {
                    throw new RuntimeException("No viable user could be found in database!");
                }
            }
            Optional<Study> study = studyRepository.findByNativeId(l.getNativeId());
            if (study.isEmpty()) {
                Study s = Study.builder().
                        //                    createdBy(principal).
                        //                    updatedBy(principal).
                        //                    id(l.getName()).
                        nativeId(l.getNativeId()).
                        transactionUuid(jobId + "").
                        name(l.getName()).
                        visibility(Visibility.PUBLIC).
                        description(l.getDescription()).
                        owner(user).
                        build();
                study = Optional.of(studyRepository.save(s));
            }
            study.ifPresent((s) -> {
                for (String mzTabResultId : l.getMzTabResultIds()) {
                    mzTabResultRepository.findById(mzTabResultId).map((mzTabResult) -> {
                        return HasMzTabResult.builder().
                                //                            createdBy(principal).
                                //                            updatedBy(principal).
                                transactionUuid(jobId + "").
                                visibility(Visibility.PUBLIC).
                                from(s).
                                to(mzTabResult).
                                build();
                    }).map((hasMzTabResult) -> {
                        return hasMzTabResultRepository.save(hasMzTabResult);
                    });
                }
            });
        }
    }
}
