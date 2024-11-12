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
package org.lipidcompass.backend.repository;

import com.arangodb.model.AqlQueryOptions;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.annotation.QueryOptions;
import org.lipidcompass.data.model.Lipid;
import org.lipidcompass.data.model.Lipidome;
import org.lifstools.jgoslin.domain.LipidLevel;
import java.util.List;
import org.lipidcompass.data.model.Study;
import org.lipidcompass.data.model.dto.IsobaricLipids;
import org.lipidcompass.data.model.dto.query.MatchMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nilshoffmann
 */
@Repository
public interface LipidomeRepository extends SecuredArangoRepository<Lipidome>, CustomArangoRepository<Lipidome, String> {

}
