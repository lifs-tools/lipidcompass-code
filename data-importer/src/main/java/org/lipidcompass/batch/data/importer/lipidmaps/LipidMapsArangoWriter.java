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
package org.lipidcompass.batch.data.importer.lipidmaps;

import com.arangodb.springframework.core.ArangoOperations;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.lipidcompass.backend.repository.HasLipidMapsChildRepository;
import org.lipidcompass.backend.repository.LipidMapsEntryRepository;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class LipidMapsArangoWriter implements ItemWriter<LipidMapsEntry> {

    private final HasLipidMapsChildRepository hasLipidMapsChildRepository;
    private final LipidMapsEntryRepository lmEntryRepository;
//    private final SolrOperations solrTemplate;
//    private final String lipidMapsCore;
    private final String principal;

    @Autowired
    public LipidMapsArangoWriter(
            HasLipidMapsChildRepository hasLipidMapsChildRepository,
            LipidMapsEntryRepository lmEntryRepository,
            ArangoOperations arangoOperations,
            String principal) {
        this.hasLipidMapsChildRepository = hasLipidMapsChildRepository;
        this.lmEntryRepository = lmEntryRepository;
        this.principal = principal;
    }

    private Long jobId;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Transactional
    @Override
    public void write(Chunk<? extends LipidMapsEntry> list) throws Exception {
        log.info("Writing LipidMaps {} entries to arangodb", list.size());
        Map<String, LipidMapsEntry> m = new LinkedHashMap<>();
        lmEntryRepository.findAllByNativeId(list.getItems().stream().map((t) -> t.getNativeId()).collect(Collectors.toList())).forEach((t) -> {
            m.put(t.getNativeId(), t);
        });
        List<LipidMapsEntry> l = list.getItems().stream().map(
                (t) -> {
                    if (m.containsKey(t.getNativeId())) {
                        t.setId(m.get(t.getNativeId()).getId());
                        return t;
                    } else {
                        return t;
                    }
                }
        ).collect(Collectors.toList());
        this.lmEntryRepository.createOrUpdateAll(l, LipidMapsEntry.class, "nativeId", (t) -> t.getNativeId());
    }
    
    
//    @Transactional
//    @Override
//    public LipidMapsEntry process(LipidMapsEntry l) throws Exception {
//        this.jobId = this.stepExecution.getJobExecutionId();
//        Optional<LipidMapsEntry> dbLipid = createAndStoreLipid(l, principal);
//        if (dbLipid.isPresent()) {
//            return dbLipid.get();
//        }
//        throw new RuntimeException("Trying to create/store entity " + l + " failed!");
//    }

//    @Transactional
//    @Override
//    public void write(
//            List<? extends LipidMapsEntry> items) throws Exception {
//        if (!items.isEmpty()) {
//            log.info("Writing {} Lipid Maps entries to database!", items.size());
//        } else {
//            return;
//        }
//
//        items.forEach(l -> {
//            try {
//                Optional<LipidMapsEntry> dbLipid = createAndStoreLipid(l, principal);
//            } catch (RuntimeException e) {
//                log.error("Caught exception while trying to persist " + l, e);
//            }
//        });
//    }
//    private Optional<LipidMapsEntry> createAndStoreLipid(LipidMapsEntry l, String principal) throws RuntimeException {
//        log.info("Transaction uuid is {}", jobId);
////        LipidMapsEntry al = lmEntryRepository.findByNativeId(l.getNativeId());
////        if (al == null) {
////            log.debug("Saving lipid maps entry with nativeId '{}' since nativeId was not known.", l.getNativeId());
//        l.setTransactionUuid(jobId + "");
//        return Optional.of(lmEntryRepository.save(l));
////        } else {
////
////            al.setUpdatedBy(principal);
////        }
////        try {
////            addParents(al, principal);
////        } catch (Exception e) {
////            log.warn("Caught exception while handling lipid maps for {}", l.getId() + ": " + l.getName());
////        }
////        try {
////            handleCrossReferences(al, l);
////        } catch (Exception e) {
////            log.warn("Caught exception while handling cross references for {}", l.getId() + ": " + l.getName());
////        }
////        if (al.getNativeId() == null || al.getNativeId().isEmpty()) {
////            log.warn("Not storing lipid {} for original lipid {} with null or empty nativeId!", al, l);
////            return Optional.empty();
////        } else {
////            log.debug("Loading updated lipid {}:{}", al.getNativeId(), al.getName());
////            return Optional.of(lmEntryRepository.save(al));
////        }
//    }

//    private void handleCrossReferences(final LipidMapsEntry dbLipid, LipidMapsEntry l) {
//        //            al.setLipidMapsEntry(lmEntry);
//        dbLipid.setCrossReferences(l.getCrossReferences().
//                stream().
//                map((crossRef)
//                        -> {
//                    CrossReference cr = crossRefRepository.findByNativeId(crossRef.getNativeId());
//                    if (cr == null) {
//                        cr = crossRefRepository.save(crossRef);
//                    }
//                    hasCrossRefRepository.save(new HasCrossReference(
//                            dbLipid, cr));
//                    return cr;
//                }).collect(Collectors.toList()));
//    }
//
//    private void handleLipidMaps(LipidMapsEntry l, final LipidMapsEntry dbLipid) throws RuntimeException {
//        LipidMapsEntry lmCategory = l.getLevel()LipidMapsCategory();
//        if (lmCategory != null) {
//            LipidMapsEntry dbLmEntry = addLmChild(lmCategory, lmCategory.getChildren());
//            if (dbLmEntry == null) {
//                throw new RuntimeException("Persisted LipidMapsEntry was null!");
//            }
//            dbLipid.setLipidMapsCategory(lmEntryRepository.findByAbbreviation(lmCategory.getAbbreviation()));
//
//            if (l.getLipidMapsMainClass() == null) {
//                throw new RuntimeException("LipidMaps main class was null!");
//            }
//            dbLipid.setLipidMapsMainClass(lmEntryRepository.findByAbbreviation(l.getLipidMapsMainClass().getAbbreviation()));
//
//            if (l.getLipidMapsSubClass() == null) {
//                throw new RuntimeException("LipidMaps sub class was null!");
//            }
//            dbLipid.setLipidMapsSubClass(lmEntryRepository.findByAbbreviation(l.getLipidMapsSubClass().getAbbreviation()));
//        }
//    }
//    private void addParents(final LipidMapsEntry entry, final String principal) {
//        LipidMapsEntry activeParent = entry.getParent();
//        if (activeParent != null) {
//            LipidMapsEntry activeParentEntry = lmEntryRepository.findByNativeId(activeParent.getNativeId());
//            if (activeParentEntry == null) {
//                activeParent.setCreatedBy(principal);
//                activeParent.setUpdatedBy(principal);
//                activeParent.setTransactionUuid(this.jobId + "");
//                activeParentEntry = lmEntryRepository.save(activeParent);
//            } else {
//                activeParentEntry.setUpdatedBy(principal);
//            }
//            entry.setParent(activeParentEntry);
//            HasLipidMapsChild hlmc = new HasLipidMapsChild(activeParentEntry, entry);
//            hlmc.setCreatedBy(principal);
//            hlmc.setUpdatedBy(principal);
//            hlmc.setTransactionUuid(this.jobId + "");
//            hasLipidMapsChildRepository.save(hlmc);
//            addParents(activeParentEntry, principal);
//        }
//    }
}
