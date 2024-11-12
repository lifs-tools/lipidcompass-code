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
package org.lipidcompass.batch.data.importer.lipidmaps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lifstools.jgoslin.parser.ShorthandParser;
import org.lifstools.jgoslin.parser.ShorthandParserEventHandler;
import org.lipidcompass.batch.data.importer.utilities.ExternalDatabaseReference;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author Nils Hoffmann
 * @link https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#flatFileItemWriter
 */
@Slf4j
public class LipidMapsFlatFileItemWriter implements ItemWriter<LipidMapsEntry> {

    private File outputDirectory;
    
    public void setOutputDirectory(File file) {
        this.outputDirectory = file;
    }
    
    @Override
    public void write(Chunk<? extends LipidMapsEntry> list) throws Exception {
        File outputFile = new File(outputDirectory, "lipid-maps-normalized.tsv");
        outputFile.getParentFile().mkdirs();
        boolean append = outputFile.exists();
        log.info("Writing {} entries to file '{}'", list.size(), outputFile);
        ShorthandParser parser = new ShorthandParser();
        ShorthandParserEventHandler handler = parser.newEventHandler();
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, append))) {
            // SLID	LEVEL	NAME	ABBREVIATION	SYNONYMS1	SYNONYMS2	SYNONYMS3	SYNONYMS4	SYNONYMS5
            if(!append) {
                bw.write("databaseUrl\tdatabaseElementId\tlipidLevel\tnativeAbbreviation\tnativeName\tnormalizedName");
                bw.newLine();
            }
            list.getItems().stream().map((lipidMapsEntry) -> {
                Optional<LipidAdduct> lipidAdduct = Optional.ofNullable(parser.parse(lipidMapsEntry.getNormalizedName(), handler, false));
                return new ExternalDatabaseReference(
                        "https://lipidmaps.org/databases/lmsd/",
                        lipidMapsEntry.getNativeId(),
                        lipidAdduct.map((t) -> {
                            return t.getLipidLevel().name();
                        }).orElse(LipidLevel.UNDEFINED_LEVEL.name()),
                        lipidMapsEntry.getAbbreviation(),
                        lipidMapsEntry.getSystematicName(),
                        lipidMapsEntry.getNormalizedName()
                );
            }).forEach((edr) -> {
                StringBuilder sb = new StringBuilder();
                sb.
                        append(edr.databaseUrl()).append("\t").
                        append(edr.databaseElementId()).append("\t").
                        append(edr.lipidLevel()).append("\t").
                        append(edr.nativeAbbreviation()).append("\t").
                        append(edr.nativeName()).append("\t").
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

}
