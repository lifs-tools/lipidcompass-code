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
package org.lipidcompass.batch.data.importer.swisslipids;

import java.util.LinkedHashMap;
import org.lipidcompass.backend.repository.HasSwissLipidsChildRepository;
import org.lipidcompass.backend.repository.SwissLipidsEntryRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class SwissLipidsArangoWriter implements ItemWriter<SwissLipidsEntry> {

    private final HasSwissLipidsChildRepository hasSwissLipidsChildRepository;
    private final SwissLipidsEntryRepository slEntryRepository;
    private final String principal;

    @Autowired
    public SwissLipidsArangoWriter(
            SwissLipidsEntryRepository slEntryRepository,
            HasSwissLipidsChildRepository hasSwissLipidsChildRepository,
            String principal) {
        this.slEntryRepository = slEntryRepository;
        this.hasSwissLipidsChildRepository = hasSwissLipidsChildRepository;
        this.principal = principal;
    }

    private Long jobId;

    @BeforeStep
    public void getInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        this.jobId = jobExecution.getJobId();
    }

    @Transactional
    @Override
    public void write(Chunk<? extends SwissLipidsEntry> list) throws Exception {
        if(list.isEmpty()) {
            return;
        }
        log.info("Writing {} SwissLipids entries to arangodb", list.size());
        Map<String, SwissLipidsEntry> m = new LinkedHashMap<>();
        slEntryRepository.findAllByNativeId(list.getItems().stream().map((t) -> t.getNativeId()).collect(Collectors.toList())).forEach((t) -> {
            m.put(t.getNativeId(), t);
        });
        List<SwissLipidsEntry> l = list.getItems().stream().map(
                (t) -> {
                    if (m.containsKey(t.getNativeId())) {
                        t.setId(m.get(t.getNativeId()).getId());
                        t.setVisibility(Visibility.PUBLIC);
                        return t;
                    } else {
                        return createLipid(t, principal);
                    }
                }
        ).collect(Collectors.toList());
//        log.info("Updating {} entries!", l.size());
        this.slEntryRepository.createOrUpdateAll(l, SwissLipidsEntry.class, "nativeId", (t) -> t.getNativeId());
    }

//    private SwissLipidsEntry write(SwissLipidsEntry l) throws Exception {
//        Optional<SwissLipidsEntry> dbLipid = createAndStoreLipid(l, principal);
//        if (dbLipid.isPresent()) {
//            return dbLipid.get();
//        }
//        throw new RuntimeException("Trying to create/store entity " + l + " failed!");
//    }
    private SwissLipidsEntry createLipid(SwissLipidsEntry l, String principal) throws RuntimeException {
        Optional<SwissLipidsEntry> parentEntry = Optional.empty();
        if (l.getParent() != null) {
            parentEntry = Optional.ofNullable(slEntryRepository.findByNativeId(l.getParent().getNativeId()));
        }
//        Optional<SwissLipidsEntry> al = slEntryRepository.findById(l.getId());

//        if (al.isEmpty()) {
        log.debug("Saving swiss lipids entry with id '{}' since id was not known.", l.getId());
        l.setCreatedBy(principal);
        l.setUpdatedBy(principal);
        l.setTransactionUuid(jobId + "");
        l.setVisibility(Visibility.PUBLIC);
//            al = slEntryRepository.save(l);
//        } else {
//            al.get().setUpdatedBy(principal);
//        }
        if (parentEntry.isPresent()) {
            log.debug("Setting parent entry to {}", parentEntry.get().getId());
            l.setParent(parentEntry.get());
        }
//        try {
//            addParents(al, principal);
//        } catch (Exception e) {
//            log.warn("Caught exception while handling lipid maps for {}", l.getId() + ": " + l.getName());
//        }
//        try {
//            handleCrossReferences(al, l);
//        } catch (Exception e) {
//            log.warn("Caught exception while handling cross references for {}", l.getId() + ": " + l.getName());
//        }
//        if (al.getNativeId() == null || al.getNativeId().isEmpty()) {
//            log.warn("Not storing lipid {} for original lipid {} with null or empty nativeId!", al, l);
//            return Optional.empty();
//        } else {
//            log.debug("Loading updated lipid {}:{}", al.getNativeId(), al.getNativeId());
//            return Optional.of(slEntryRepository.save(al));
//        }
        return l;
    }

//    private void handleSwissLipids(SwissLipidsEntry l) throws RuntimeException {
//        SwissLipidsEntry slEntry = l;
//        if (slEntry != null) {
//            SwissLipidsEntry dbSlEntry = addSlChild(slEntry);
//            if (dbSlEntry == null) {
//                throw new RuntimeException("Persisted SwissLipidsEntry was null!");
//            }
//        }
//    }
//
//    private SwissLipidsEntry addSlChild(final SwissLipidsEntry slEntry) {
//        SwissLipidsEntry activeEntry = slEntryRepository.findByNativeId(slEntry.getNativeId());
//        if (activeEntry == null) {
//            log.info("Saving swiss lipids entry {}", slEntry.getNativeId());
//            activeEntry = slEntryRepository.save(slEntry);
//        }
//        if (slEntry.getChildren() != null && !slEntry.getChildren().isEmpty()) {
//            for (SwissLipidsEntry entry : slEntry.getChildren()) {
//                try {
//                    SwissLipidsEntry childEntry = slEntryRepository.findByNativeId(entry.getNativeId());
//                    if (childEntry == null) {
//                        childEntry = slEntryRepository.save(entry);
//                    }
//                    hasSwissLipidsChildRepository.save(new HasSwissLipidsChild(activeEntry, childEntry));
////                    addSlChild(activeEntry, entry.getChildren());
//                } catch (ArangoDBException ex) {
//                    Logger.getLogger(SwissLipidsArangoWriter.class.getName()).
//                            log(Level.SEVERE, "Failed to add lipidmaps children for parent: {0}", slEntry);
//                }
//            }
//        }
//        if (slEntry.getParent() != null) {
//            SwissLipidsEntry parentEntry = slEntry.getParent();
//            if (parentEntry != null) {
//                SwissLipidsEntry dbParentEntry = slEntryRepository.findByNativeId(parentEntry.getNativeId());
//                if (dbParentEntry == null) {
//                    dbParentEntry = slEntryRepository.save(parentEntry);
//                }
//                if (dbParentEntry.getDescription() == null || dbParentEntry.getDescription() == null) {
//                    dbParentEntry.setDescription(parentEntry.getDescription());
//                }
//                if (dbParentEntry.getAbbreviation() == null || dbParentEntry.getAbbreviation() == null) {
//                    dbParentEntry.setAbbreviation(parentEntry.getAbbreviation());
//                }
//                if (dbParentEntry.getLevel() == null || dbParentEntry.getLevel() == null) {
//                    dbParentEntry.setLevel(parentEntry.getLevel());
//                }
//                dbParentEntry = slEntryRepository.save(dbParentEntry);
//                hasSwissLipidsChildRepository.save(new HasSwissLipidsChild(dbParentEntry, activeEntry));
//                activeEntry.setParent(parentEntry);
//            }
//        }
//        return slEntryRepository.save(activeEntry);
//
//    }
}
