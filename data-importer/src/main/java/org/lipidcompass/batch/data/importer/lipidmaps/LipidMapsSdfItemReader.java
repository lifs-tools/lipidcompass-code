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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author nilshoffmann
 */
public class LipidMapsSdfItemReader implements ItemReader<IAtomContainer> {

    private InputStream sourceInputStream;
    private IteratingSDFReader reader;
    
    public LipidMapsSdfItemReader(File source, String fileName) {
        Logger.getLogger(LipidMapsSdfItemReader.class.getName()).log(Level.INFO, "Reading from resource {0}", source);
        try {
            FileSystemResource resource = new FileSystemResource(new File(source, fileName));
            this.sourceInputStream = resource.getInputStream();
            this.reader = new IteratingSDFReader(
                sourceInputStream, DefaultChemObjectBuilder.getInstance());
        } catch (IOException ex) {
            Logger.getLogger(LipidMapsSdfItemReader.class.getName()).
                log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public IAtomContainer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (reader.hasNext()) {
            return (IAtomContainer) reader.next();
        } else {
            reader.close();
            return null;
        }
    }

}
