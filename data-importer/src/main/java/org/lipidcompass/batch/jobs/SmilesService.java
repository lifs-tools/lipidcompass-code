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
package org.lipidcompass.batch.jobs;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.batch.rest.dto.SmilesDto;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nilshoffmann
 */
@Slf4j
@Service
public class SmilesService {

    public SmilesDto canonicalizeSmiles(@RequestBody SmilesDto inputSmiles, Authentication authentication) throws Exception {
//        [C@](COP(=O)(O)O)([H])(OC(CCCCCCC/C=C\CCCC)=O)COC(CCCCCCCCCCCCC)=O
        log.info("Using SMILES flavor: {}", inputSmiles.getSmilesFlavor());
        inputSmiles.setOutputSmiles(inputSmiles.getInputSmiles().stream().map(smiles -> {
            try {
                SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
                IAtomContainer m = sp.parseSmiles(smiles);
                SmilesGenerator sg = new SmilesGenerator(inputSmiles.getSmilesFlavor());
                return sg.create(m);
            } catch (InvalidSmilesException e) {
                log.error("Invalid SMILES: " + smiles, e);
                return "INVALID: " + e.getLocalizedMessage();
            } catch (CDKException ex) {
                log.error("CDKException: " + smiles, ex);
                return "EXCEPTION: " + ex.getLocalizedMessage();
            }
        }).collect(Collectors.toList()));
        return inputSmiles;
    }
}
