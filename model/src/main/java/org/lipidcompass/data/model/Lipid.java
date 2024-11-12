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
package org.lipidcompass.data.model;

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Relations;
import org.lipidcompass.data.model.lipidmaps.LipidMapsEntry;
import org.lipidcompass.data.model.relations.HasLipidMapsReference;
import org.lipidcompass.data.model.relations.HasSwissLipidsReference;
import org.lipidcompass.data.model.swisslipids.SwissLipidsEntry;
import org.lifstools.jgoslin.domain.LipidLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lipidcompass.data.model.relations.HasFattyAcyl;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nilshoffmann
 */
@Document(value = "lipids", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Lipid extends BaseLipid {

    @PersistentIndexed
    private String lipidCategory;

    @PersistentIndexed
    private String lipidClass;

    @PersistentIndexed
    private String lipidSpecies;

    @PersistentIndexed
    private String lipidMolecularSpecies;

    @PersistentIndexed
    private String lipidSnPosition;

    @PersistentIndexed
    private String lipidStructureDefined;

    @PersistentIndexed
    private String lipidFullStructure;

    @PersistentIndexed
    private String lipidCompleteStructure;

    private String nativeId;

    private FattyAcyl lipidSpeciesInfo;

    @Reference
    @Relations(edges = HasLipidMapsReference.class, lazy = true)
    private Collection<LipidMapsEntry> lipidMapsEntry;

    @Reference
    @Relations(edges = HasSwissLipidsReference.class, lazy = true)
    private Collection<SwissLipidsEntry> swissLipidsEntry;

    @Reference
    @Relations(edges = HasFattyAcyl.class, lazy = true)
    private Collection<FattyAcyl> fattyAcyls;

    @Builder
    public Lipid(LipidLevel lipidLevel, String lipidCategory, String lipidClass, String lipidSpecies, String lipidMolecularSpecies, String lipidSnPosition, String lipidStructureDefined, String lipidFullStructure, String lipidCompleteStructure, String nativeId, String nativeUrl, String commonName, String normalizedShorthandName, List<String> synonyms, String systematicName, String chemicalFormula, Float exactMass, String inchiKey, String inchi, String smiles, String mdlModel, FattyAcyl lipidSpeciesInfo, Collection<CrossReference> crossReferences, String transactionUuid, Visibility visibility) {
        super(nativeUrl, commonName, normalizedShorthandName, lipidLevel, synonyms, systematicName, chemicalFormula, exactMass, inchiKey, inchi, smiles, mdlModel, crossReferences, transactionUuid, visibility);
        this.lipidSpeciesInfo = lipidSpeciesInfo;
        this.lipidCategory = lipidCategory;
        this.lipidClass = lipidClass;
        this.lipidSpecies = lipidSpecies;
        this.lipidMolecularSpecies = lipidMolecularSpecies;
        this.lipidSnPosition = lipidSnPosition;
        this.lipidStructureDefined = lipidStructureDefined;
        this.lipidFullStructure = lipidFullStructure;
        this.lipidCompleteStructure = lipidCompleteStructure;
    }
}
