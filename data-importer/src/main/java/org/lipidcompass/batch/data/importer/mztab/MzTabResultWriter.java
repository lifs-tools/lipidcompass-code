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
package org.lipidcompass.batch.data.importer.mztab;

import org.lipidcompass.data.parser.GoslinAllGrammarsParser;
import com.arangodb.springframework.core.ArangoOperations;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
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
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.ControlledVocabulary;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.Lipid.LipidBuilder;
import org.lipidcompass.data.model.LipidQuantity;
import org.lipidcompass.data.model.MzTabAssay;
import org.lipidcompass.data.model.MzTabData;
import org.lipidcompass.data.model.MzTabResult;
import org.lipidcompass.data.model.MzTabStudyVariable;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.relations.HasMzTabResultLipidQuantity;
import org.lipidcompass.data.model.relations.HasCvParameterReference;
import org.lipidcompass.data.model.relations.HasCvParameterReference.ReferenceType;
import org.lipidcompass.data.model.relations.HasCvParent;
import org.lipidcompass.data.model.relations.HasLipidMapsReference;
import org.lipidcompass.data.model.relations.HasSwissLipidsReference;
import de.isas.mztab2.model.Assay;
import de.isas.mztab2.model.MsRun;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.SmallMoleculeSummary;
import de.isas.mztab2.model.StudyVariable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.FattyAcid;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lifstools.jgoslin.domain.LipidSpecies;
import org.lifstools.jgoslin.domain.LipidSpeciesInfo;
import org.lipidcompass.backend.repository.FattyAcylRepository;
import org.lipidcompass.backend.repository.HasFattyAcylRepository;
import org.lipidcompass.backend.repository.HasLipidQuantityLipidReferenceRepository;
import org.lipidcompass.backend.repository.HasMzTabResultRepository;
import org.lipidcompass.backend.repository.MzTabMsRunRepository;
import org.lipidcompass.backend.repository.StudyRepository;
import org.lipidcompass.backend.repository.SubmissionRepository;
import org.lipidcompass.data.model.CvParameter.CvParameterType;
import org.lipidcompass.data.model.FattyAcyl;
import org.lipidcompass.data.model.MzTabMsRun;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.SubmissionStatus;
import org.lipidcompass.data.model.relations.HasFattyAcyl;
import org.lipidcompass.data.model.relations.HasMzTabResult;
import org.lipidcompass.data.model.submission.Submission;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class MzTabResultWriter implements ItemWriter<MzTabResult> {

    private final MzTabResultRepository mzTabResultRepository;
    private final MzTabDataRepository mzTabDataRepository;
    private final LipidRepository lipidRepository;
    private final FattyAcylRepository fattyAcylRepository;
    private final LipidQuantityRepository lipidQuantityRepository;
    private final ControlledVocabularyRepository cvRepository;
    private final CvParameterRepository cvParamRepository;
    private final LipidMapsEntryRepository lmEntryRepository;
    private final SwissLipidsEntryRepository slEntryRepository;
    private final HasAssayLipidQuantityRepository hasAssayLipidQuantityRepository;
    private final HasCvParentRepository hasCvParentRepository;
    private final HasLipidMapsReferenceRepository hasLipidMapsReferenceRepository;
    private final HasFattyAcylRepository hasFattyAcylRepository;
    private final HasSwissLipidsReferenceRepository hasSwissLipidsReferenceRepository;
    private final HasCvParameterReferenceRepository hasCvParameterReferenceRepository;
    private final HasMzTabResultRepository hasMzTabResultRepository;
    private final HasLipidQuantityLipidReferenceRepository hasLipidQuantityLipidReferenceRepository;
    private final MzTabAssayRepository mzTabAssayRepository;
    private final MzTabStudyVariableRepository mzTabStudyVariableRepository;
    private final MzTabMsRunRepository mzTabMsRunRepository;
    private final SubmissionRepository submissionRepository;
    private final StudyRepository studyRepository;
    private final GoslinAllGrammarsParser lipidNameParser;
    private final ArangoOperations arangoOperations;
    private final String arangodbDatabase;
    private final String principal;
    private final Long maxRows;
    private String submissionId;
    private final LinkedHashMap<String, CvParameterType> cvParameterTypeMap;

    @Autowired
    public MzTabResultWriter(MzTabResultRepository mzTabResultRepository,
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
            GoslinAllGrammarsParser lipidNameParser,
            ArangoOperations arangoOperations,
            String arangodbDatabase,
            String principal,
            Long maxRows) {
        this.mzTabResultRepository = mzTabResultRepository;
        this.mzTabDataRepository = mzTabDataRepository;
        this.lipidRepository = lipidRepository;
        this.fattyAcylRepository = fattyAcylRepository;
        this.lipidQuantityRepository = lipidQuantityRepository;
        this.cvRepository = cvRepository;
        this.cvParamRepository = cvParamRepository;
        this.lmEntryRepository = lmEntryRepository;
        this.slEntryRepository = slEntryRepository;
        this.hasAssayLipidQuantityRepository = hasAssayLipidQuantityRepository;
        this.hasCvParentRepository = hasCvParentRepository;
        this.hasLipidMapsReferenceRepository = hasLipidMapsReferenceRepository;
        this.hasFattyAcylRepository = hasFattyAcylRepository;
        this.hasSwissLipidsReferenceRepository = hasSwissLipidsReferenceRepository;
        this.hasCvParameterReferenceRepository = hasCvParameterReferenceRepository;
        this.hasMzTabResultRepository = hasMzTabResultRepository;
        this.hasLipidQuantityLipidReferenceRepository = hasLipidQuantityLipidReferenceRepository;
        this.mzTabAssayRepository = mzTabAssayRepository;
        this.mzTabStudyVariableRepository = mzTabStudyVariableRepository;
        this.mzTabMsRunRepository = mzTabMsRunRepository;
        this.submissionRepository = submissionRepository;
        this.studyRepository = studyRepository;
        this.lipidNameParser = lipidNameParser;
        this.arangoOperations = arangoOperations;
        this.arangodbDatabase = arangodbDatabase;
        this.principal = principal;
        this.maxRows = maxRows;
        this.cvParameterTypeMap = new LinkedHashMap<>();
        this.cvParameterTypeMap.put("BTO:0000131", CvParameterType.MARKER);
        this.cvParameterTypeMap.put("MS:1000130", CvParameterType.MARKER);
        this.cvParameterTypeMap.put("MS:1002890", CvParameterType.MARKER);
        this.cvParameterTypeMap.put("NCBITaxon:9606", CvParameterType.MARKER);
        this.cvParameterTypeMap.put("NCIT:C61037", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C61041", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C61042", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C16358", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C16564", CvParameterType.NOMINAL);
        this.cvParameterTypeMap.put("NCIT:C16741", CvParameterType.NOMINAL);
        this.cvParameterTypeMap.put("NCIT:C17357", CvParameterType.NOMINAL);
        this.cvParameterTypeMap.put("NCIT:C25150", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C25298", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C25299", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C61032", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C61042", CvParameterType.FLOAT);
        this.cvParameterTypeMap.put("NCIT:C64206", CvParameterType.NOMINAL);
        this.cvParameterTypeMap.put("NCIT:C67147", CvParameterType.BOOLEAN);
        this.cvParameterTypeMap.put("NCIT:C67432", CvParameterType.MARKER);
        this.cvParameterTypeMap.put("NCIT:C80400", CvParameterType.FLOAT);
    }

    private Long jobId;

    @BeforeStep
    public void getInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        this.jobId = jobExecution.getJobId();
        this.submissionId = jobExecution.getJobParameters().getString("submissionId");
    }

    @Transactional
    @Override
    public void write(
            Chunk<? extends MzTabResult> items) throws Exception {
        log.info("Received {} items for writing!", items.size());
        initializeCollections();
        Map<String, CvParameter> allCvParameterAccessions = new LinkedHashMap<>();
        List<MzTabResult> results = items.getItems().stream().map((l) -> {
            return handleMzTabResult(l, allCvParameterAccessions);
        }).collect(Collectors.toList());
        handleDatasetSubmission(results, allCvParameterAccessions);
    }

    private void handleDatasetSubmission(List<MzTabResult> results, Map<String, CvParameter> allCvParameterAccessions) throws NullPointerException {
        Optional<Submission> submission = submissionRepository.findById(this.submissionId);
        if (submission.isPresent()) {
            log.info("Creating study object for submission id {}", this.submissionId);
            Study study = submission.get().getStudy();
            log.info("Adding {} mzTab results for submission id {}", results.size(), this.submissionId);
            results.stream().forEach((t) -> {
                HasMzTabResult hasMzTabResult = HasMzTabResult.builder().transactionUuid(jobId + "").visibility(Visibility.PRIVATE).from(study).to(t).build();
                List<CvParameter> uniqueCvParameters = allCvParameterAccessions.values().stream().map((cvParameter) -> {
                    CvParameter copyWithoutValue = CvParameter.builder().transactionUuid(jobId + "").accession(cvParameter.getAccession()).cvParameterType(cvParameter.getCvParameterType()).name(cvParameter.getName()).referenceType(cvParameter.getReferenceType()).visibility(cvParameter.getVisibility()).build();
                    return copyWithoutValue;
                }).collect(Collectors.toList());
                Set<CvParameter> uniqueCvParameterSet = new LinkedHashSet<>(uniqueCvParameters);
                Iterable<CvParameter> persistedUniqueCvParameters = this.cvParamRepository.saveAll(uniqueCvParameterSet);
                this.hasMzTabResultRepository.save(hasMzTabResult);
                this.hasCvParameterReferenceRepository.saveAll(StreamSupport.stream(persistedUniqueCvParameters.spliterator(), false).map((cvParameter) -> {
                    return HasCvParameterReference.builder().transactionUuid(jobId + "").visibility(Visibility.PRIVATE).from(study).to(cvParameter).build();
                }).collect(Collectors.toList()));
            });
            Submission sub = submission.get();
            sub.setVisibility(Visibility.PRIVATE);
            sub.setStatus(SubmissionStatus.IN_CURATION);
            submissionRepository.save(sub);
            study.setId(this.submissionId);
            study.setVisibility(Visibility.PRIVATE);
            study.setStatus(SubmissionStatus.IN_CURATION);
            studyRepository.save(study);
            log.info("Finished importing data for submission id {}", this.submissionId);
        } else {
            throw new NullPointerException("Could not retrieve submission with id: " + this.submissionId);
        }
    }

    private void initializeCollections() throws DataAccessException {
        arangoOperations.collection(User.class).count();
        arangoOperations.collection(MzTabResult.class).count();
        arangoOperations.collection(MzTabData.class).count();
        arangoOperations.collection(Lipid.class).count();
        arangoOperations.collection(ControlledVocabulary.class).count();
        arangoOperations.collection(CvParameter.class).count();
        arangoOperations.collection(HasCvParent.class).count();
        arangoOperations.collection(HasMzTabResultLipidQuantity.class).count();
        arangoOperations.collection(MzTabAssay.class).count();
        arangoOperations.collection(MzTabStudyVariable.class).count();
        arangoOperations.collection(FattyAcyl.class).count();
        arangoOperations.collection(HasFattyAcyl.class).count();
        arangoOperations.collection(Study.class).count();
        arangoOperations.collection(Submission.class).count();
    }

    private MzTabResult handleMzTabResult(MzTabResult l, Map<String, CvParameter> allCvParameterAccessions) throws RuntimeException {
//        StreamTransactionOptions sto = new StreamTransactionOptions().;
//        StreamTransactionEntity ste = arangoOperations.driver().db(DbName.of(arangodbDatabase)).beginStreamTransaction(sto);
        MzTabResult updated = createOrUpdateMzTabResult(l);
        Map<String, CvParameter> cvParameterAccessions = new LinkedHashMap<>();
        String transactionUuid = jobId + "";

        log.info("Creating controlled vocabularies for MzTabResult {}", updated.getId());
        Map<String, ControlledVocabulary> cvm = createControlledVocabularies(updated);
        log.info("Handling cv terms for MzTabResult {}", updated.getId());

        Parameter quantificationUnit = updated.getMzTabData().getMzTab().getMetadata().getSmallMoleculeQuantificationUnit();
        Optional<CvParameter> cvQuantificationUnit = createCvParameter("/" + l.getMzTabSummary().getId() + "/metadata/small_molecule_quantification_unit/", cvm, quantificationUnit, principal, CvParameterType.MARKER, ReferenceType.SMALL_MOLECULE_QUANTIFICATION_UNIT, transactionUuid);
        cvQuantificationUnit.ifPresent((cvParameter) -> {
            cvParameterAccessions.put(cvParameter.getAccession(), cvParameter);
        });
        var instrumentParameters = addInstruments(updated, cvm, transactionUuid, cvParameterAccessions);
        var studyVariableLookup = addStudyVariables(l, cvm, transactionUuid, cvParameterAccessions);
        var assayToCvParameters = addAssays(updated, studyVariableLookup, cvm, instrumentParameters, transactionUuid);

        // Add lipid entries
        long effectiveRows = (maxRows == null || maxRows < 0) ? updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size() : maxRows;
        log.info("Handling {}/{} SMLs for MzTabResult {}", effectiveRows, updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size(), updated.getId());
        var lipids = addLipids(updated);
        // process SmallMoleculeSummary
        var totalAssayAbundances = addAssayAbundances(updated);
        log.info("Importing SML rows");
        addLipidQuantities(updated, effectiveRows, lipids, l, cvm, transactionUuid, assayToCvParameters, cvParameterAccessions, totalAssayAbundances, cvQuantificationUnit, allCvParameterAccessions);
        return createOrUpdateMzTabResult(updated);
    }

    private void addLipidQuantities(MzTabResult updated, long effectiveRows, Map<Integer, Collection<Lipid>> lipids, MzTabResult l, Map<String, ControlledVocabulary> cvm, String transactionUuid, Map<Integer, AssayStudyVariableCvs> assayToCvParameters, Map<String, CvParameter> cvParameterAccessions, Map<Integer, Double> totalAssayAbundances, Optional<CvParameter> cvQuantificationUnit, Map<String, CvParameter> allCvParameterAccessions) throws IllegalStateException {
        StopWatch stopWatch = new StopWatch("Summary Row Creation");
        List<LipidQuantity> lipidQuantities = new ArrayList<>();
        IntStream.range(0, updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size()).forEach((rowCounter) -> {
            try {
//                SecurityContext ctx = SecurityContextHolder.createEmptyContext();
//                SecurityContextHolder.setContext(context);
                SmallMoleculeSummary summary = updated.getMzTabData().getMzTab().getSmallMoleculeSummary().get(rowCounter);
                stopWatch.start("Handling row input " + (rowCounter + 1));
                log.debug("Handling row {}/{}", (rowCounter + 1), effectiveRows);
                if ((rowCounter + 1) == effectiveRows && maxRows != null && maxRows != -1) {
                    log.info("Import job processed {}/{} rows. Set maxRows=-1 to process all rows!", (rowCounter + 1), effectiveRows);
                    return;
                }
                List<Double> acrossAssayAbundances = summary.getAbundanceAssay();
                Optional<Collection<Lipid>> referencedLipids = Optional.ofNullable(lipids.get(rowCounter));
                Optional<CvParameter> bestIdConfidenceMeasure = createCvParameter(
                        l.getMzTabSummary().getId() + "/summary/sml[" + summary.getSmlId() + "]/best_id_confidence_measure",
                        cvm,
                        summary.getBestIdConfidenceMeasure(), principal, CvParameterType.MARKER, ReferenceType.BEST_ID_CONFIDENCE_MEASURE,
                        transactionUuid
                );
                log.debug("Saving assay, study variable and cv terms");
                updated.getMzTabData().getMzTab().getMetadata().getAssay().forEach(a -> {
                    AssayStudyVariableCvs asvc = assayToCvParameters.get(a.getId());
                    asvc.cvParameters.forEach(cvParam -> {
                        cvParameterAccessions.put(cvParam.getAccession(), cvParam);
                    });
                    lipidQuantities.add(buildLipidQuantity(asvc.getAssay(), asvc.getStudyVariable(), updated, summary, bestIdConfidenceMeasure, acrossAssayAbundances, totalAssayAbundances.getOrDefault(a.getId(), 0.0d), referencedLipids, cvm, cvQuantificationUnit));
                });
            } catch (RuntimeException re) {
                throw re;
            } finally {
//                SecurityContextHolder.clearContext();
                stopWatch.stop();
                log.debug("Row import for row {} took {} s", (rowCounter + 1), stopWatch.getLastTaskTimeMillis() / 1000.d);
            }
        });
        try {
            log.info("Saving {} lipid quantities", lipidQuantities.size());
//            Map<LipidQuantity, Collection<Lipid>> lqToLipidEdges = new LinkedHashMap<>();
//            lipidQuantities.stream().forEach(lq -> lqToLipidEdges.put(lq, lq.getLipid()));

            stopWatch.start("Saving lipid quantities");
            List<HasMzTabResultLipidQuantity> mzTabToQuantEdges = Lists.partition(lipidQuantities, 2000).stream().map((t) -> {
                return lipidQuantityRepository.createOrUpdateAll(t, LipidQuantity.class, "nativeId", (lipidQuantity) -> lipidQuantity.getNativeId());
            }).map((t) -> {
                return StreamSupport.stream(t.spliterator(), false).map(lipidQuant -> {
                    return HasMzTabResultLipidQuantity.builder().
                            //                            id(HasMzTabResultLipidQuantity.buildId(updated, lipidQuant)).
                            mzTabResult(updated).
                            lipidQuantity(lipidQuant).
                            visibility(Visibility.PRIVATE).
                            transactionUuid(jobId + "").build();
                }).collect(Collectors.toList());
            }).flatMap(Collection::stream).collect(Collectors.toList());
//            log.info("Saving {} lipidQuantity to lipid edges!", lqToLipidEdges.keySet().size());
//            List<HasLipidQuantityLipidReference> lqLipidReferenceEdges = lqToLipidEdges.entrySet().stream().map(
//                    entry -> {
//                        return entry.getValue().stream().map(
//                                (lipid) -> {
//                                    return new HasLipidQuantityLipidReference(entry.getKey(), lipid);
//                                }).collect(Collectors.toList());
//                    }).flatMap(Collection::stream).collect(Collectors.toList());
//            Lists.partition(lqLipidReferenceEdges, 2000).stream().forEach((t) -> {
//                hasLipidQuantityLipidReferenceRepository.saveAll(t);
//            });
//                    map(lipidQuant -> {
//                        return HasMzTabResultLipidQuantity.builder().
//                                id(HasMzTabResultLipidQuantity.buildId(updated, lipidQuant)).
//                                mzTabResult(updated).
//                                lipidQuantity(lipidQuant).
//                                transactionUuid(jobId + "").build();
//                    }).collect(Collectors.toList());
//            StreamSupport.stream(Iterables.partition(lipidQuantities, 10000).spliterator(), false).;
//            List<HasAssayLipidQuantity> mzTabToQuantEdges
//                    = Streams.stream(lipidQuantityRepository.saveAll(lipidQuantities)).map(lipidQuant -> {
//                        return HasMzTabResultLipidQuantity.builder().
//                                id(HasMzTabResultLipidQuantity.buildId(updated, lipidQuant)).
//                                mzTabResult(updated).
//                                lipidQuantity(lipidQuant).
//                                transactionUuid(jobId + "").build();
//                    }).collect(Collectors.toList());
            log.info("Saving {} mzTabResult lipid quantity edges!", mzTabToQuantEdges.size());
            Lists.partition(mzTabToQuantEdges, 2000).stream().forEach((t) -> {
                hasAssayLipidQuantityRepository.saveAll(t);
            });
//            hasAssayLipidQuantityRepository.saveAll(mzTabToQuantEdges);
            log.debug("Done importing {} quantities!", lipidQuantities.size());
            updated.setCvParameters(new ArrayList<>(cvParameterAccessions.values()));
            allCvParameterAccessions.putAll(cvParameterAccessions);
        } finally {
            stopWatch.stop();
            log.info("Saving lipid quantities took {} s", stopWatch.getLastTaskTimeMillis() / 1000.d);
        }
    }

    private Map<Integer, Double> addAssayAbundances(MzTabResult updated) {
        Map<Integer, Double> totalAssayAbundances = new LinkedHashMap<>();
        log.info("Calculating totalAssayAbundances");
        IntStream.range(0, updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size()).forEach((rowCounter) -> {
            SmallMoleculeSummary summary = updated.getMzTabData().getMzTab().getSmallMoleculeSummary().get(rowCounter);
            List<Double> acrossAssayAbundances = summary.getAbundanceAssay();
            for (int assayId = 1; assayId <= acrossAssayAbundances.size(); assayId++) {
                totalAssayAbundances.put(assayId, totalAssayAbundances.getOrDefault((assayId - 1), 0.0d) + acrossAssayAbundances.get(assayId - 1));
            }
        });
        return totalAssayAbundances;
    }

    private Map<Integer, Collection<Lipid>> addLipids(MzTabResult updated) {
        Map<Integer, Collection<Lipid>> lipids = new LinkedHashMap<>();
        log.info("Creating {} lipid entries in database", updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size());
        IntStream.range(0, updated.getMzTabData().getMzTab().getSmallMoleculeSummary().size()).forEach((rowCounter) -> {
            SmallMoleculeSummary summary = updated.getMzTabData().getMzTab().getSmallMoleculeSummary().get(rowCounter);
            Optional<Collection<Lipid>> referencedLipids = createOrUpdateReferencedLipid(summary);
            if (referencedLipids.isPresent()) {
                lipids.put((rowCounter), referencedLipids.get());
            }
        });
        return lipids;
    }

    private Map<Integer, AssayStudyVariableCvs> addAssays(MzTabResult updated, Map<Integer, StudyVariable> studyVariableLookup, Map<String, ControlledVocabulary> cvm, Map<Integer, List<CvParameter>> instrumentParameters, String transactionUuid) {
        Map<Integer, AssayStudyVariableCvs> assayToCvParameters = new LinkedHashMap<>();
        updated.getMzTabData().getMzTab().getMetadata().getAssay().forEach(a -> {
            if (assayToCvParameters.containsKey(a.getId())) {
                log.debug("Skipping creation of cvTerms for assay {}", a.getId());
            } else {
                assayToCvParameters.put(a.getId(), buildAndStoreAssayAndStudyVariable(updated, a, studyVariableLookup.get(a.getId()), cvm, instrumentParameters, transactionUuid));
            }
        });
        return assayToCvParameters;
    }

    private Map<Integer, StudyVariable> addStudyVariables(MzTabResult l, Map<String, ControlledVocabulary> cvm, String transactionUuid, Map<String, CvParameter> cvParameterAccessions) {
        log.info("Mapping study variables and assays");
        Map<Integer, StudyVariable> studyVariableLookup = new HashMap<>();
        l.getMzTabData().getMzTab().getMetadata().getStudyVariable().forEach(sv -> {
            String assayRefIds = sv.getAssayRefs().stream().map(assay -> {
                return assay.getId() + "";
            }).collect(Collectors.joining(",", "[", "]"));
            log.debug("Study variable {} assay-refs: {}", sv.getId(), assayRefIds);
            sv.getAssayRefs().forEach(a -> {
                log.debug("Mapping assay {} to study variable {}", a.getId(), sv.getId());
                studyVariableLookup.put(a.getId(), sv);
                if (sv.getFactors() != null) {
                    for (int i = 0; i < sv.getFactors().size(); i++) {
                        Parameter svf = sv.getFactors().get(i);
                        Optional<CvParameter> svfParameter = createCvParameter("/" + l.getMzTabSummary().getId() + "/metadata/small_molecule_quantification_unit/study_variable[" + sv.getId() + "]/factor[" + (i + 1) + "]/assay[" + a.getId() + "]/" + svf.getCvAccession(), cvm, svf, principal, cvParameterTypeMap.getOrDefault(svf.getCvAccession(), CvParameterType.MARKER), ReferenceType.STUDY_VARIABLE_FACTOR, transactionUuid);
                        svfParameter.ifPresent((svfParameterValue) -> {
                            cvParameterAccessions.put(svfParameterValue.getAccession(), svfParameterValue);
                        });
                    }
//                    sv.getFactors().forEach(svf -> {
//
//                    });
                }
            });
        });
        return studyVariableLookup;
    }

    private Map<Integer, List<CvParameter>> addInstruments(MzTabResult updated, Map<String, ControlledVocabulary> cvm, String transactionUuid, Map<String, CvParameter> cvParameterAccessions) {
        //        Map<Integer, MsRun>
//        List<MzTabMsRun> mzTabMsRuns = mzTabMsRunRepository.saveAll(updated.getMzTabData().getMzTab().getMetadata().getMsRun().stream().map((msRun) -> {
//            String msRunIdString = updated.getMzTabData().getMzTab().getMetadata().getMzTabID() + "/metadata/ms_run/" + msRun.getId();
//
//            return MzTabMsRun.builder().id(ArangoBaseEntity.uuidFromString(msRunIdString)).msRun(msRun).transactionUuid(jobId + "").build();
//        }).collect(Collectors.toList()).spliterator(), false).collect(Collectors.toList());
        Map<Integer, List<CvParameter>> instrumentParameters = updated.getMzTabData().getMzTab().getMetadata().getInstrument().stream().map((instrument) -> {
            var parameters = new ArrayList<CvParameter>();
            parameters.addAll(createCvParameters(cvm, instrument.getAnalyzer(), principal, ReferenceType.INSTRUMENT_ANALYZER, transactionUuid));
            parameters.addAll(createCvParameters(cvm, Arrays.asList(instrument.getDetector()), principal, ReferenceType.INSTRUMENT_DETECTOR, transactionUuid));
            parameters.addAll(createCvParameters(cvm, Arrays.asList(instrument.getName()), principal, ReferenceType.INSTRUMENT_NAME, transactionUuid));
            parameters.addAll(createCvParameters(cvm, Arrays.asList(instrument.getSource()), principal, ReferenceType.INSTRUMENT_SOURCE, transactionUuid));
            return Map.entry(instrument.getId(), StreamSupport.stream(
                    cvParamRepository.createOrUpdateAll(parameters, CvParameter.class, "nativeId", (t) -> t.getNativeId()).spliterator(),
                    false
            ).collect(Collectors.toList())
            //                    cvParamRepository.saveAll(parameters).spliterator(), false).collect(Collectors.toList())
            );
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        instrumentParameters.entrySet().stream().forEach((entry) -> {
            entry.getValue().stream().forEach(cvParam -> {
                cvParameterAccessions.put(cvParam.getAccession(), cvParam);
            });
        });
        return instrumentParameters;
    }

    private MzTabMsRun buildAndStoreMsRun(MzTabResult updated, MsRun msRun, Map<String, ControlledVocabulary> cvm) {
        String msRunIdString = updated.getMzTabData().getMzTab().getMetadata().getMzTabID() + "/metadata/msRun/" + msRun.getId();
        MzTabMsRun mztmsrun = MzTabMsRun.builder().visibility(Visibility.PRIVATE).id(ArangoBaseEntity.uuidFromString(msRunIdString)).msRun(msRun).transactionUuid(jobId + "").build();
        log.debug("Trying to save mzTabMsRun: {}", mztmsrun.getId());
        return mzTabMsRunRepository.save(mztmsrun);
    }

    private AssayStudyVariableCvs buildAndStoreAssayAndStudyVariable(MzTabResult updated, Assay assay, StudyVariable studyVariable, Map<String, ControlledVocabulary> cvm, Map<Integer, List<CvParameter>> instrumentParameters, String transactionUuid) {
        String assayIdString = updated.getMzTabData().getMzTab().getMetadata().getMzTabID() + "/metadata/assay/" + assay.getId();
        MzTabAssay mzta = MzTabAssay.builder().visibility(Visibility.PRIVATE).mzTab(updated.getMzTabData().getMzTab()).assay(assay).transactionUuid(jobId + "").build();
        log.debug("Trying to save mzTabAssay: {}", mzta.getId());
        mzta = mzTabAssayRepository.save(mzta);

        List<CvParameter> allCvParameters = new ArrayList<>();

        if (assay.getCustom() != null && !assay.getCustom().isEmpty()) {
            log.debug("Adding assay custom CvParameters!");
            allCvParameters.addAll(createCvParameters(cvm, assay.getCustom(), principal, ReferenceType.ASSAY_CUSTOM, transactionUuid));
        } else {
            log.debug("Not adding assay custom CvParameters, assay-custom is null!");
        }
        if (assay.getSampleRef() != null) {
            log.debug("Adding sample CvParameters!");
            if (assay.getSampleRef().getCellType() != null && !assay.getSampleRef().getCellType().isEmpty()) {
                log.debug("Handling Assay SampleRef celltype cvParameters: {}", assay.getSampleRef().getCustom());
                allCvParameters.addAll(createCvParameters(cvm, assay.getSampleRef().getCellType(), principal, ReferenceType.SAMPLE_CELLTYPE, transactionUuid));
            }
            if (assay.getSampleRef().getDisease() != null && !assay.getSampleRef().getDisease().isEmpty()) {
                log.debug("Handling Assay SampleRef disease cvParameters: {}", assay.getSampleRef().getCustom());
                allCvParameters.addAll(createCvParameters(cvm, assay.getSampleRef().getDisease(), principal, ReferenceType.SAMPLE_DISEASE, transactionUuid));
            }
            if (assay.getSampleRef().getSpecies() != null && !assay.getSampleRef().getSpecies().isEmpty()) {
                log.debug("Handling Assay SampleRef organism cvParameters: {}", assay.getSampleRef().getCustom());
                allCvParameters.addAll(createCvParameters(cvm, assay.getSampleRef().getSpecies(), principal, ReferenceType.SAMPLE_ORGANISM, transactionUuid));
            }
            if (assay.getSampleRef().getTissue() != null && !assay.getSampleRef().getTissue().isEmpty()) {
                log.debug("Handling Assay SampleRef tissue cvParameters: {}", assay.getSampleRef().getCustom());
                allCvParameters.addAll(createCvParameters(cvm, assay.getSampleRef().getTissue(), principal, ReferenceType.SAMPLE_TISSUE, transactionUuid));
            }
            if (assay.getSampleRef().getCustom() != null && !assay.getSampleRef().getCustom().isEmpty()) {
                log.debug("Handling Assay SampleRef custom cvParameters: {}", assay.getSampleRef().getCustom());
                allCvParameters.addAll(createCvParameters(cvm, assay.getSampleRef().getCustom(), principal, ReferenceType.SAMPLE_CUSTOM, transactionUuid));
            }
        } else {
            log.debug("Not adding Sample CvParameters, sample is null!");
        }
        if (!assay.getMsRunRef().isEmpty()) {
            log.debug("Adding MsRun CvParameters!");
            allCvParameters.addAll(createCvParameters(cvm, assay.getMsRunRef().stream().map(MsRun::getFormat).filter(x -> x != null).collect(Collectors.toList()), principal, ReferenceType.MS_RUN_FORMAT, transactionUuid));
            allCvParameters.addAll(createCvParameters(cvm, assay.getMsRunRef().stream().map(MsRun::getFragmentationMethod).flatMap(list -> Stream.ofNullable(list)).flatMap(List::stream).filter(x -> x != null).collect(Collectors.toList()), principal, ReferenceType.MS_RUN_FRAGMENTATION_METHOD, transactionUuid));
            allCvParameters.addAll(createCvParameters(cvm, assay.getMsRunRef().stream().map(MsRun::getScanPolarity).flatMap(list -> Stream.ofNullable(list)).flatMap(List::stream).filter(x -> x != null).collect(Collectors.toList()), principal, ReferenceType.MS_RUN_SCAN_POLARITY, transactionUuid));
            allCvParameters.addAll(createCvParameters(cvm, assay.getMsRunRef().stream().map(MsRun::getHashMethod).filter(x -> x != null).collect(Collectors.toList()), principal, ReferenceType.MS_RUN_HASH_METHOD, transactionUuid));
            allCvParameters.addAll(createCvParameters(cvm, assay.getMsRunRef().stream().map(MsRun::getIdFormat).filter(x -> x != null).collect(Collectors.toList()), principal, ReferenceType.MS_RUN_ID_FORMAT, transactionUuid));
        } else {
            log.debug("Not adding MsRun CvParameters, msRun is null!");
        }

        MzTabStudyVariable mztsv = MzTabStudyVariable.builder().visibility(Visibility.PRIVATE).mzTab(updated.getMzTabData().getMzTab()).studyVariable(studyVariable).transactionUuid(jobId + "").build();

        log.debug("Adding StudyVariable CvParameters!");
        allCvParameters.addAll(createCvParameters(cvm, studyVariable.getFactors(), principal, ReferenceType.STUDY_VARIABLE_FACTOR, transactionUuid));

        log.debug("Trying to save mzTabStudyVariable: {}", mztsv.getId());
        Optional<MzTabStudyVariable> mztsvExists = mzTabStudyVariableRepository.findByNativeId(mztsv.getNativeId());
        if (mztsvExists.isPresent()) {
            //set id if entity with same native id already exists
            mztsv.setId(mztsvExists.get().getId());
        }
        mztsv = mzTabStudyVariableRepository.save(mztsv);
        log.debug("Saving cv parameters for: {}", mztsv.getId());
        allCvParameters = Lists.newArrayList(cvParamRepository.saveAll(allCvParameters));
        MzTabAssay fixedAssay = mzta;
        List<HasCvParameterReference> references = allCvParameters.
                stream().
                map((entry) -> {
                    CvParameter cvParameter = entry;
                    return HasCvParameterReference.builder().
                            visibility(Visibility.PRIVATE).
                            from(fixedAssay).
                            to(cvParameter).
                            id(HasCvParameterReference.buildId(fixedAssay, cvParameter)).
                            transactionUuid(jobId + "").
                            build();
                }).collect(Collectors.toList());

        log.debug("Saving a total of {} hasCvParameterReferences!", references.size());
        references = Lists.newArrayList(hasCvParameterReferenceRepository.saveAll(references));
        //refresh mzta with links to cv parameters
        mzta = mzTabAssayRepository.findById(mzta.getId()).orElseThrow();
        AssayStudyVariableCvs asvc = new AssayStudyVariableCvs(mzta, mztsv, allCvParameters, references);
        return asvc;
    }

    @Data
    private class AssayStudyVariableCvs {

        private final MzTabAssay assay;
        private final MzTabStudyVariable studyVariable;
        private final List<CvParameter> cvParameters;
        private final List<HasCvParameterReference> references;
    }

    private LipidQuantity buildLipidQuantity(MzTabAssay a, MzTabStudyVariable s, MzTabResult updated, SmallMoleculeSummary summary, Optional<CvParameter> bestIdConfidenceMeasure, List<Double> acrossAssayAbundances, Double totalWithinAssayAbundance, Optional<Collection<Lipid>> referencedLipids, Map<String, ControlledVocabulary> cvm, Optional<CvParameter> cvQuantificationUnit) {

        String idString = updated.getMzTabData().getMzTab().getMetadata().getMzTabID() + "/sml/" + summary.getSmlId() + "/assay/" + a.getId();
        String lipidQuantityNativeId = UUID.nameUUIDFromBytes(idString.getBytes(StandardCharsets.UTF_8)).toString();
        LipidQuantity.LipidQuantityBuilder lqb = LipidQuantity.builder();
        double quantityNormalizationFactor = 1.0d;
        CvParameter cvParam = cvQuantificationUnit.get();
        switch (cvParam.getAccession()) {
            case "NCIT:C48508" -> // Micromole per liter
                quantityNormalizationFactor = 1000.d;
            case "NCIT:C67432" -> { // Nanomole per liter
                quantityNormalizationFactor = 1.d;
            }
            case "NCIT:C67434" -> { // Picomole per liter
                quantityNormalizationFactor = 1 / 1000.d;
            }
            case "NCIT:C64387" -> { // millimole per liter
                quantityNormalizationFactor = 1000000.0d;
            }
            case "NCIT:C68892" -> { // Millimole per Kilogram, nmol/mg
                quantityNormalizationFactor = 1000000.0d;
            }
            case "NCIT:C85754" -> { // Nanomole per Kilogram, nmol/kg
                quantityNormalizationFactor = 1.0d;
            }
            default ->
                log.info("Unsupported cvQuantificationUnit, not normalizing quantity to nanomole per liter: {}", cvParam.getAccession() + " = " + cvParam.getName());
        }
        double assayQuantity = acrossAssayAbundances.get(a.getAssay().getId() - 1) * quantityNormalizationFactor;
        double assayRelativeQuantity = assayQuantity / totalWithinAssayAbundance * quantityNormalizationFactor;
        lqb.//nativeId(lipidQuantityNativeId).
                assay(a).
                studyVariable(s).
                assayQuantity(assayQuantity).
                assayRelativeQuantity(assayRelativeQuantity).
                identificationReliability(summary.getReliability());
        if (referencedLipids.isPresent()) {
            lqb.lipid(referencedLipids.get());
        }
        lqb.
                mzTabResultId(updated.getId()).
                quantificationUnit(cvQuantificationUnit.get()).
                smlId(summary.getSmlId());
        if (bestIdConfidenceMeasure.isPresent()) {
            lqb.bestIdConfidenceValue(summary.getBestIdConfidenceValue()).
                    bestIdentificationConfidenceMeasure(bestIdConfidenceMeasure.get());
        }

        lqb.visibility(Visibility.PRIVATE).
                //                createdBy(principal).
                //                updatedBy(principal).
                transactionUuid(jobId + "");
//        if (lipidQuantityRepository.existsById(lipidQuantityNativeId)) {
//            lqb.dateCreated(lipidQuantityRepository.findById(lipidQuantityNativeId).get().getDateCreated());
//        } else {
//            lqb.dateCreated(new Date());
//        }
        LipidQuantity lq = lqb.build();
        return lq;
    }

    protected Optional<Collection<Lipid>> createOrUpdateReferencedLipid(SmallMoleculeSummary summary) throws IllegalStateException {
        Set<Lipid> referencedLipids = new LinkedHashSet<>();
//        Optional<Collection<LipidAdduct>> normalizedNames = Optional.empty();
        if (summary.getChemicalName().size() > 1) {
            log.warn("Skipping handling of multiple chemical names: {} Currently not implemented!", summary.getChemicalName());
        } else {
            if (summary.getChemicalName().isEmpty()) {
                log.warn("Skipping handling of empty chemical names!");
            } else {
                for (String chemicalName : summary.getChemicalName()) {
                    Optional<LipidAdduct> normalizedName = Optional.empty();
                    try {
                        normalizedName = lipidNameParser.parse(chemicalName);
                    } catch (RuntimeException re) {
                        log.warn("Skipping name {} since it could not be parsed!", chemicalName, re);
                        throw new RuntimeException(re);
                    }
                    if (normalizedName.isPresent()) {
                        LipidAdduct la = normalizedName.get();
                        if (la == null) {
                            log.warn("Skipping name {} since it could not be parsed!", chemicalName);
//                            return referencedLipids;
                        } else {
                            String normalizedLipidName = la.getLipidString();
                            final LipidLevel level = la.getLipid().getInfo().getLevel();
                            Page<Lipid> lipidPage = lipidRepository.findByLipidLevelAndNormalizedShorthandNameMatches(level, null, 0.0d, 2000.0d, Arrays.asList(normalizedLipidName), null, Pageable.unpaged());
                            log.debug("Found {} lipids in db for name {}", lipidPage.getTotalElements(), normalizedLipidName);
                            Optional<Lipid> lipid = Optional.empty();
                            if (!lipidPage.isEmpty() && lipidPage.getTotalElements() > 0) {
                                lipid = lipidPage.filter((t) -> t.getLipidLevel() == level).stream().findFirst();
//                                if (lipidPage.getTotalElements() > 1) {
//                                    throw new IllegalStateException("Found more than one lipid for name " + normalizedLipidName);
//                                }
//                                lipid = lipidPage.get().findFirst();
                            } else {
                                lipid = addLipid(la);
                            }
                            if (lipid.isPresent()) {
                                referencedLipids.add(lipid.get());
                            } else {
                                log.warn("Lipid from db for name {} was null!", normalizedLipidName);
                            }
                        }
                    }
                }
            }
        }
        return Optional.of(referencedLipids);
    }

    protected Optional<Lipid> addLipid(LipidAdduct la) {
        log.info("Adding lipids for {}", la.getLipidString());
        List<Lipid> lipids = getParentsFor(la);
        log.info("Found {} parents for lipid: {}", lipids.size(), lipids.stream().map((t) -> t.getNormalizedShorthandName() + " (" + t.getLipidLevel() + ")").collect(Collectors.toList()));
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(lipidRepository.createOrUpdateAll(lipids, Lipid.class, "normalizedShorthandName", (t) -> t.getNormalizedShorthandName()).iterator(), Spliterator.ORDERED),
                false).map((optLipid) -> {
                    Optional<Lipid> lipid = addDbReferences(Optional.ofNullable(optLipid));
                    log.debug("Handling fatty acyls.");
                    return handleFattyAcyls(la, lipid);
                }).collect(Collectors.toList()).get(0);
    }

    protected Optional<Lipid> addDbReferences(Optional<Lipid> persistedLipid) {
        if (persistedLipid.isPresent()) {
            Lipid l = persistedLipid.get();
            List<HasLipidMapsReference> lmReferences = lmEntryRepository.findAllByNormalizedName(l.getNormalizedShorthandName(), Pageable.unpaged()).stream().map((lmEntry) -> {
                return HasLipidMapsReference.builder().transactionUuid(jobId + "").from(l).to(lmEntry).visibility(Visibility.PRIVATE).build();
            }).collect(Collectors.toList());
            log.debug("Adding {} LipidMaps references.", lmReferences.size());
            hasLipidMapsReferenceRepository.saveAll(lmReferences);
            List<HasSwissLipidsReference> slReferences = slEntryRepository.findAllByNormalizedName(l.getNormalizedShorthandName(), Pageable.unpaged()).stream().map((slEntry) -> {
                return HasSwissLipidsReference.builder().transactionUuid(jobId + "").from(l).to(slEntry).visibility(Visibility.PRIVATE).build();
            }).collect(Collectors.toList());
            log.debug("Adding {} SwissLipids references.", slReferences.size());
            hasSwissLipidsReferenceRepository.saveAll(slReferences);
            return Optional.ofNullable(lipidRepository.save(l));
        }
        return persistedLipid;
    }

    protected Optional<Lipid> handleFattyAcyls(LipidAdduct lipidAdduct, Optional<Lipid> persistedLipid) {
        if (persistedLipid.isPresent()) {
            Lipid lipid = persistedLipid.get();
            LipidSpecies ls = lipidAdduct.getLipid();
            LipidSpeciesInfo lsi = ls.getInfo();
            if (lsi.getLevel() == LipidLevel.CATEGORY || lsi.getLevel() == LipidLevel.CLASS || lsi.getLevel() == LipidLevel.UNDEFINED_LEVEL) {
                log.info("Skipping fatty acyls handling for lipid {} on level {}", ls.getLipidString(), lsi.getLevel());
                return persistedLipid;
            } else {
                log.debug("Handling fatty acyls for lipid {} on level {}", lipidAdduct.getLipidString(), lsi.getLevel());
                FattyAcyl lsiFa = FattyAcyl.builder().
                        name(lsi.getName()).
                        nCarbon(lsi.getNumCarbon()).
                        nDoubleBonds(lsi.getNDoubleBonds()).
                        //                        nHydroxy(lsi.getNHydroxy()).
                        faBondType(FattyAcyl.FaBondType.of(lsi.getLipidFaBondType())).
                        position(lsi.getPosition()).
                        visibility(Visibility.PUBLIC).
                        //                        createdBy(principal).
                        //                        updatedBy(principal).
                        transactionUuid(jobId + "").
                        build();
                lsiFa = fattyAcylRepository.save(lsiFa);
                lipid.setLipidSpeciesInfo(lsiFa);
                lipid = lipidRepository.save(lipid);
                for (FattyAcid e : ls.getFaList()) {
                    String key = e.getName();
                    FattyAcid fa = e;
                    FattyAcyl fal = FattyAcyl.builder().
                            name(key).
                            nCarbon(fa.getNumCarbon()).
                            nDoubleBonds(fa.getNDoubleBonds()).
                            //                            nHydroxy(fa.getNHydroxy()).
                            faBondType(FattyAcyl.FaBondType.of(fa.getLipidFaBondType())).
                            //                            faType(fa.getLipidFaBondType()).
                            position(fa.getPosition()).
                            visibility(Visibility.PUBLIC).
                            //                            createdBy(principal).
                            //                            updatedBy(principal).
                            transactionUuid(jobId + "").
                            build();
                    fal = fattyAcylRepository.save(fal);
                    HasFattyAcyl hfa = HasFattyAcyl.builder().
                            from(lipid).
                            to(fal).
                            visibility(Visibility.PUBLIC).
                            //                            createdBy(principal).
                            //                            updatedBy(principal).
                            transactionUuid(jobId + "").build();
                    hasFattyAcylRepository.save(hfa);
                }
                return lipidRepository.findById(lipid.getId());
            }
        }
        return persistedLipid;
    }

    protected List<Lipid> getParentsFor(LipidAdduct la) {
        LipidLevel level = la.getLipid().getInfo().getLevel();
        // return Arrays.asList(lipidFor(la, level));
        switch (level) {
            case CATEGORY, UNDEFINED_LEVEL, NO_LEVEL -> {
                return Arrays.asList(
                        lipidFor(la, level)
                );
            }
            case CLASS -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case SPECIES -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case MOLECULAR_SPECIES -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.SPECIES),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case SN_POSITION -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.MOLECULAR_SPECIES),
                        lipidFor(la, LipidLevel.SPECIES),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case STRUCTURE_DEFINED -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.SN_POSITION),
                        lipidFor(la, LipidLevel.MOLECULAR_SPECIES),
                        lipidFor(la, LipidLevel.SPECIES),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case FULL_STRUCTURE -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.STRUCTURE_DEFINED),
                        lipidFor(la, LipidLevel.SN_POSITION),
                        lipidFor(la, LipidLevel.MOLECULAR_SPECIES),
                        lipidFor(la, LipidLevel.SPECIES),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
            case COMPLETE_STRUCTURE -> {
                return Arrays.asList(
                        lipidFor(la, level),
                        lipidFor(la, LipidLevel.FULL_STRUCTURE),
                        lipidFor(la, LipidLevel.STRUCTURE_DEFINED),
                        lipidFor(la, LipidLevel.SN_POSITION),
                        lipidFor(la, LipidLevel.MOLECULAR_SPECIES),
                        lipidFor(la, LipidLevel.SPECIES),
                        lipidFor(la, LipidLevel.CLASS),
                        lipidFor(la, LipidLevel.CATEGORY)
                );
            }
        }
        return Collections.emptyList();
    }

    // FIXME this creates too many lipids, we need to create a lipid hierarchy
    protected Lipid lipidFor(LipidAdduct la, LipidLevel level) {
        String normalizedLipidName = la.getLipidString(level);
        log.info("Adding lipid with normalized name {} for {} on level {}", normalizedLipidName, la.getLipidString(), level);
        // create lipid
        LipidBuilder builder = Lipid.builder();
        if (level != LipidLevel.CATEGORY && level != LipidLevel.CLASS) {
            builder.chemicalFormula(la.getSumFormula()).
                    //commonName().
                    exactMass((float) la.getMass());
            //inchi("").
            //inchiKey("").
            //mdlModel(mzTabCore).
            //smiles(chemicalName).
        }

        //                                    lipidMapsCategory(la.getLipid().getLipidCategory()).
        //                                    lipidMapsMainClass().
        //lipidMapsSubClass().
        String nativeId = "LCLID:" + normalizedLipidName;
        UUID uuid = UUID.nameUUIDFromBytes((nativeId).getBytes(Charsets.UTF_8));
        builder.//id(uuid.toString()).
                //                nativeId(nativeId).
                lipidCategory(la.getLipidString(LipidLevel.CATEGORY)).
                lipidClass(la.getLipidString(LipidLevel.CLASS)).
                //                                        nativeUrl(mzTabCore)
                normalizedShorthandName(normalizedLipidName).
                //                                        smiles(chemicalName).
                //                                        swissLipidsEntry(swissLipidsEntry).
                //                                        synonyms(synonyms)
                //                                        systematicName(chemicalName)
                lipidLevel(level).
                visibility(Visibility.PUBLIC).
                //                createdBy(principal).
                //                updatedBy(principal).
                transactionUuid(jobId + "");
        switch (level) {
            case NO_LEVEL, UNDEFINED_LEVEL, CATEGORY, CLASS, SPECIES ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES));
            case MOLECULAR_SPECIES ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES)).
                        lipidMolecularSpecies(la.getLipidString(LipidLevel.MOLECULAR_SPECIES));
            case SN_POSITION ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES)).
                        lipidMolecularSpecies(la.getLipidString(LipidLevel.MOLECULAR_SPECIES)).
                        lipidSnPosition(la.getLipidString(LipidLevel.SN_POSITION));
            case STRUCTURE_DEFINED ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES)).
                        lipidMolecularSpecies(la.getLipidString(LipidLevel.MOLECULAR_SPECIES)).
                        lipidSnPosition(la.getLipidString(LipidLevel.SN_POSITION)).
                        lipidStructureDefined(la.getLipidString(LipidLevel.STRUCTURE_DEFINED));
            case FULL_STRUCTURE ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES)).
                        lipidMolecularSpecies(la.getLipidString(LipidLevel.MOLECULAR_SPECIES)).
                        lipidSnPosition(la.getLipidString(LipidLevel.SN_POSITION)).
                        lipidStructureDefined(la.getLipidString(LipidLevel.STRUCTURE_DEFINED)).
                        lipidFullStructure(la.getLipidString(LipidLevel.FULL_STRUCTURE));
            case COMPLETE_STRUCTURE ->
                builder.lipidSpecies(la.getLipidString(LipidLevel.SPECIES)).
                        lipidMolecularSpecies(la.getLipidString(LipidLevel.MOLECULAR_SPECIES)).
                        lipidSnPosition(la.getLipidString(LipidLevel.SN_POSITION)).
                        lipidStructureDefined(la.getLipidString(LipidLevel.STRUCTURE_DEFINED)).
                        lipidFullStructure(la.getLipidString(LipidLevel.FULL_STRUCTURE)).
                        lipidCompleteStructure(la.getLipidString(LipidLevel.COMPLETE_STRUCTURE));
        }
        return builder.build();
    }

    protected MzTabResult createOrUpdateMzTabResult(MzTabResult l) {
        l.getMzTabData().setTransactionUuid(jobId + "");
        l.getMzTabData().setNativeId(l.getMzTabData().getMzTab().getMetadata().getMzTabID());
        l.getMzTabData().setVisibility(Visibility.PRIVATE);
        MzTabData mzTabData = mzTabDataRepository.createOrUpdateAll(
                Arrays.asList(l.getMzTabData()),
                MzTabData.class,
                "nativeId",
                (mzTabDataItem) -> mzTabDataItem.getNativeId()
        ).iterator().next();

        //MzTabData mzTabData = mzTabDataRepository.save(l.getMzTabData());
        l.setMzTabData(mzTabData);
        log.debug("Persisting mzTabResult: {} ", l.getMzTabSummary().getId());
        l.setCreatedBy(principal);
        l.setUpdatedBy(principal);
        l.setNativeId(l.getMzTabData().getMzTab().getMetadata().getMzTabID());
        l.setTransactionUuid(jobId + "");
        l.setVisibility(Visibility.PUBLIC);
        l.setSubmissionStatus(SubmissionStatus.PUBLISHED);
        l.setCompleteness(MzTabResult.CompletenessLevel.SUMMARY);
        l.setRating(MzTabResult.Rating.AUTOMATICALLY_CHECKED);
        //MzTabResult updated = mzTabResultRepository.save(l);
        MzTabResult updated = mzTabResultRepository.createOrUpdateAll(
                Arrays.asList(l),
                MzTabResult.class,
                "nativeId",
                (mzTabResultDataItem) -> mzTabResultDataItem.getNativeId()
        ).iterator().next();
        log.debug("Persisted mzTabResult: {}", l.getMzTabSummary().getId());
        return updated;
    }

    protected Map<String, ControlledVocabulary> createControlledVocabularies(MzTabResult updated) {
        Collection<ControlledVocabulary> cvs = updated.getMzTabData().getMzTab().getMetadata().getCv().stream().map((cv) -> {
            return ControlledVocabulary.builder().
                    label(cv.getLabel().toUpperCase()).
                    name(cv.getFullName()).
                    uri(cv.getUri()).
                    version(cv.getVersion()).
                    visibility(Visibility.PRIVATE).
                    transactionUuid(jobId + "").
                    build();
        }).collect(Collectors.toList());
        log.debug("Saving {} controlled vocabularies!", cvs.size());
        Map<String, ControlledVocabulary> cvm = cvs.stream().map((cv) -> {
            ControlledVocabulary cvFromDb = cvRepository.findByLabelAndVersion(cv.getLabel().toUpperCase(), cv.getVersion());
            if (cvFromDb == null) {
                cv.setCreatedBy(principal);
                cv.setUpdatedBy(principal);
                cv.setTransactionUuid(jobId + "");
                cv.setVisibility(Visibility.PUBLIC);
                return cvRepository.save(cv);
            }
            return cvFromDb;
        }).collect(Collectors.toMap(ControlledVocabulary::getLabel, Function.identity()));
        return cvm;
    }

