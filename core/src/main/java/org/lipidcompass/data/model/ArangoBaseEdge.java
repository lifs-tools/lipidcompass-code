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

import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Reference;

/**
 * Edges may be directional. The first one will be called 'from', the second one
 * 'to'.
 *
 * @author nilshoffmann
 * @param <FROM> type of the source for this edge
 * @param <TO>
 */
@Schema(allOf = ArangoBaseEntity.class)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public abstract class ArangoBaseEdge<FROM, TO> extends ArangoBaseEntity {

    @Reference
    @From(lazy = true)
    private FROM from;

    @Reference
    @To(lazy = true)
    private TO to;

    public ArangoBaseEdge(FROM from, TO to, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.from = from;
        this.to = to;
    }
}
