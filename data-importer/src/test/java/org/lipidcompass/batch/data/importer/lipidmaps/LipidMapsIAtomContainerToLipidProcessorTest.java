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

import org.lipidcompass.test.utils.ClassPathFile;
import org.lipidcompass.test.utils.ExtractClassPathFiles;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.Parameterized;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
public class LipidMapsIAtomContainerToLipidProcessorTest {

    @ClassRule
    public static final TemporaryFolder TF = new TemporaryFolder();

    @ClassRule
    public static final ExtractClassPathFiles EXTRACT_FILES = new ExtractClassPathFiles(TF, new ClassPathFile("/data/", "LMSDFDownload3Jan19-three-items.sdf"));

    @Parameterized.Parameters(
            name = "{index}: parsing SDF file ''{0}''.")
    public static Collection<File> data() {
        return EXTRACT_FILES.getFiles();
    }

    @Parameterized.Parameter(0)
    public File sdfFile;

    @Test
    public void testParseSdf() throws IOException, Exception {
        log.info("EXTRACT_FILES basedir: {}", EXTRACT_FILES.getBaseDir());
        for (File sdfFile : EXTRACT_FILES.getFiles()) {
            log.info("{}", sdfFile);
            LipidMapsSdfItemReader reader = new LipidMapsSdfItemReader(sdfFile.getParentFile(), sdfFile.getName());
            int readCount = 0;
            while (readCount < 2) {
                IAtomContainer container = reader.read();
                Assert.assertNotNull(container);
                Assert.assertTrue(container.getAtomCount() > 0);
                Assert.assertTrue(container.getBondCount() > 0);
                Assert.assertNull(container.getProperty("<LM_ID>"));
                Assert.assertNotNull(container.getProperty("LM_ID"));
                readCount++;
            }
            Assert.assertNull(reader.read());
        }
    }

}
