/*
 * Copyright 2022 The LipidCompass Developers.
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lifstools.jgoslin.parser.SwissLipidsParser;
import org.lifstools.jgoslin.parser.SwissLipidsParserEventHandler;
import org.springframework.batch.item.ItemWriter;
import org.lipidcompass.batch.data.importer.utilities.ExternalDatabaseReference;
import org.springframework.batch.item.Chunk;

/**
 *
 * @author Nils Hoffmann
 */
@Slf4j
public class SwissLipidsFlatFileItemWriter implements ItemWriter<SwissLipidsLipid> {

    private File outputDirectory;

    public void setOutputDirectory(File file) {
        this.outputDirectory = file;
    }

    @Override
    public void write(Chunk<? extends SwissLipidsLipid> list) throws Exception {
        File outputFile = new File(outputDirectory, "swiss-lipids-normalized.tsv");
        outputFile.getParentFile().mkdirs();
        boolean append = outputFile.exists();
        log.info("Writing {} entries to file '{}'", list.size(), outputFile);
        SwissLipidsParser parser = new SwissLipidsParser();
        SwissLipidsParserEventHandler handler = parser.newEventHandler();
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, append))) {
            // SLID	LEVEL	NAME	ABBREVIATION	SYNONYMS1	SYNONYMS2	SYNONYMS3	SYNONYMS4	SYNONYMS5
            if (!append) {
                bw.write("databaseUrl\tdatabaseElementId\tlipidLevel\tnativeAbbreviation\tnativeName\tnormalizedName");
                bw.newLine();
            }
            list.getItems().stream().map((swissLipidsEntry) -> {
                Optional<LipidAdduct> opt = parseAbbreviation(swissLipidsEntry, parser, handler);
                String lipidString = "NA";
                LipidLevel level = LipidLevel.UNDEFINED_LEVEL;
                if (opt.isPresent()) {
                    lipidString = opt.get().getLipidString();
                    level = opt.get().getLipidLevel();
                }
                return new ExternalDatabaseReference(
                        "https://www.swisslipids.org/#/entity/",
                        swissLipidsEntry.getLipidId(),
                        level.name(),
                        swissLipidsEntry.getAbbreviation(),
                        swissLipidsEntry.getName(),
                        lipidString
                );
            }).forEach((edr) -> {
                StringBuilder sb = new StringBuilder();
                sb.
                        append(edr.databaseUrl()).append("\t").
                        append(edr.databaseElementId()).append("\t").
                        append(edr.lipidLevel()).append("\t").
                        append(edr.nativeName()).append("\t").
                        append(edr.nativeAbbreviation()).append("\t").
                        append(edr.normalizedName()).append("\t");
                try {
                    bw.write(sb.toString());
                    bw.newLine();
                } catch (IOException ex) {
                    log.error("Exception while writing external database reference " + edr + " to file " + outputFile, ex);
                }
            });
            bw.flush();
        }
    }

    private static Optional<LipidAdduct> parseAbbreviation(SwissLipidsLipid swissLipidsLipid, SwissLipidsParser parser, SwissLipidsParserEventHandler handler) {
        Optional<LipidAdduct> opt = Optional.ofNullable(parser.parse(swissLipidsLipid.getAbbreviation(), handler, false));
        return opt;
    }

}
