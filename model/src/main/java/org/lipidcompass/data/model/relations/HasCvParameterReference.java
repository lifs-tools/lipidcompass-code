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

import com.arangodb.springframework.annotation.Edge;
import io.swagger.v3.oas.annotations.media.Schema;
import org.lipidcompass.data.model.ArangoBaseEdge;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.Visibility;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEdge.class)
@Edge(collection = "hasCvParameterReference")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class HasCvParameterReference extends ArangoBaseEdge<ArangoBaseEntity, CvParameter> {

    public static enum ReferenceType {
        BEST_ID_CONFIDENCE_MEASURE,
        SMALL_MOLECULE_QUANTIFICATION_UNIT,
        SAMPLE_DISEASE,
        SAMPLE_CELLTYPE,
        SAMPLE_ORGANISM,
        SAMPLE_TISSUE,
        SAMPLE_CUSTOM,
        ASSAY_CUSTOM,
        MS_RUN_FORMAT,
        MS_RUN_HASH_METHOD,
        MS_RUN_ID_FORMAT,
        MS_RUN_FRAGMENTATION_METHOD,
        MS_RUN_SCAN_POLARITY,
        STUDY_VARIABLE_FACTOR,
        INSTRUMENT_ANALYZER,
        INSTRUMENT_DETECTOR,
        INSTRUMENT_NAME,
        INSTRUMENT_SOURCE,
        UNSPECIFIED
    };

    public static String buildId(ArangoBaseEntity entity, CvParameter cvParam) {
        return buildId(entity.getId(), cvParam.getId());
    }

    public static String buildId(String entityId, String cvParamId) {
        String id = entityId
                + "|"
                + cvParamId;
        return UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public HasCvParameterReference(ArangoBaseEntity arangoBaseEntity, CvParameter cvParameter, String transactionUuid, Visibility visibility) {
        super(arangoBaseEntity, cvParameter, transactionUuid, visibility);
    }

}
