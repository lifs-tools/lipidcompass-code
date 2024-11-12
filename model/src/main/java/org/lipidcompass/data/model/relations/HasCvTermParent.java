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
import org.lipidcompass.data.model.CvParameter;
import org.lipidcompass.data.model.Visibility;
import java.util.Date;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEdge.class)
@Edge("hasCvTermParent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class HasCvTermParent extends ArangoBaseEdge<CvParameter, CvParameter> {

    public HasCvTermParent(CvParameter child, CvParameter parent, String transactionUuid, Visibility visibility) {
        super(child, parent, transactionUuid, visibility);
    }

    public HasCvTermParent(CvParameter child, CvParameter parent) {
        super(child, parent);
    }

}