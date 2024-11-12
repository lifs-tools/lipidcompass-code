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
package org.lipidcompass.batch;

import org.lipidcompass.batch.data.importer.lipidmaps.LipidMapsSdfItemReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author nilshoffmann
 */
//@Slf4j
public class SdfToTable {

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

    public static void main(String[] args) {
        InputStream sourceInputStream;
        IteratingSDFReader reader;
        try {
            FileSystemResource resource = new FileSystemResource(new File(args[0]));
            sourceInputStream = resource.getInputStream();
            reader = new IteratingSDFReader(
                    sourceInputStream, DefaultChemObjectBuilder.getInstance());
            File outFile = new File("lipidMaps-names.tsv");
            System.out.println("Writing output to " + outFile.getAbsolutePath());
            try (BufferedWriter w = new BufferedWriter(new FileWriter(outFile))) {
                w.write(new StringBuilder().append(SdfToTable.LipidMapsDataKeys.LM_ID.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.NAME.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.SYSTEMATIC_NAME.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.ABBREVIATION.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.CATEGORY.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.MAIN_CLASS.name()).append("\t"
                ).append(SdfToTable.LipidMapsDataKeys.SUB_CLASS.name()).toString()
                );
                w.newLine();
                long items = 0;
                while (reader.hasNext()) {
                    IAtomContainer container = (IAtomContainer) reader.next();
//                    System.out.println(container.getProperties());
                    String lmId = container.getProperty("LM_ID");
                    if (lmId == null) {
                        throw new RuntimeException("Entry was not parseable!");
                    }
                    System.out.println("Processing entry: " + lmId);
                    Map<String, String> entry = new LinkedHashMap<>();
                    entry.put(SdfToTable.LipidMapsDataKeys.LM_ID.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.LM_ID, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.NAME.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.NAME, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.SYSTEMATIC_NAME.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.SYSTEMATIC_NAME, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.ABBREVIATION.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.ABBREVIATION, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.CATEGORY.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.CATEGORY, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.MAIN_CLASS.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.MAIN_CLASS, ""));
                    entry.put(SdfToTable.LipidMapsDataKeys.SUB_CLASS.name(), propertyToString(container, SdfToTable.LipidMapsDataKeys.SUB_CLASS, ""));
//                    System.out.println("Adding entry: " + entry);
                    StringBuilder sb = new StringBuilder();
                    sb.
                            append(entry.get(SdfToTable.LipidMapsDataKeys.LM_ID.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.NAME.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.SYSTEMATIC_NAME.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.ABBREVIATION.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.CATEGORY.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.MAIN_CLASS.name())).append("\t").
                            append(entry.get(SdfToTable.LipidMapsDataKeys.SUB_CLASS.name()));
                    w.write(sb.toString());
//                    System.out.println("Writing line: "+sb.toString());
                    w.newLine();
                    items++;
                }
                w.flush();
                w.close();
                System.out.println("Processed " + items + " entries");
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LipidMapsSdfItemReader.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static String propertyToString(IAtomContainer container,
            SdfToTable.LipidMapsDataKeys key, String defaultValue) {
        if (container != null && key != null && container.getProperties().
                containsKey(key.name())) {
            return container.getProperties().
                    get(key.name()).
                    toString();
        }
        return defaultValue;
    }

    public static String[] splitLmNameAndAbbr(String lmOntologyLevelString,
            String level, String levelAbb) {
        String regexp = "([A-Z0-9a-z \\/,\\(\\)'-\\[\\]]+) \\[([A-Za-z0-9]+)\\]";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(lmOntologyLevelString);
        if (m.matches()) {
            String[] lmNameAndAbbr = new String[]{m.group(1), m.group(2)};
//            log.debug("Lipidmaps id ''{}'': {}", lmNameAndAbbr[0], lmNameAndAbbr[1]);
            return lmNameAndAbbr;
        } else {
//            log.debug("Lipidmaps id ''{}'': {}", lmOntologyLevelString,
//                    levelAbb);
            return new String[]{"", ""};
        }
    }
}