//    protected Iterable<HasCvParent> createCvParameterEdges(Map<String, ControlledVocabulary> cvm, Iterable<CvParameter> cvParameters, Map<String, String> accessionToVocabLookup, String principal) {
//        Set<HasCvParent> hasCvParent = Streams.stream(cvParameters).map((t) -> {
//            String cvKey = accessionToVocabLookup.get(t.getAccession());
//            if (cvKey == null) {
//                return null;
//            } else {
//                ControlledVocabulary cv = cvm.get(cvKey.toUpperCase());
//                HasCvParent parent = HasCvParent.builder().
//                        from(t).
//                        to(cv).
//                        visibility(Visibility.PUBLIC).
//                        build();
//                parent.setCreatedBy(principal);
//                parent.setUpdatedBy(principal);
//                parent.setTransactionUuid(this.jobId + "");
//                return parent;
//            }
//        }).filter((t) -> {
//            return t != null;
//        }).collect(Collectors.toSet());
//        log.debug("Attempting to save {} new HasCvParent edges", hasCvParent.size());
//        return hasCvParentRepository.saveAll(hasCvParent);
//    }
    protected Collection<CvParameter> createCvParameters(Map<String, ControlledVocabulary> cvm, List<Parameter> p, String principal, ReferenceType referenceType, String transactionUuid) {
        log.debug("CVs: {}", cvm.entrySet());
        if (p == null) {
            return Collections.emptyList();
        }
        List<CvParameter> cvParameters = p.stream().filter((t) -> {
            return t != null;
        }).map((t) -> {
            log.debug("Processing param: {}", t.getCvAccession());
            ControlledVocabulary cv = null;
            if (t.getCvLabel() != null) {
                cv = cvm.get(t.getCvLabel().toUpperCase());
                if (cv != null) {
                    log.debug("Found CV: {}, for param: {}", cv.getLabel(), t.getCvAccession());
                }
            }
            CvParameterType cvParameterType = cvParameterTypeMap.getOrDefault(t.getCvAccession(), CvParameterType.MARKER);
            String id = CvParameter.buildId(t.getCvAccession(), t.getName(), null, cv);

            CvParameter cvParam = new CvParameter(cvParameterType, referenceType, t.getCvAccession(), t.getName(), t.getValue(), cv, transactionUuid);
            cvParam.setCreatedBy(principal);
            cvParam.setUpdatedBy(principal);
            cvParam.setId(UUID.nameUUIDFromBytes(id.getBytes(Charset.forName("UTF8"))).toString());
            cvParam.setNativeId(id);
            cvParam.setVisibility(Visibility.PUBLIC);
            cvParam.setTransactionUuid(jobId + "");
            return cvParam;
        }).collect(Collectors.toList());
        return cvParameters;
    }

    protected Optional<CvParameter> createCvParameter(String cvPathPrefix, Map<String, ControlledVocabulary> cvm, Parameter p, String principal, CvParameterType cvParameterType, ReferenceType referenceType, String transactionUuid) {
        if (p == null) {
            return Optional.empty();
        }
        ControlledVocabulary cv = null;
        if (p.getCvLabel() != null) {
            cv = cvm.get(p.getCvLabel());
        }
        String id = CvParameter.buildId(cvPathPrefix + p.getCvAccession(), p.getName(), p.getValue(), cv);
        CvParameter cvParam = new CvParameter(cvParameterType, referenceType, p.getCvAccession(), p.getName(), p.getValue(), cv, transactionUuid);
        cvParam.setId(UUID.nameUUIDFromBytes(id.getBytes(Charset.forName("UTF8"))).toString());
        cvParam.setNativeId(id);
        cvParam.setVisibility(Visibility.PRIVATE);
        cvParam.setTransactionUuid(jobId + "");
        CvParameter optCvParam = cvParamRepository.save(cvParam);
        if (optCvParam != null && cv != null) {
            HasCvParent hcp = HasCvParent.builder().from(optCvParam).to(cv).visibility(Visibility.PRIVATE).build();
            hcp.setTransactionUuid(jobId + "");
            hasCvParentRepository.save(hcp);
        }
        return Optional.ofNullable(optCvParam);
    }

}
