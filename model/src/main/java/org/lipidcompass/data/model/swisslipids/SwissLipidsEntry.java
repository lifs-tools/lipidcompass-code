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
package org.lipidcompass.data.model.swisslipids;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.CrossReference;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.relations.HasCrossReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nils.hoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "swissLipidsEntries")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwissLipidsEntry extends ArangoBaseEntity {

    public static enum Level {
        CATEGORY, CLASS, SPECIES, MOLECULARSUBSPECIES, STRUCTURALSUBSPECIES, ISOMERICSUBSPECIES
    };
    @PersistentIndexed
    private String normalizedName;
    @PersistentIndexed
    private String abbreviation;
    private String description;
    @PersistentIndexed
    private SwissLipidsEntry.Level level;

    @PersistentIndexed
    private LipidLevel goslinLevel;

    private List<String> synonyms;
    
    private String smiles;

    @PersistentIndexed(unique = true)
    private String nativeId;

    private String nativeUrl;

//    @JsonSerialize(using = SwissLipidsEntrySerializer.class)
    @Reference
    @Ref(lazy = true)
    private SwissLipidsEntry parent;

    @Reference
    @Relations(edges = HasSwissLipidsChild.class, lazy = true, minDepth = 1, maxDepth = 1)
    private Collection<SwissLipidsEntry> children;

    @Reference
    @Relations(edges = HasCrossReference.class, lazy = true)
    private Collection<CrossReference> crossReferences;

    @Builder
    public SwissLipidsEntry(String normalizedName, String abbreviation, String description, String smiles, String nativeId, String nativeUrl, SwissLipidsEntry.Level level, LipidLevel goslinLevel, List<String> synonyms, String transactionUuid, SwissLipidsEntry parent, List<SwissLipidsEntry> children, List<CrossReference> crossReferences, Visibility visibility) {
        super(transactionUuid, visibility);
        this.normalizedName = normalizedName;
        this.abbreviation = abbreviation;
        this.description = description;
        this.smiles = smiles;
        this.nativeId = nativeId;
        this.nativeUrl = nativeUrl;
        this.level = level;
        this.goslinLevel = goslinLevel;
        this.parent = parent;
        this.synonyms = synonyms;
        this.children = children == null ? Collections.emptyList() : children;
        this.crossReferences = crossReferences == null ? Collections.emptyList() : crossReferences;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SwissLipidsEntry.class.getSimpleName()).
                append("{").
                append("normalizedName:").append(normalizedName).append(", ").
                append("abbreviation:").append(abbreviation).append(", ").
                append("synonyms:").append(synonyms.stream().collect(Collectors.joining(",", "[", "]"))).append(", ").
                append("description:").append(description).append(", ").
                append("level:").append(level.name()).append(", ").
                append("smiles:").append(smiles).append(", ").
                append("nativeId:").append(nativeId).append(", ").
                append("nativeUrl:").append(nativeUrl).append(", ").
                append("parent:").append(parent == null ? "null" : parent.getNativeId()).append(", ").
                append("children:").append(children.stream().map((t) -> {
            return t.getNativeId();
        }).collect(Collectors.joining(",", "[", "]"))).append(", ").
                append("crossReferences:").append(crossReferences.stream().map((t) -> {
            return t.toString();
        }).collect(Collectors.joining(",", "[", "]"))).append(", ").
                append("}");
        return sb.toString();
    }

}
