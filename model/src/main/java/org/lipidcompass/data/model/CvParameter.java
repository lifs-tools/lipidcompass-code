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

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndexed;
import com.arangodb.springframework.annotation.Relations;
import io.swagger.v3.oas.annotations.media.Schema;
import org.lipidcompass.data.model.relations.HasCvParameterReference.ReferenceType;
import org.lipidcompass.data.model.relations.HasCvParent;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lipidcompass.data.model.relations.HasCvTermParent;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "parameters")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CvParameter extends ArangoBaseEntity {

    public static enum CvParameterType {
        MARKER, BOOLEAN, NOMINAL, INTEGER, FLOAT
    };

    @PersistentIndexed(unique = false)
    private String accession;

    @PersistentIndexed(unique = false)
    private String name;

    @PersistentIndexed(unique = false)
    private String value;

    @PersistentIndexed(unique = false)
    private Boolean booleanValue;

    @PersistentIndexed(unique = false)
    private Long longValue;

    @PersistentIndexed(unique = false)
    private Double doubleValue;

    @PersistentIndexed(unique = false)
    private CvParameterType cvParameterType;

    @Reference
    @Relations(edges = HasCvParent.class, lazy = true)
    private ControlledVocabulary cv;

    @Reference
    @Relations(edges = HasCvTermParent.class, lazy = true)
    private Collection<CvParameter> parents;

    @PersistentIndexed
    private Collection<String> synonyms;

    @PersistentIndexed
    private ReferenceType referenceType;

    @PersistentIndexed(unique = true)
    private String nativeId;

    public static String buildId(CvParameter cvParam, ControlledVocabulary cv) {
        return buildId(cvParam.getAccession(), cvParam.getName(), cvParam.getValue(), cv);
    }

    public static String buildId(String accession, String name, String value, ControlledVocabulary cv) {
        String cvValue = ((value == null) ? "" : value);
        String id = (cv == null ? "" : (cv.getLabel()
                + cv.getVersion())) + "|"
                + accession + "|"
                + name + "|"
                + cvValue;
        return UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public CvParameter(CvParameterType cvParameterType, ReferenceType referenceType, String accession, String name, String value, ControlledVocabulary cv, String transactionUuid) {
        this(cvParameterType, referenceType, accession, name, value, CvParameter.buildId(accession, name, value, cv), transactionUuid, Visibility.PUBLIC);
    }

    @Builder
    public CvParameter(CvParameterType cvParameterType, ReferenceType referenceType, String accession, String name, String value, String nativeId, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        if (accession == null) {
            throw new IllegalArgumentException("Argument accession must not be null!");
        }
        this.nativeId = nativeId;
        this.accession = accession;
        this.name = name;
        if (name == null) {
            throw new IllegalArgumentException("Argument name must not be null!");
        }
        if (cvParameterType == null) {
            throw new IllegalArgumentException("Argument cvParameterType must not be null!");
        }
        this.cvParameterType = cvParameterType;
        switch (cvParameterType) {
            case FLOAT -> {
                this.doubleValue = value == null ? null : Double.parseDouble(value);
            }
            case INTEGER -> {
                this.longValue = value == null ? null : Long.parseLong(value);
            }
            case BOOLEAN -> {
                this.booleanValue = value == null ? null : Boolean.parseBoolean(value);
            }
            case NOMINAL -> {
                this.value = value == null ? null : value;
            }
            case MARKER -> {
                this.value = null;
            }
            default ->
                throw new IllegalStateException("Unhandled cvParameterType: " + cvParameterType.toString());
        }
        this.referenceType = referenceType;
    }

}
