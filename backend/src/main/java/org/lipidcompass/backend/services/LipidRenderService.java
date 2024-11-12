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
package org.lipidcompass.backend.services;

import org.lipidcompass.backend.config.CachingConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.openscience.cdk.depict.Depiction;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Service
public class LipidRenderService {

    @Cacheable(cacheNames = {CachingConfiguration.SMILES_SVG})
    public String render(String smiles) throws InvalidSmilesException, CDKException {
        SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer m = sp.parseSmiles(smiles.replaceAll("\\*", "R"));
        DepictionGenerator dg = new DepictionGenerator();
        return dg.withAtomColors().withDeuteriumSymbol(true).depict(m).toSvgStr();
    }
}
