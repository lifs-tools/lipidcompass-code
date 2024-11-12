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
package org.lipidcompass.data.model.lipidmaps;

import com.arangodb.entity.KeyType;
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
@Document(value = "lipidMapsEntries", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LipidMapsEntry extends ArangoBaseEntity {

    public static enum Level {
        CATEGORY, MAIN_CLASS, SUB_CLASS
    };

    private String lmClassificationCode;

    @PersistentIndexed
    private String abbreviation;

    @PersistentIndexed
    private String normalizedName;

    @PersistentIndexed
    private String name;

    @PersistentIndexed
    private String systematicName;

    @PersistentIndexed
    private String lipidMapsCategory;

    @PersistentIndexed
    private String lipidMapsMainClass;

    @PersistentIndexed(unique = true)
    private String nativeId;

    private String nativeUrl;

    @PersistentIndexed
    private Level level;

    @PersistentIndexed
    private LipidLevel goslinLevel;

//    @JsonSerialize(using = LipidMapsEntrySerializer.class)
    @Reference
    @Ref(lazy = true)
    private LipidMapsEntry parent;

    @Reference
    @Relations(edges = HasLipidMapsChild.class, lazy = true)
    private Collection<LipidMapsEntry> children;

    @Reference
    @Relations(edges = HasCrossReference.class, lazy = true)
    private Collection<CrossReference> crossReferences;

    @Builder
    public LipidMapsEntry(String lmClassificationCode, String abbreviation, String normalizedName, String name, String systematicName, String lipidMapsCategory, String lipidMapsMainClass, String nativeId, String nativeUrl, Level level, LipidLevel goslinLevel, String transactionUuid, LipidMapsEntry parent, List<LipidMapsEntry> children, List<CrossReference> crossReferences, Visibility visibility) {
        super(transactionUuid, visibility);
        this.lmClassificationCode = lmClassificationCode;
        this.abbreviation = abbreviation;
        this.normalizedName = normalizedName;
        this.name = name;
        this.systematicName = systematicName;
        this.lipidMapsCategory = lipidMapsCategory;
        this.lipidMapsMainClass = lipidMapsMainClass;
        this.nativeId = nativeId;
        this.nativeUrl = nativeUrl;
        this.level = level;
        this.goslinLevel = goslinLevel;
        this.parent = parent;
        this.children = children == null ? Collections.emptyList() : children;
        this.crossReferences = crossReferences == null ? Collections.emptyList() : crossReferences;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LipidMapsEntry.class.getSimpleName()).
                append("{").
                append("lmClassificationCode:").append(lmClassificationCode).append(", ").
                append("normalizedName:").append(normalizedName).append(", ").
                append("abbreviation:").append(abbreviation).append(", ").
                append("name:").append(name).append(", ").
                append("systematicName:").append(systematicName).append(", ").
                append("lipidMapsCategory:").append(lipidMapsCategory).append(", ").
                append("lipidMapsMainClass:").append(lipidMapsMainClass).append(", ").
                append("level:").append(level.name()).append(", ").
                append("goslinLevel:").append(goslinLevel.name()).append(", ").
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
