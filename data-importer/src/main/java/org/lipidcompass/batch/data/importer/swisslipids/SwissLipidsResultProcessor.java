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

import com.google.common.base.Charsets;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import static org.lipidcompass.batch.data.importer.utilities.LipidNameNormalizer.normalizedSwissLipidsShortHandString;
import java.util.UUID;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lipidcompass.data.model.Visibility;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
public class SwissLipidsResultProcessor implements ItemProcessor<SwissLipidsLipid, SwissLipidsEntry> {

    private String levelToSelect = "Category";

    public SwissLipidsResultProcessor(String levelToSelect) {
        this.levelToSelect = levelToSelect;
    }

    private Long jobId;

    @BeforeStep
    public void getInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        this.jobId = jobExecution.getJobId();
    }

    @Override
    public SwissLipidsEntry process(SwissLipidsLipid i) throws Exception {
        if (levelToSelect.equals(i.getLevel())) {
//        Lipid lipidMapsLipid = null;
//        if (i.getLipidMapsId() != null) {
//            lipidMapsLipid = lipidRepository.findByNativeId(i.getLipidMapsId());
//        }
//            LipidBuilder builder = Lipid.builder();
//            Set<String> names = new LinkedHashSet<>();
//            names.addAll(Arrays.asList(i.getName().split("\\|")));
//            names.addAll(Arrays.asList(i.getAbbreviation().split("\\|")));
//            names.addAll(Arrays.asList(i.getSynonyms().split("\\|")));
            String abbreviation = i.getAbbreviation().trim();
            if (i.getAbbreviation().contains("|")) {
                String[] shortHandAbbrs = abbreviation.split("\\|");
                abbreviation = shortHandAbbrs[0].trim();
            }
//            Optional<Lipid> lipid = LipidNameNormalizer.of(normalizedSwissLipidsShortHandString(abbreviation));
            Optional<LipidAdduct> la = normalizedSwissLipidsShortHandString(abbreviation);
            String normalizedShorthandName = "";
            LipidLevel goslinLevel = LipidLevel.UNDEFINED_LEVEL;
            if (la.isPresent()) {
                normalizedShorthandName = la.get().getLipidString();
                goslinLevel = la.get().getLipidLevel();
            } else {
                switch(i.getLevel()) {
                    case "CATEGORY" -> goslinLevel = LipidLevel.CATEGORY;
                    case "CLASS" -> goslinLevel = LipidLevel.CLASS;
                }
                normalizedShorthandName = abbreviation;
            }
//            builder.chemicalFormula(i.getFormula()).
//                    commonName(i.getAbbreviation()).
//                    normalizedShorthandName(normalizedShorthandName).
//                    exactMass(Optional.ofNullable(i.getExactMass()).orElse(Double.NaN).floatValue()).
//                    inchi(i.getInchi()).
//                    inchiKey(i.getInchiKey()).
//                    smiles(i.getSmiles()).
//                    nativeId(i.getLipidId()).
//                    nativeUrl("https://www.swisslipids.org/#/entity/" + i.getLipidId()).
//                    synonyms(names.stream().map((t) -> {
//                        return t.trim();
//                    }).collect(Collectors.toList())).systematicName(i.getName());
//            List<CrossReference> crossReferences = new ArrayList<>();
//            if (i.getChebiId() != null && !i.getChebiId().isEmpty()) {
//                crossReferences.add(new CrossReference(CrossReferenceType.CHEBI_ID, "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + i.getChebiId(), i.getChebiId()));
//            }
//            if (i.getLipidMapsId() != null && !i.getLipidMapsId().isEmpty()) {
//                crossReferences.add(new CrossReference(CrossReferenceType.LIPIDMAPS_ID, "http://www.lipidmaps.org/data/LMSDRecord.php?LMID=" + i.getLipidMapsId(), i.getLipidMapsId()));
//            }
//            if (i.getHmdbId() != null && !i.getHmdbId().isEmpty()) {
//                crossReferences.add(new CrossReference(CrossReferenceType.HMDB_ID, "http://www.hmdb.ca/metabolites/" + i.getHmdbId(), i.getHmdbId()));
//            }

            SwissLipidsEntry.SwissLipidsEntryBuilder swissLipidsEntryBuilder = SwissLipidsEntry.builder().
                    //                    id(UUID.nameUUIDFromBytes(i.getLipidId().getBytes(Charsets.UTF_8)).toString()).
                    nativeId(i.getLipidId()).
                    nativeUrl("https://www.swisslipids.org/#/entity/" + i.getLipidId());
            swissLipidsEntryBuilder.abbreviation(abbreviation).normalizedName(normalizedShorthandName).
                    description(i.getName()).smiles(i.getSmiles()).
                    level(SwissLipidsEntry.Level.valueOf(i.getLevel().replaceAll(" ", "").toUpperCase())).
                    transactionUuid(jobId + "").
                    visibility(Visibility.PUBLIC).
                    goslinLevel(goslinLevel);
            // add the parent, whose ID is stored in lipid class
            if (i.getLipidClass() != null && !i.getLipidClass().isEmpty()) {
                SwissLipidsEntry.SwissLipidsEntryBuilder parentEntryBuilder = SwissLipidsEntry.builder().
                        //                        id(UUID.nameUUIDFromBytes(i.getLipidClass().getBytes(Charsets.UTF_8)).toString()).
                        transactionUuid(jobId + "").
                        visibility(Visibility.PUBLIC).
                        nativeId(i.getLipidClass());
                swissLipidsEntryBuilder.parent(parentEntryBuilder.build());
            }
//            builder.swissLipidsEntry(swissLipidsEntryBuilder.build());
//            builder.crossReferences(crossReferences);
//            return builder.build();
            return swissLipidsEntryBuilder.build();
        }
        log.debug("Skipping {}, {} != {}", i.getLipidId(), i.getLevel(), levelToSelect);
        return null;
    }

}
