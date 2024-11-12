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

import lombok.Data;

/**
 *
 * @author nils.hoffmann
 */
@Data
public class SwissLipidsLipid {

    public static final String[] CSV_ALL_FIELDS = {"Lipid ID", "Level", "Name", "Abbreviation*", "Synonyms*", "Lipid class", "Parent",
        "Components*", "SMILES (pH7.3)", "InChI (pH7.3)", "InChI key (pH7.3)", "Formula (pH7.3)", "Charge (pH7.3)",
        "Mass (pH7.3)", "Exact Mass (neutral form)", "Exact m/z of [M.]+", "Exact m/z of [M+H]+", "Exact m/z of [M+K]+",
        "Exact m/z of [M+Na]+", "Exact m/z of [M+Li]+", "Exact m/z of [M+NH4]+", "Exact m/z of [M-H]-",
        "Exact m/z of [M+Cl]-", "Exact m/z of [M+OAc]-", "CHEBI", "LIPID MAPS", "HMDB", "PMID"};
    public static final String[] CSV_MIN_FIELDS = {"Lipid ID", "Level", "Name", "Abbreviation*", "Synonyms*", "Lipid class", "Parent",
        "Components*", "SMILES (pH7.3)", "InChI (pH7.3)", "InChI key (pH7.3)", "Formula (pH7.3)", "Charge (pH7.3)",
        "Mass (pH7.3)", "Exact Mass (neutral form)", "CHEBI", "LIPID MAPS", "HMDB", "PMID"};

    private String lipidId;
    private String level;
    private String name;
    private String abbreviation;
    private String synonyms;
    private String lipidClass;
    private String parent;
    private String components;
    private String smiles;
    private String inchi;
    private String inchiKey;
    private String formula;
    private String charge;
    private Double exactMass;
    private String chebiId;
    private String lipidMapsId;
    private String hmdbId;
    private String pmId;

}
