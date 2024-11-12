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
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author nilshoffmann
 */
@Schema(allOf = ArangoBaseEntity.class)
@Document(value = "databaseInfo", keyType = KeyType.autoincrement, keyIncrement = 1)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseInfo extends ArangoBaseEntity {

    private String releaseVersion;

    private Date releaseDate;
    
    private String modelVersion;

    @Builder
    public DatabaseInfo(String releaseVersion, Date releaseDate, String modelVersion, String transactionUuid, Visibility visibility) {
        super(transactionUuid, visibility);
        this.releaseVersion = releaseVersion;
        this.releaseDate = releaseDate;
        this.modelVersion = modelVersion;
    }
}
