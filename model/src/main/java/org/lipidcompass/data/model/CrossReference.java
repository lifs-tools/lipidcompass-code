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

import org.lipidcompass.data.model.relations.CrossReferenceType;

import com.arangodb.entity.KeyType;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.PersistentIndexed;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Nils Hoffmann <nils.hoffmann@isas.de>
 *
 * LIPIDBANK_ID PUBCHEM_SID PUBCHEM_CID KEGG_ID HMDBID CHEBI_ID DOI PUBMED_ID
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "crossReferences", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@PersistentIndex(fields = {"nativeId"}, unique = true)
public class CrossReference extends ArangoBaseEntity {

    @PersistentIndexed
    private CrossReferenceType crossReferenceType;

    @PersistentIndexed
    private String url;

    private String nativeId;

    @Builder
    public CrossReference(CrossReferenceType crossReferenceType, String url, String nativeId, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.crossReferenceType = crossReferenceType;
        this.url = url;
        this.nativeId = nativeId;
    }

}
