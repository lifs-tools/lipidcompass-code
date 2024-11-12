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
package org.lipidcompass.batch.data.importer.swisslipids;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
public class SwissLipidsFieldSetMapper implements FieldSetMapper<SwissLipidsLipid> {

    @Override
    public SwissLipidsLipid mapFieldSet(FieldSet arg0) throws BindException {
//        log.info("{}", Arrays.asList(arg0.getNames()));
//        CSV_ALL_FIELDS = {"Lipid ID"
//        , "Level", "Name", "Abbreviation*", "Synonyms*", "Lipid class", "Parent",
//        "Components*", "SMILES (pH7.3)", "InChI (pH7.3)", "InChI key (pH7.3)", "Formula (pH7.3)", "Charge (pH7.3)",
//        "Mass (pH7.3)", "Exact Mass (neutral form)", "Exact m/z of [M.]+", "Exact m/z of [M+H]+", "Exact m/z of [M+K]+",
//        "Exact m/z of [M+Na]+", "Exact m/z of [M+Li]+", "Exact m/z of [M+NH4]+", "Exact m/z of [M-H]-",
//        "Exact m/z of [M+Cl]-", "Exact m/z of [M+OAc]-", "CHEBI", "LIPID MAPS", "HMDB", "PMID"};
        SwissLipidsLipid lipid = new SwissLipidsLipid();
        lipid.setLipidId(arg0.readString("Lipid ID"));
        lipid.setLevel(arg0.readString("Level"));
        lipid.setName(arg0.readString("Name"));
        lipid.setAbbreviation(arg0.readString("Abbreviation*"));
        lipid.setSynonyms(arg0.readString("Synonyms*"));
        lipid.setLipidClass(arg0.readString("Lipid class"));
        lipid.setParent(arg0.readString("Parent"));
        lipid.setComponents(arg0.readString("Components*"));
        lipid.setSmiles(arg0.readString("SMILES (pH7.3)"));
        lipid.setInchi(arg0.readString("InChI (pH7.3)"));
        lipid.setInchiKey(arg0.readString("InChI key (pH7.3)"));
        lipid.setFormula(arg0.readString("Formula (pH7.3)"));
        lipid.setCharge(arg0.readString("Charge (pH7.3)"));
        String exactMass = arg0.readString("Exact Mass (neutral form)");
        if(!exactMass.isEmpty()) {
            lipid.setExactMass(Double.parseDouble(exactMass));
        }
        lipid.setChebiId(arg0.readString("CHEBI"));
        lipid.setLipidMapsId(arg0.readString("LIPID MAPS"));
        lipid.setHmdbId(arg0.readString("HMDB"));
        lipid.setPmId(arg0.readString("PMID"));
       
        return lipid;
    }
    
}
