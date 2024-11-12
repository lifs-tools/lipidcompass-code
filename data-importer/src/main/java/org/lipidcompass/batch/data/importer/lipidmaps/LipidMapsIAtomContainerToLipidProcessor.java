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

import org.lipidcompass.data.model.CrossReference;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.springframework.batch.item.ItemProcessor;
import static org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsIAtomContainerToLipidProcessor.LipidMapsDataKeys.*;
import static org.lipidcompass.batch.data.importer.utilities.LipidNameNormalizer.normalizedLipidMapsShortHandString;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry.LipidMapsEntryBuilder;
import org.lipidcompass.data.model.relations.CrossReferenceType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.LipidAdduct;
import java.util.Optional;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lipidcompass.data.model.Visibility;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class LipidMapsIAtomContainerToLipidProcessor implements ItemProcessor<IAtomContainer, LipidMapsEntry> {

    public static enum LipidMapsDataKeys {
        ABBREVIATION,
        CATEGORY,
        MAIN_CLASS,
        SUB_CLASS,
        CLASS_LEVEL4,
        EXACT_MASS,
        FORMULA,
        INCHI,
        INCHI_KEY,
        //        PUBCHEM_SUBSTANCE_URL,
        //        LIPID_MAPS_CMPD_URL,
        LM_ID,
        NAME,
        //        COMMON_NAME,
        SYNONYMS,
        //        PUBCHEM_SID,
        PUBCHEM_CID,
        SMILES,
        STATUS,
        SYSTEMATIC_NAME
    }

    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
    private Long jobId;

//    @BeforeStep
//    public void getInterstepData(StepExecution stepExecution) {
//        JobExecution jobExecution = stepExecution.getJobExecution();
//        this.jobId = jobExecution.getJobId();
//    }

    @Override
    public LipidMapsEntry process(IAtomContainer i) throws Exception {
        this.jobId = this.stepExecution.getJobExecutionId();
        String defaultValue = "";
        log.debug("IAtomContainer: {}", i);
        String lmId = propertyToString(i, LM_ID, "");
        log.debug("Found LM_ID={}", lmId);
        String model = "";
        try ( StringWriter sw = new StringWriter()) {
            try ( MDLV2000Writer writer = new MDLV2000Writer(sw)) {
                writer.writeMolecule(i);
            }
            model = sw.toString();
        }
        String smiles = "";
        try {
            SmilesGenerator smigen = new SmilesGenerator(SmiFlavor.Canonical);
            smiles = smigen.create(i);
        } catch (CDKException ex) {
            log.error("Caught an exception while parsing SMILES for " + lmId + ":", ex);
        }
        Float mass = Float.NaN;
        if (!propertyToString(i, EXACT_MASS, defaultValue).
                isEmpty()) {
            mass = Float.parseFloat(
                    propertyToString(i, EXACT_MASS, defaultValue));
        }
        List<CrossReference> crossRefs = new LinkedList<>();
        if (!propertyToString(i, PUBCHEM_CID, defaultValue).
                isEmpty()) {
            CrossReference pubChem = CrossReference.builder().transactionUuid(jobId+"").
                    url("https://pubchem.ncbi.nlm.nih.gov/compound/" + propertyToString(i, PUBCHEM_CID, defaultValue)).
                    crossReferenceType(CrossReferenceType.PUBCHEM_CID).
                    nativeId(propertyToString(i, PUBCHEM_CID, defaultValue)).
                    visibility(Visibility.PUBLIC).
                    build();
            crossRefs.add(pubChem);
        } else {
            log.warn("''{}'' was not accessible for LipidMaps compound {}",
                    new Object[]{PUBCHEM_CID,
                        lmId});
        }
        if (!propertyToString(i, LM_ID, defaultValue).
                isEmpty()) {
            CrossReference lipidMaps = CrossReference.builder().transactionUuid(jobId+"").
                    url("https://www.lipidmaps.org/databases/lmsd/" + propertyToString(i, LM_ID, defaultValue)).
                    crossReferenceType(CrossReferenceType.LIPIDMAPS_ID).
                    nativeId(propertyToString(i, LM_ID, defaultValue)).
                    visibility(Visibility.PUBLIC).
                    build();
            crossRefs.add(lipidMaps);
        } else {
            log.warn("''{}'' was not accessible for LipidMaps compound {}",
                    new Object[]{LM_ID,
                        lmId});
        }
        log.debug("Properties for ''{}'': {}", new Object[]{lmId,
            i.getProperties()});

        String abbreviation = propertyToString(i, ABBREVIATION, defaultValue).trim();
        String commonName = propertyToString(i, NAME, defaultValue);
        Optional<LipidAdduct> la = normalizedLipidMapsShortHandString(abbreviation);
        LipidLevel level = LipidLevel.UNDEFINED_LEVEL;
        String normalizedShortHandName = "";

        if (la.isPresent()) {
            LipidAdduct lipidAdduct = la.get();
            normalizedShortHandName = lipidAdduct.getLipidString();
            level = lipidAdduct.getLipidLevel();
//            Lipid.builder().
//                chemicalFormula(lipidAdduct.getSumFormula()).
//                exactMass(lipidAdduct.getMass().floatValue()).
//                    normalizedShorthandName(normalizedShortHandName).
//                    synonyms(synonyms)
//                build();
//            
        }

        if ("".equals(normalizedShortHandName)) {
            log.warn("Parsing of '" + abbreviation + "' failed! Setting normalizedShortHandName to 'NA'");
            normalizedShortHandName = "NA"; //abbreviation.replaceAll("\\(", " ").replaceAll("\\)", "");
            log.warn("Skipping entry {}", propertyToString(i, LM_ID, defaultValue));
            return null;
        }

        String systematicName = propertyToString(i, SYSTEMATIC_NAME, defaultValue);
//        Lipid.LipidBuilder builder = Lipid.builder().
//                chemicalFormula(propertyToString(i, FORMULA, defaultValue)).
//                commonName(commonName).
//                normalizedShorthandName(normalizedShortHandName).
//                exactMass(mass).
//                inchi(propertyToString(i, INCHI, defaultValue)).
//                inchiKey(propertyToString(i, INCHI_KEY, defaultValue)).
//                mdlModel(model).
//                nativeUrl("http://www.lipidmaps.org/data/LMSDRecord.php?LMID=" + propertyToString(i, LM_ID, defaultValue)).
//                nativeId(propertyToString(i, LM_ID, defaultValue)).
//                smiles(smiles).
//                systematicName(systematicName);
//        builder.synonyms(Arrays.asList(abbreviation, normalizedShortHandName, commonName, systematicName).stream().distinct().filter((t) -> {
//            return !t.isEmpty();
//        }).collect(Collectors.toList()));
        String[] lmcInfo = splitLmNameAndAbbr(propertyToString(i, CATEGORY,
                ""), "CATEGORY", "CAT");
        String[] lmMcInfo = splitLmNameAndAbbr(propertyToString(i, MAIN_CLASS,
                ""), "MAIN CLASS", "MCLS");
        String[] lmScInfo = splitLmNameAndAbbr(propertyToString(i, SUB_CLASS,
                ""), "SUB CLASS", "SCLS");
        LipidMapsEntryBuilder subClassEntryBuilder = LipidMapsEntry.builder();
        LipidMapsEntry subClassEntry = subClassEntryBuilder.
                nativeId(propertyToString(i, LM_ID, defaultValue)).
                transactionUuid(jobId+"").
                nativeUrl("https://www.lipidmaps.org/databases/lmsd/" + propertyToString(i, LM_ID, defaultValue)).
                name(lmScInfo[0]).
                lmClassificationCode(lmScInfo[1]).
                abbreviation(abbreviation).
                normalizedName(normalizedShortHandName).
                systematicName(systematicName).
                level(LipidMapsEntry.Level.SUB_CLASS).
                goslinLevel(level).
                visibility(Visibility.PUBLIC).
                build();
//        subClassEntry.setTransactionUuid(jobId + "");
//        builder.lipidMapsSubClass(subClassEntry);

//        LipidMapsEntryBuilder mainClass = LipidMapsEntry.builder();
//        String[] lmMcInfo = splitLmNameAndAbbr(propertyToString(i, MAIN_CLASS,
//                ""), "MAIN CLASS", "MCLS");
//        LipidMapsEntry mainClassEntry = mainClass.name(lmMcInfo[0]).abbreviation(lmMcInfo[1]).level(LipidMapsEntry.Level.MainClass).children(Arrays.asList(subClassEntry)).build();
////        builder.lipidMapsMainClass(mainClassEntry);
//        subClassEntry.setParent(mainClassEntry);
//        String[] lmcInfo = splitLmNameAndAbbr(propertyToString(i, CATEGORY,
//                ""), "CATEGORY", "CAT");
//        LipidMapsEntryBuilder categoryBuilder = LipidMapsEntry.builder();
//        LipidMapsEntry category = categoryBuilder.name(lmcInfo[0]).abbreviation(lmcInfo[1]).level(LipidMapsEntry.Level.Category).children(Arrays.asList(mainClassEntry)).build();
////        builder.lipidMapsCategory(category);
////        builder.crossReferences(crossRefs);
////        Lipid l = builder.build();
//        mainClassEntry.setParent(category);
        log.debug("Built lipid {} for LM_ID={}", subClassEntry.getNormalizedName(), subClassEntry.getNativeId());
        return subClassEntry;
    }

    protected String propertyToString(IAtomContainer container,
            LipidMapsDataKeys key, String defaultValue) {
        if (container != null && key != null && container.getProperties().
                containsKey(key.name())) {
            return container.getProperties().
                    get(key.name()).
                    toString();
        }
        return defaultValue;
    }

    public String[] splitLmNameAndAbbr(String lmOntologyLevelString,
            String level, String levelAbb) {
        String regexp = "([A-Z0-9a-z \\/,\\(\\)'-\\[\\]]+) \\[([A-Za-z0-9]+)\\]";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(lmOntologyLevelString);
        if (m.matches()) {
            String[] lmNameAndAbbr = new String[]{m.group(1), m.group(2)};
            log.debug("Lipidmaps id ''{}'': {}", lmNameAndAbbr[0], lmNameAndAbbr[1]);
            return lmNameAndAbbr;
        } else {
            log.debug("Lipidmaps id ''{}'': {}", lmOntologyLevelString,
                    levelAbb);
            return new String[]{"", ""};
        }
    }

}
