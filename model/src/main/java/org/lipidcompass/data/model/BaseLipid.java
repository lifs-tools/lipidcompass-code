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

import java.util.Collection;
import java.util.List;

import org.lifstools.jgoslin.domain.LipidLevel;
import org.lipidcompass.data.model.relations.HasCrossReference;
import org.springframework.data.annotation.Reference;

import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author nils.hoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PersistentIndex(fields = {"normalizedShorthandName"}, unique = false)
public class BaseLipid extends ArangoBaseEntity {

    private String nativeUrl;

    private String commonName;

    private String normalizedShorthandName;

    private LipidLevel lipidLevel;

    private List<String> synonyms;

    private String systematicName;

    private String chemicalFormula;

    private Float exactMass;

    private String inchiKey;

    private String inchi;

    private String smiles;
    private String mdlModel;
    @Reference
    @Relations(edges = HasCrossReference.class, lazy = true)
    private Collection<CrossReference> crossReferences;

    public BaseLipid(String nativeUrl, String commonName, String normalizedShorthandName, LipidLevel lipidLevel, List<String> synonyms, String systematicName, String chemicalFormula, Float exactMass, String inchiKey, String inchi, String smiles, String mdlModel, Collection<CrossReference> crossReferences, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.nativeUrl = nativeUrl;
        this.commonName = commonName;
        this.normalizedShorthandName = normalizedShorthandName;
        this.lipidLevel = lipidLevel;
        this.synonyms = synonyms;
        this.systematicName = systematicName;
        this.chemicalFormula = chemicalFormula;
        this.exactMass = exactMass;
        this.inchiKey = inchiKey;
        this.inchi = inchi;
        this.smiles = smiles;
        this.mdlModel = mdlModel;
        this.crossReferences = crossReferences;
    }
}
