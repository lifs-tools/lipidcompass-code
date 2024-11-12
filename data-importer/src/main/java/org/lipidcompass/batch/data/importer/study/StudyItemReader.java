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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;

/**
 *
 * @author nilshoffmann
 */
public class StudyItemReader implements ItemReader<StudyDto> {

    private final URI inputResource;
    private ObjectMapper objectMapper;

    public StudyItemReader(Resource source) throws IOException {
        Logger.getLogger(StudyItemReader.class.getName()).log(Level.INFO, "Reading from resource {0}", source);
        this.inputResource = source.getURI();
    }

    @Override
    public StudyDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
            return this.objectMapper.readValue(inputResource.toURL(), StudyDto.class);
        }
        return null;
    }

}
