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
package org.lipidcompass.data.model.relations;

/**
 *
 * @author nils.hoffmann
 */
public enum CrossReferenceType {
    LIPIDMAPS_ID, 
    SWISSLIPIDS_ID, 
    LIPIDCOMPASS_EXPERIMENT_ID, 
    LIPIDBANK_ID, 
    PUBCHEM_SID, 
    PUBCHEM_CID, 
    KEGG_ID, 
    HMDB_ID, 
    CHEBI_ID, 
    DOI, 
    PUBMED_ID, 
    GO_TERM_ID,
    EC_ID,
    METACYC_ID,
    REACTOME_ID,
    RHEA_ID,
    MASS_BANK_ID;
}
