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
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.lipidcompass.data.model.ArangoBaseEntity;
import org.lipidcompass.data.model.GroupedLipidQuantity;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.Visibility;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;

/**
 *
 * @author nils.hoffmann
 */
@Edge("hasAssayLipidQuantity")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HasGroupedAssayLipidQuantity extends ArangoBaseEntity {

    @Reference
    @From
    private GroupedLipidQuantity groupedLipidQuantity;

    @Reference
    @To
    private Lipid lipidQuantity;

    @Builder
    public HasGroupedAssayLipidQuantity(GroupedLipidQuantity groupedLipidQuantity, Lipid lipidQuantity, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.groupedLipidQuantity = groupedLipidQuantity;
        this.lipidQuantity = lipidQuantity;
    }

}
