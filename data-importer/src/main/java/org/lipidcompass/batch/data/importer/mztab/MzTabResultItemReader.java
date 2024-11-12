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

import de.isas.mztab2.io.MzTabFileParser;
import de.isas.mztab2.model.MzTab;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorType;

/**
 *
 * @author nilshoffmann
 */
public class MzTabResultItemReader implements ItemReader<MzTab> {

    private MzTabFileParser reader;
    private final URI inputResource;

    public MzTabResultItemReader(Resource source) throws IOException {
        Logger.getLogger(MzTabResultItemReader.class.getName()).log(Level.INFO, "Reading from resource {0}", source);
        this.inputResource = source.getURI();
    }

    @Override
    public MzTab read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(this.reader == null) {
            this.reader = new MzTabFileParser(inputResource);
            MZTabErrorList errorList = this.reader.parse(System.out, MZTabErrorType.Level.Warn, 100);
            if (!errorList.
                isEmpty()) {
                throw new ParseException(
                    "File from resource " + inputResource + " contained errors: " + this.reader.
                        getErrorList().
                        toString());
            }
            return this.reader.getMZTabFile();
        } else {
            return null;
        }
    }

}
